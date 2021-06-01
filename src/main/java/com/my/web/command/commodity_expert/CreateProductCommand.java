package com.my.web.command.commodity_expert;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.validators.ProductValidator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
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

    private String doGet(HttpServletRequest request) throws ApplicationException {
        logger.debug("create product command started at GET method");

        try {
            Map<Integer, Category> categories = categoryDAO.findAllCategories();
            request.setAttribute("categories", categories);
        } catch (DBException exception) {
            String errorMessage = "category.dao.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        return Path.ADD_PRODUCT_PAGE;
    }

    private String doPost(HttpServletRequest request) throws ApplicationException {
        logger.debug("create product command started at POST method");

        Product product = new Product();
        HttpSession session = request.getSession();

        String nameRu = request.getParameter("name_ru");
        String nameEn = request.getParameter("name_en");
        String code = request.getParameter("code");
        BigDecimal price;
        int amount;
        BigDecimal weight;
        try {
            price = new BigDecimal(request.getParameter("price"));
        } catch (NumberFormatException ex) {
            String errorMessage = "add.product.price.invalid";
            logger.error("errorMessage --> price invalid");
            throw new ApplicationException(errorMessage);
        }

        try {
            amount = Integer.parseInt(request.getParameter("amount"));
        } catch (NumberFormatException ex) {
            String errorMessage = "add.product.amount.invalid";
            logger.error("errorMessage --> amount invalid");
            throw new ApplicationException(errorMessage);
        }

        try {
            weight = new BigDecimal(request.getParameter("weight"));
        } catch (NumberFormatException ex) {
            String errorMessage = "add.product.weight.invalid";
            logger.error("errorMessage --> weight invalid");
            throw new ApplicationException(errorMessage);
        }

        String descriptionRu = request.getParameter("description_ru");
        String descriptionEn = request.getParameter("description_en");

        int categoryId;
        try {
            categoryId = categoryDAO.findCategoryByName(request.getParameter("category_id"),
                    session.getAttribute("lang").toString()).getId();
        } catch (DBException exception) {
            String errorMessage = "category.dao.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
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
        } catch (DBException exception) {
            String errorMessage = "category.dao.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.trace("Got product => " + product);

        productValidator.validate(product);

        int id;
        try {
            id = productDAO.addProduct(product);
        } catch (DBException exception) {
            String errorMessage = "product.dao.add.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("create product command is finished at POST method, forwarding to view product");
        session.setAttribute("lastAction", "controller?command=viewProduct&id=" + id);
        return "controller?command=viewProduct&id=" + id;
    }

}
