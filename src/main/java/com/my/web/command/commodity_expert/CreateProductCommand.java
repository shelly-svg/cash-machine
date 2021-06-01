package com.my.web.command.commodity_expert;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.validators.ProductValidator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateProductCommand extends Command {

    private static final long serialVersionUID = 2394193249932294933L;
    private static final Logger logger = Logger.getLogger(CreateProductCommand.class);
    private final CategoryDAO categoryDAO;
    private final ProductValidator productValidator;
    private final ProductDAO productDAO;

    public CreateProductCommand() {
        categoryDAO = new CategoryDAO();
        productValidator = new ProductValidator();
        productDAO = new ProductDAO();
    }

    public CreateProductCommand(CategoryDAO categoryDAO, ProductValidator productValidator, ProductDAO productDAO) {
        this.categoryDAO = categoryDAO;
        this.productValidator = productValidator;
        this.productDAO = productDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("create product command is started");
        String forward = null;
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
        logger.debug("create product command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("create product command started at GET method");
        HttpSession session = request.getSession();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
        try {
            Map<Integer, Category> categories = categoryDAO.findAllCategories();
            request.setAttribute("categories", categories);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("category.dao.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }
        return Path.ADD_PRODUCT_PAGE;
    }

    private String doPost(HttpServletRequest request) {
        logger.debug("create product command started at POST method");
        Product product = new Product();

        HttpSession session = request.getSession();

        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);

        String nameRu = request.getParameter("name_ru");
        String nameEn = request.getParameter("name_en");
        String code = request.getParameter("code");
        BigDecimal price;
        int amount;
        BigDecimal weight;
        try {
            price = new BigDecimal(request.getParameter("price"));
        } catch (NumberFormatException ex) {
            String errorMessage = rb.getString("add.product.price.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        try {
            amount = Integer.parseInt(request.getParameter("amount"));
        } catch (NumberFormatException ex) {
            String errorMessage = rb.getString("add.product.amount.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        try {
            weight = new BigDecimal(request.getParameter("weight"));
        } catch (NumberFormatException ex) {
            String errorMessage = rb.getString("add.product.weight.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        String descriptionRu = request.getParameter("description_ru");
        String descriptionEn = request.getParameter("description_en");
        int categoryId;
        try {
            categoryId = categoryDAO.findCategoryByName(request.getParameter("category_id"),
                    session.getAttribute("lang").toString()).getId();
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("category.dao.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }
        product.setNameRu(nameRu);
        product.setNameEn(nameEn);
        product.setCode(code);
        product.setPrice(price);
        product.setAmount(amount);
        product.setWeight(weight);
        product.setDescriptionRu(descriptionRu);
        product.setDescriptionEn(descriptionEn);
        try {
            product.setCategory(categoryDAO.findCategoryById(categoryId));
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("category.dao.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }
        logger.trace("Got product => " + product);

        if (!productValidator.validate(product, session, rb)) {
            return Commands.ERROR_PAGE_COMMAND;
        }

        int id;
        try {
            id = productDAO.addProduct(product);
        } catch (ApplicationException ex) {
            String errorMessage = "An error has occurred while adding new product, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + ex.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("create product command is finished at POST method, forwarding to view product");
        session.setAttribute("lastAction", "controller?command=viewProduct&id=" + id);
        return "controller?command=viewProduct&id=" + id;
    }

}
