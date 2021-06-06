package com.my.web.command.senior_cashier;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.my.db.entities.*;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Generate cashier report servlet
 */
@WebServlet(urlPatterns = "/generateCashierReport")
public class GenerateCashierReportServlet extends HttpServlet {

    private static final long serialVersionUID = 8102932984891234652L;
    private static final Logger logger = Logger.getLogger(GenerateCashierReportServlet.class);
    private static final String FILE = System.getProperty("catalina.home") + "\\logs\\final-task-reports\\UserReport";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("generate cashier report servlet is started at the POST method");
        HttpSession session = req.getSession(false);
        logger.debug("SESSION => " + session);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userRole") == null
                || !session.getAttribute("userRole").equals(Role.SENIOR_CASHIER)) {
            String errorMessage = "You are not logged or not have access to the chosen page";
            req.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            resp.sendRedirect(Commands.ERROR_PAGE_COMMAND);
            return;
        }


        String sId = req.getParameter("id");
        logger.debug("Received id => " + sId);
        if (sId == null) {
            String errorMessage = "You didnt chose user";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            resp.sendRedirect(Commands.ERROR_PAGE_COMMAND);
            return;
        }

        int id = Integer.parseInt(sId);

        logger.debug("SESSION => " + session);
        String lang = "en";
        Object o = session.getAttribute("lang");
        if (o != null) {
            lang = o.toString();
        }

        logger.debug("creating weekly report is started");

        try {
            createCashierReport(lang, id);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while creating a report, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            resp.sendRedirect(Commands.ERROR_PAGE_COMMAND);
        }

        logger.debug("getting weekly report and setting attachment");
        getCashierReport(resp, id);

        logger.debug("generateWeeklyReport servlet is finished at the POST method");
    }

    /**
     * GNU Affero General Public License v3.0. Itextpdf copyright.
     * Generates cashier report and save`s into file
     *
     * @param localeName language, on which report will be generated
     * @param id         cashier`s id to report on
     * @throws IOException when file not found
     * @throws DBException when something happened with connection to DB
     */
    private void createCashierReport(String localeName, int id) throws IOException, DBException {
        Document document = new Document();
        try {
            File file = new File(FILE + id + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            BaseFont baseFont = BaseFont.createFont(System.getProperty("catalina.home") +
                    "\\logs\\final-task-reports\\fonts\\times.ttf", "cp1251", BaseFont.EMBEDDED);

            Font catFont = new Font(baseFont, 18, Font.BOLD);
            Font subFont = new Font(baseFont, 16, Font.BOLD);
            Font tableHeaderFont = new Font(baseFont, 10, Font.NORMAL);
            Font smallBold = new Font(baseFont, 8, Font.NORMAL);

            Locale locale;
            if ("ru".equals(localeName)) {
                locale = new Locale("ru", "RU");
            } else {
                locale = new Locale("en", "EN");
            }

            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            createTitle(id, document, catFont, subFont, rb);

            createTable(id, document, smallBold, tableHeaderFont, rb);

            document.close();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
    }

    private void createTitle(int userId, Document document, Font catFont, Font subFont, ResourceBundle rb) throws DocumentException, DBException {
        User user = new UserDAO().getUserForReport(userId);
        String title = rb.getString("cashier.report.name.first") + " " + user.getFirstName() + " " + user.getLastName() + " "
                + rb.getString("cashier.report.name.second");
        Chunk reportName = new Chunk(title, catFont);
        String pattern = "EEEEE d MMMMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, rb.getLocale());
        String creationText = rb.getString("cashier.report.title");
        Chunk creationDate = new Chunk(creationText + ": " + simpleDateFormat.format(new Date()), subFont);

        Paragraph preface = new Paragraph();
        preface.add(reportName);
        addEmptyLine(preface, 1);
        preface.add(creationDate);
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private void createTable(int userId, Document document, Font smallBold, Font tableHeaderFont, ResourceBundle rb) throws DocumentException, DBException {
        User user = new UserDAO().getUserForReport(userId);
        ReceiptDAO receiptDAO = new ReceiptDAO();

        List<Receipt> userReceipts = receiptDAO.getCurrentDayClosedReceiptsForUser(user);
        if (!userReceipts.isEmpty()) {
            Paragraph tableParagraph = new Paragraph();
            tableParagraph.add(new Paragraph(rb.getString("cashier.report.table.title"), tableHeaderFont));
            addEmptyLine(tableParagraph, 1);
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            createReceiptsTable(tableHeaderFont, rb, table);

            double todayEarn = 0d;
            for (Receipt receipt : userReceipts) {
                Map<Product, Integer> receiptsProducts = receiptDAO.getMapOfAmountsAndProductsFromReceipt(receipt);
                if (receiptsProducts.isEmpty()) {
                    continue;
                }
                table.addCell(new Phrase(String.valueOf(receipt.getId()), smallBold));
                String pattern = "MM-dd HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                table.addCell(new Phrase(simpleDateFormat.format(receipt.getCreateTime()), smallBold));
                table.addCell(new Phrase(receipt.getNameRu(), smallBold));
                table.addCell(new Phrase(receipt.getNameEn(), smallBold));
                table.addCell(new Phrase(receipt.getAddressRu(), smallBold));
                table.addCell(new Phrase(receipt.getPhoneNumber(), smallBold));
                if ("ru".equals(rb.getLocale().getLanguage())) {
                    table.addCell(new Phrase(receipt.getDelivery().getNameRu(), smallBold));
                } else {
                    table.addCell(new Phrase(receipt.getDelivery().getNameEn(), smallBold));
                }
                if ("ru".equals(rb.getLocale().getLanguage())) {
                    table.addCell(new Phrase(receipt.getReceiptStatus().getNameRu(), smallBold));
                } else {
                    table.addCell(new Phrase(receipt.getReceiptStatus().getNameEn(), smallBold));
                }

                double price = 0d;
                for (Product product : receiptsProducts.keySet()) {
                    price += product.getPrice().doubleValue() * receiptsProducts.get(product);
                }
                table.addCell(new Phrase(String.valueOf(price), smallBold));
                todayEarn += price;
                table.completeRow();
            }

            Paragraph summary = new Paragraph();
            addEmptyLine(summary, 2);
            summary.add(new Phrase(rb.getString("weekly.report.summary") + ": " + todayEarn, tableHeaderFont));
            addEmptyLine(summary, 2);
            Paragraph copy = new Paragraph("©iTextPDF Copyright", smallBold);
            copy.setAlignment(Element.ALIGN_CENTER);
            summary.add(copy);
            tableParagraph.add(table);
            tableParagraph.add(summary);
            document.add(tableParagraph);
        } else {
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase(rb.getString("cashier.report.no.products"), tableHeaderFont));
            addEmptyLine(paragraph, 2);
            document.add(paragraph);
        }
    }

    private void createReceiptsTable(Font tableHeaderFont, ResourceBundle rb, PdfPTable table) {
        PdfPCell c1 = new PdfPCell(new Phrase("id", tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.creation.date"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.name.ru"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.name.en"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.address"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.phone.number"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.delivery"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.receipt.status"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(rb.getString("weekly.report.table.column.price"), tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
    }

    private void getCashierReport(HttpServletResponse resp, int id) throws IOException {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition", "attachment;filename=" + "UserReport" + id + ".pdf");
        FileInputStream fis = null;
        try {
            File f = new File(FILE + id + ".pdf");
            fis = new FileInputStream(f);
            DataOutputStream os = new DataOutputStream(resp.getOutputStream());
            resp.setHeader("Content-Length", String.valueOf(f.length()));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                fis.close();
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
