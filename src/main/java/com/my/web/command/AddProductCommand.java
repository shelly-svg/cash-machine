package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class AddProductCommand extends Command {

    private static final long serialVersionUID = 2394193249932294933L;
    private static final Logger logger = Logger.getLogger(AddProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = null;
        logger.debug("Add product command is started");
        logger.debug("REQUEST METHOD IS => " + request.getMethod());
        if (request.getMethod().equals("GET")) {
            forward = doGet(request);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }
        logger.debug("Add product command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doGet(HttpServletRequest request)  {
        logger.debug("Add product command started at GET method");
        CategoryDAO categoryDAO = new CategoryDAO();
        Map<Integer, Category> categories = categoryDAO.findAllCategories();
        request.setAttribute("categories", categories);
        return Path.ADD_PRODUCT_PAGE;
    }

    private String doPost(HttpServletRequest request) {
        logger.debug("Add product command started at POST method");
        Product product = new Product();
        String nameRu = request.getParameter("name_ru");
        String nameEn = request.getParameter("name_en");
        String code = request.getParameter("code");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        BigDecimal weight = new BigDecimal(request.getParameter("weight"));
        String descriptionRu = request.getParameter("description_ru");
        String descriptionEn = request.getParameter("description_en");
        int categoryId = new CategoryDAO().findCategoryByName(request.getParameter("category_id"),
                request.getSession().getAttribute("lang").toString()).getId();

        product.setNameRu(nameRu);
        product.setNameEn(nameEn);
        product.setCode(code);
        product.setPrice(price);
        product.setAmount(amount);
        product.setWeight(weight);
        product.setDescriptionRu(descriptionRu);
        product.setDescriptionEn(descriptionEn);
        product.setCategoryId(categoryId);

        logger.trace("Got product => " + product);
        int id = new ProductDAO().addProduct(product);
        logger.debug("Add product command is finished at POST method, forwarding to view product");
        request.getSession().setAttribute("lastAction", "controller?command=viewProduct&id=" + id);
        return "controller?command=viewProduct&id=" + id;
    }

}
