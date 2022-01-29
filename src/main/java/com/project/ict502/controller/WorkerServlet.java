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
            case "add":
                addWorker(request,response);
                break;
            case "delete":
                deleteWorker(request, response);
                break;
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

    private void addWorker(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        System.out.println("addWorker");
        String username, password, name ,email; int managerId;

        username = request.getParameter("username");
        password = request.getParameter("password");
        name = request.getParameter("name");
        email = request.getParameter("email");

        if((username == null || password == null || name == null || email == null)
                || (username.equals("") || password.equals("") || name.equals("") || email.equals("")) ) {
            System.out.println("Input empty");
            json.put("error", "Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        // get manager session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            System.out.println("Login first before add worker");
            json.put("error", "Authorization failed! Please login first!");
            jsonResponse(response, 401, json);
            return;
        }
        Worker manager = (Worker) session.getAttribute("workerObj");
        managerId = manager.getWorkerId();

        // check existed username
        Worker worker = WorkerDA.isUsernameExisted(username);
        boolean succeed = false;

        if(!worker.isValid()) {
            // add worker into database
            worker.setWorker(username,password,name,email);

            if(WorkerDA.createWorker(worker,managerId)) {
                System.out.println("New worker added");
                json.put("message", "New worker added");
                succeed = true;
            }
        } else {
            System.out.println("Cannot add: username duplicated");
            json.put("error", "Username duplicated");
        }

        jsonResponse(response, succeed ? 201 : 400, json);
    }

    private void deleteWorker(HttpServletRequest request, HttpServletResponse response)  {
        JSONObject json = new JSONObject();
        int workerId;
        String tempWorkerId = request.getParameter("workerId");

        if(tempWorkerId == null || tempWorkerId.equals("")) {
            json.put("error","Parameter for worker id is empty");
            jsonResponse(response,400,json);
            return;
        }

        try {
            workerId = Integer.parseInt(tempWorkerId);
        } catch (NumberFormatException err) {
            err.printStackTrace();
            json.put("error","Worker id must be number");
            json.put("given", tempWorkerId);
            jsonResponse(response,406,json);
            return;
        }

        // check session
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            System.out.println("Please login first before update profile");
            json.put("error", "Authorization failed! Please login first!");
            jsonResponse(response, 401, json);
            return;
        }

        Worker currentWorker = (Worker) session.getAttribute("workerObj");

        if(currentWorker.getManagerId() != -1) {
            System.out.println("Only manager can delete worker");
            json.put("error","Only manager can delete worker");
            jsonResponse(response, 401, json);
            return;
        }

        boolean succeed = false;
        if(WorkerDA.deleteWorker(workerId)) {
            json.put("message", "Worker has been deleted");
            succeed = true;
        } else {
            json.put("error", "Worker id not exist");
            json.put("workerId", workerId);
        }

        jsonResponse(response, succeed ? 200 : 400, json);
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
                json.put("url", "/Admin/");
            } else {
                json.put("url", "/Worker/");
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
        JSONObject json = new JSONObject();
        String currentPassword, newPassword;

        currentPassword = request.getParameter("current-password");
        newPassword = request.getParameter("new-password");

        if(currentPassword == null || newPassword == null || currentPassword.equals("") || newPassword.equals("")) {
            System.out.println("Input empty");
            json.put("error","Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        // check session
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("workerObj") == null) {
            System.out.println("Please login first before update profile");
            json.put("error", "Authorization failed! Please login first!");
            jsonResponse(response, 401, json);
            return;
        }

        // check current password same or not
        Worker currentWorker = (Worker) session.getAttribute("workerObj");
        boolean proceed = WorkerDA.retrieveWorker(currentWorker.getWorkerUsername(), currentPassword).isValid();
        boolean succeed = false;

        // update new password
        if(proceed) {
            if(WorkerDA.updateWorkerPassword(newPassword, currentWorker.getWorkerId())) {
                json.put("message","Password updated");
                succeed = true;
            } else {
                json.put("error", "Could not update password!");
            }
        } else {
            json.put("error", "Current password is wrong");
        }

        jsonResponse(response, succeed ? 200 : 400, json);
    }
}
