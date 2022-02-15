package com.project.ict502.controller;

import com.project.ict502.dataaccess.OrderDA;
import com.project.ict502.model.Customer;
import com.project.ict502.model.Order;
import com.project.ict502.model.Worker;
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
            case "retrieveorderforworker":
                retrieveForWorker(request, response);
                break;
            case "getordersummary":
                getOrderSummary(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action == null) return;

        switch(action.toLowerCase()) {
            case "changestatus":
                changeOrderStatus(request, response);
                break;
        }
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

    private void retrieveForWorker(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            json.put("error", "Please login first!");
            jsonResponse(response, 400, json);
            return;
        }

        JSONArray orders = OrderDA.retrieveOrders();

        if(orders.length() == 0) {
            json.put("message", "No order");
            jsonResponse(response, 200, json);
            return;
        }

        json.put("orders", orders);
        jsonResponse(response, 200, json);
    }

    private void changeOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null){
            json.put("error", "Please login first");
            jsonResponse(response, 400, json);
            return;
        }

        String tempOrderId = request.getParameter("orderId");
        String orderStatus = request.getParameter("orderStatus");

        if(tempOrderId == null || orderStatus == null || tempOrderId.equals("") || orderStatus.equals("")) {
            json.put("error", "Order id / status is empty");
            jsonResponse(response, 400, json);
            return;
        }

        int orderId = -1;

        try {
            orderId = Integer.parseInt(tempOrderId);
        } catch (Exception err) {
            err.printStackTrace();
        }

        if(orderId <= 0) {
            json.put("error", "Order id / status is invalid format");
            jsonResponse(response, 400, json);
            return;
        }

        boolean succeed = OrderDA.updateOrderStatus(orderId, orderStatus);

        if(!succeed) {
            json.put("error", "Cannot update status");
            jsonResponse(response, 400, json);
            return;
        }

        json.put("message", "Order status updated");
        jsonResponse(response, 200, json);
    }

    private void getOrderSummary(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null){
            json.put("error", "Please login first");
            jsonResponse(response, 400, json);
            return;
        }

        JSONObject temp = OrderDA.retrieveOrderDetail();

        if(temp.length() <= 0) {
            json.put("message", "None");
            jsonResponse(response, 400, json);
            return;
        }

        json = temp;
        jsonResponse(response, 200, json);
    }
}
