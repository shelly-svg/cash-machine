package com.my.web.command.senior_cashier;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.Role;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@WebServlet(urlPatterns = "/generateWeeklyReport")
public class GenerateWeeklyReportServlet extends HttpServlet {

    private static final long serialVersionUID = -6102934823992349102L;
    private static final Logger logger = Logger.getLogger(GenerateWeeklyReportServlet.class);
    private static final String FILE = System.getProperty("catalina.home") + "\\logs\\final-task-reports\\WeeklyReport.pdf";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("generateWeeklyReport servlet is started at the POST method");
        HttpSession session = req.getSession(false);
        logger.debug("SESSION => " + session);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userRole") == null
                || !session.getAttribute("userRole").equals(Role.SENIOR_CASHIER)) {
            String errorMessage = "You are not logged or not have access to the chosen page";
            req.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            resp.sendRedirect("controller?command=noCommand");
            logger.debug("DESTROYING SERVLET");
            return;
        }

        logger.debug("SESSION => " + session);
        String lang = "en";
        Object o = session.getAttribute("lang");
        if (o != null) {
            lang = o.toString();
        }
        logger.debug("creating weekly report is started");
        createWeeklyReport(lang);

        logger.debug("getting weekly report and setting attachment");
        getWeeklyReport(resp);

        logger.debug("generateWeeklyReport servlet is finished at the POST method");
    }

    private void createWeeklyReport(String localeName) throws IOException {
        Document document = new Document();
        try {
            File file = new File(FILE);
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

            createTitle(document, catFont, subFont, rb);

            createTable(document, smallBold, tableHeaderFont, rb);


            document.close();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
    }

    private void createTable(Document document, Font smallBold, Font tableHeaderFont, ResourceBundle rb) throws DocumentException {
        Paragraph tableParagraph = new Paragraph();
        tableParagraph.add(new Paragraph(rb.getString("weekly.report.table.title"), tableHeaderFont));
        addEmptyLine(tableParagraph, 1);
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);


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
        ReceiptDAO receiptDAO = new ReceiptDAO();

        double lastWeekIncome = 0d;
        List<Receipt> lastWeekClosedReceipts = receiptDAO.getLastWeekClosedReceipts();
        for (Receipt receipt : lastWeekClosedReceipts) {
            Map<Product, Integer> receiptsProducts = receiptDAO.getMapOfAmountsAndProductsFromReceipt(receipt);
            if (receiptsProducts.isEmpty()){
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
            lastWeekIncome += price;
            table.completeRow();
        }

        Paragraph summary = new Paragraph();
        addEmptyLine(summary, 2);
        summary.add(new Phrase(rb.getString("weekly.report.summary") + ": " + lastWeekIncome, tableHeaderFont));
        tableParagraph.add(table);
        tableParagraph.add(summary);

        document.add(tableParagraph);

    }

    private void createTitle(Document document, Font catFont, Font subFont, ResourceBundle rb) throws DocumentException {
        String name = rb.getString("weekly.report.name");
        Chunk reportName = new Chunk(name, catFont);
        String pattern = "EEEEE d MMMMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, rb.getLocale());
        String creationText = rb.getString("weekly.report.creation.date");
        Chunk creationDate = new Chunk(creationText + ": " + simpleDateFormat.format(new Date()), subFont);

        Paragraph preface = new Paragraph();
        preface.add(reportName);
        addEmptyLine(preface, 1);
        preface.add(creationDate);
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private void getWeeklyReport(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition", "attachment;filename=" + "WeeklyReport.pdf");
        FileInputStream fis = null;
        try {
            File f = new File(FILE);
            fis = new FileInputStream(f);
            DataOutputStream os = new DataOutputStream(resp.getOutputStream());
            resp.setHeader("Content-Length", String.valueOf(f.length()));
            byte[] buffer = new byte[1024];
            int len = 0;
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
