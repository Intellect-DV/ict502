package com.project.ict502.controller;

import com.project.ict502.dataaccess.WorkerDA;
import com.project.ict502.model.Worker;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "WorkerServlet", value = "/worker")
public class WorkerServlet extends HttpServlet {

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
            case "login":
                loginWorker(request, response);
                break;
            case "updateprofile":
                updateProfile(request, response);
                break;
            case "updatepassword":
                updatePassword(request, response);
                break;
        }
    }

    private void loginWorker(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String username, password;

        username = request.getParameter("username");
        password = request.getParameter("password");

        if(username == null || password == null || username.equals("") || password.equals("")) {
            System.out.println("Input empty");
            json.put("error", "Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        Worker worker = WorkerDA.retrieveWorker(username, password);
        boolean succeed = false;

        if(worker.isValid()) {
            // make session
            HttpSession session = request.getSession();
            session.setAttribute("workerObj", worker);
            session.setMaxInactiveInterval(60*20); // 20 min timeout after inactivity

            System.out.println("Session created");
            json.put("message", "Login success!");
            if(worker.getManagerId() == -1) {
                json.put("url", "/admin/");
            } else {
                json.put("url", "/worker/");
            }
            succeed = true;
            // todo - redirect page
        } else {
            // todo - wrong username / password
            System.out.println("Wrong username or password");
            json.put("error", "Wrong username or password!");
        }

        jsonResponse(response, succeed ? 200 : 400, json);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String username, name, email;

        username = request.getParameter("username");
        name = request.getParameter("name");
        email = request.getParameter("email");

        if(username == null || name == null || email == null || username.equals("") || name.equals("") ||email.equals("")) {
            System.out.println("Input empty");
            json.put("error", "Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        // get manager session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            System.out.println("Please login first before update profile");
            json.put("error", "Authorization failed! Please login first!");
            jsonResponse(response, 401, json);
            return;
        }

        Worker currentWorker = (Worker) session.getAttribute("workerObj");

        // check if username existed
        boolean isUsernameExisted = WorkerDA.isUsernameExisted(username).isValid();
        boolean success = false;

        if(!isUsernameExisted || currentWorker.getWorkerUsername().equals(username)) {
            // update profile
            System.out.println("Update profile accepted");
            Worker tempWorker = new Worker();

            tempWorker.setWorkerUsername(username);
            tempWorker.setWorkerName(name);
            tempWorker.setWorkerEmail(email);

            if (WorkerDA.updateWorkerProfile(tempWorker, currentWorker.getWorkerId())) {
                currentWorker.setWorkerUsername(username);
                currentWorker.setWorkerName(name);
                currentWorker.setWorkerEmail(email);

                session.setAttribute("workerObj", currentWorker);

                json.put("message", "Profile updated");
                success = true;
            } else {
                json.put("error", "Cannot update profile");
            }
        } else {
            System.out.println("Cannot update: username duplicated!");
            json.put("error","Username duplicated");
        }

        jsonResponse(response,  success ? 200 : 403, json);
    }

    private void updatePassword(HttpServletRequest request, HttpServletResponse response){

    }
}
