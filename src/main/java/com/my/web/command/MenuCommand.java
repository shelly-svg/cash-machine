package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MenuCommand extends Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println(request.getSession().getAttribute("lang"));
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.findAllProducts((String) request.getSession().getAttribute("lang"));
        for (Product p : productList) {
            System.out.println(p);
        }
        request.getSession().setAttribute("products", productList);
        return Path.MENU_PAGE;
    }
}
