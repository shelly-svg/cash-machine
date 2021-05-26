package com.my.web.command.senior_cashier;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.my.db.entities.Role;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;

@WebServlet(urlPatterns = "/generateWeeklyReport")
public class GenerateWeeklyReportServlet extends HttpServlet {

    private static final long serialVersionUID = -6102934823992349102L;
    private static final Logger logger = Logger.getLogger(GenerateWeeklyReportServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("generateWeeklyReport servlet is started at the POST method");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userRole") == null
                || !session.getAttribute("userRole").equals(Role.SENIOR_CASHIER)) {
            String errorMessage = "You are not logged or not have access to the chosen page";
            req.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            resp.sendRedirect("controller?command=noCommand");
        }

        createWeeklyReport();

        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition", "attachment;filename=" + "WeeklyReport.pdf");
        FileInputStream fis = null;
        try {
            File f = new File(System.getProperty("catalina.home") + "\\logs\\final-task-reports\\WeeklyReport.pdf");
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

        logger.debug("generateWeeklyReport servlet is finished at the POST method");
    }

    private void createWeeklyReport() throws IOException {
        Document document = new Document();
        try {
            File file = new File(System.getProperty("catalina.home") + "\\logs\\final-task-reports\\WeeklyReport.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            BaseFont baseFont = BaseFont.createFont("times.ttf", "cp1251", BaseFont.EMBEDDED);
            Chunk chunk = new Chunk("Привет мир! How are you", new Font(baseFont, 16));
            Chunk chunk1 = new Chunk("Current date " + new Date(), new Font(baseFont, 20));
            Paragraph preface = new Paragraph();
            preface.add(new Paragraph(" "));
            preface.add(chunk);
            preface.add(new Paragraph(" "));
            preface.add(chunk1);
            document.add(preface);
            document.close();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
    }

}
