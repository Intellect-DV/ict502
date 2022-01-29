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
                // todo - message , etc
                System.out.println("New User added");
                json.put("message", "New user added");
                succeed = true;
            }
        } else {
            // todo - (use another username)
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
            succeed = true;
            // todo - redirect page
        } else {
            // todo - wrong username / password
            System.out.println("Wrong username or password");
            json.put("error", "Wrong username or password!");
        }

        jsonResponse(response, succeed ? 200 : 400 , json);
    }
}
