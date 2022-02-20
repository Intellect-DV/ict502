package com.project.ict502.controller;

import com.project.ict502.dataaccess.OrderDA;
import com.project.ict502.dataaccess.PaymentDA;
import com.project.ict502.model.Customer;
import com.project.ict502.model.Order;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PaymentServlet", value = "/payment")
public class PaymentServlet extends HttpServlet {
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

        switch(action.toLowerCase()) {
            case "makepayment":
                makePayment(request, response);
                break;
        }
    }

    private static void makePayment(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("customerObj") == null) {
            json.put("error", "Please login first!");
            jsonResponse(response, 400, json);
            return;
        }

        Customer currentCustomer = (Customer) session.getAttribute("customerObj");

        Order currentOrder = OrderDA.retrieveUncompletedOrder(currentCustomer.getCustomerId());

        if(currentOrder == null) {
            json.put("error", "There is no order");
            jsonResponse(response, 400, json);
            return;
        }

        boolean succeed = PaymentDA.createPayment(currentOrder.getOrderId());

        if(!succeed) {
            json.put("error", "Could not create payment");
            jsonResponse(response, 400, json);
            return;
        }

        succeed = OrderDA.updateOrderStatus(currentOrder.getOrderId(), "preparing");

        if(!succeed) {
            json.put("error", "Could not update order status");
            jsonResponse(response, 400, json);
            return;
        }

        succeed = OrderDA.updateDateNow(currentOrder.getOrderId());

        if(!succeed) {
            json.put("error", "Could not update order date");
            jsonResponse(response, 400, json);
            return;
        }

        json.put("message", "Payment created");
        jsonResponse(response, 200, json);
    }
}
