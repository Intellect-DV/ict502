package com.project.ict502.controller;

import com.project.ict502.dataaccess.CartDA;
import com.project.ict502.dataaccess.MenuDA;
import com.project.ict502.dataaccess.OrderDA;
import com.project.ict502.model.Cart;
import com.project.ict502.model.Customer;
import com.project.ict502.model.Menu;
import com.project.ict502.model.Order;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {
    private static void jsonResponse(HttpServletResponse response, int statusCode, JSONObject json) {
        try {
            response.setContentType("application/json");
            response.setStatus(statusCode);
            response.getWriter().println(json);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action == null || action.equals("")) return;

        switch (action.toLowerCase()) {
            case "retrieveforcust":
                retrieveForCust(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action == null || action.equals("")) return;

        switch(action.toLowerCase()) {
            case "add":
                addToCart(request, response);
                break;
            case "":
                break;
        }
    }

    private void retrieveForCust(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        // check customer login info using session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("customerObj") == null) {
            json.put("error", "Please login first!");
            jsonResponse(response, 400, json);
            return;
        }

        Customer currentCustomer = (Customer) session.getAttribute("customerObj");

        // get order -- if not exist, response none exist, if existed, search for the cart

        // if the cart is none, response none also
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        // check customer login info using session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("customerObj") == null) {
            json.put("error", "Please login first!");
            jsonResponse(response, 400, json);
            return;
        }

        Customer currentCustomer = (Customer) session.getAttribute("customerObj");

        // get menu id
        String menuIdTemp = request.getParameter("menuId");

        if(menuIdTemp == null || menuIdTemp.equals("")) {
            json.put("error", "Please provide menu id");
            jsonResponse(response, 400,  json);
            return;
        }

        // convert menu id to int
        int menuId = -1;
        try {
            menuId = Integer.parseInt(menuIdTemp);
        } catch (Exception err) {
            err.printStackTrace();
        }

        if(menuId <= 0) {
            json.put("error", "Menu id is invalid format");
            jsonResponse(response, 400, json);
            return;
        }

        // check menu id if existed
        Menu currentMenu = MenuDA.retrieveMenuById(menuId);
        if(currentMenu.getItemId() == -1) {
            json.put("error", "Menu id is not exist");
            jsonResponse(response, 400, json);
            return;
        }

        // Check uncompleted order, if existed, use the same id, if not create new
        Order currentOrder = OrderDA.retrieveUncompleteOrder(currentCustomer.getCustomerId());
        if(currentOrder == null) {
            OrderDA.createOrder(currentCustomer.getCustomerId());
            currentOrder = OrderDA.retrieveUncompleteOrder(currentCustomer.getCustomerId());
        }

        if(CartDA.addToCart(menuId,currentOrder.getOrderId())) {
            json.put("message", "Added to Cart");
            int  currentQuantity = CartDA.retrieveCurrentQuantity(menuId,currentOrder.getOrderId());

            Cart cartTemp = new Cart(menuId,currentOrder.getOrderId(), currentQuantity);
            JSONObject arr = new JSONObject(cartTemp);

            json.put("cart", arr);
            jsonResponse(response, 200, json);
        } else{
            json.put("error","Cannot add to cart");
            jsonResponse(response, 400, json);
        }
    }
}
