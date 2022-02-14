package com.project.ict502.controller;

import com.project.ict502.dataaccess.OrderDA;
import com.project.ict502.model.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {
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

        if(action == null) return;

        switch(action.toLowerCase()) {
            case "retrieveorderforcust":
                retrieveForCust(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void retrieveForCust(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("customerObj") == null) {
            json.put("error", "Please login first");
            jsonResponse(response, 400, json);
            return;
        }

        Customer currentCust = (Customer) session.getAttribute("customerObj");

        JSONArray jsonArray = OrderDA.retrieveOrders(currentCust.getCustomerId());

        if(jsonArray.length() == 0) {
            json.put("message", "No order");
            jsonResponse(response, 200, json);
            return;
        }

        json.put("orders", jsonArray);
        jsonResponse(response, 200, json);
    }
}
