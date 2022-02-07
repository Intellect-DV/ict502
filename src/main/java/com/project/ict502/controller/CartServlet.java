package com.project.ict502.controller;

import com.project.ict502.model.Customer;
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

    private void addToCart(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        // check customer login info using session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("customerObj") == null) {
            json.put("error", "Please login first!");
            jsonResponse(response, 400, json);
            return;
        }

        // get menu id
        // convert menu id to int
        // check menu id if existed
        // check menu quantity
        // if menu available, add to cart, otherwise no
    }
}
