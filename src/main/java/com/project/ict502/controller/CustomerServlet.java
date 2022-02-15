package com.project.ict502.controller;

import com.project.ict502.dataaccess.CustomerDA;
import com.project.ict502.model.Customer;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CustomerServlet", value = "/customer")
public class CustomerServlet extends HttpServlet {
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

        switch (action.toLowerCase()) {
            case "getcustomercount":
                getCustomerCount(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action == null) return;

        switch (action.toLowerCase()) {
            case "signup":
                signup(request, response);
                break;
            case "login":
                login(request, response);
                break;
        }
    }

    private void getCustomerCount(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            json.put("error", "Please login first");
            jsonResponse(response, 400, json);
            return;
        }

        int custCount = -1;

        custCount = CustomerDA.countCustomer();

        if(custCount < 0) {
            json.put("error", "Could not count customer");
            jsonResponse(response, 400, json);
            return;
        }

        json.put("customer_count", custCount);
        jsonResponse(response, 200, json);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        if(request.getSession(false) != null) {
            System.out.println("invalidate session");
            request.getSession().invalidate();
        }
        response.setStatus(200);
        response.getWriter().println(new JSONObject().put("message", "success"));
    }

    private void signup(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        // set data
        String username, password, name, email;
        username = request.getParameter("username");
        password = request.getParameter("password");
        name = request.getParameter("name");
        email = request.getParameter("email");

        if((username == null || password == null || name == null || email == null) ||
                (username.equals("") || password.equals("") || name.equals("") || email.equals("") )){
            System.out.println("Input empty");
            json.put("error", "Input empty");
            jsonResponse(response,400, json);
            return;
        }

        Customer cust = CustomerDA.isUsernameExisted(username);
        boolean succeed = false;

        if(!cust.isValid()) {
            cust.setCustomer(username, password, name, email);

            // add new customer
            if(CustomerDA.createCustomer(cust)) {
                System.out.println("New User added");
                json.put("message", "New user added");
                succeed = true;
            }
        } else {
            System.out.println("Cannot add user: username duplicated");
            json.put("error", "Username duplicated");
        }

        jsonResponse(response,succeed ? 201 : 400, json);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String username, password;
        username = request.getParameter("username");
        password = request.getParameter("password");

        if(username == null || password == null || username.equals("") || password.equals("")) {
            System.out.println("Input empty");
            json.put("error", "input empty");
            jsonResponse(response,400, json);
            return;
        }

        Customer cust = CustomerDA.retrieveCustomer(username, password);
        boolean succeed = false;

        if(cust.isValid()) {
            // make session
            HttpSession session = request.getSession();
            session.setAttribute("customerObj", cust);
            session.setMaxInactiveInterval(60*20); // 20 min timeout after inactivity

            System.out.println("Session created");
            json.put("message", "Login success!");
            json.put("type", "customer");
            succeed = true;
        } else {
            System.out.println("Wrong username or password");
            json.put("error", "Wrong username or password!");
        }

        jsonResponse(response, succeed ? 200 : 400 , json);
    }
}
