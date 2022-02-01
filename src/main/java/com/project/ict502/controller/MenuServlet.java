package com.project.ict502.controller;

import com.project.ict502.dataaccess.MenuDA;
import com.project.ict502.model.Menu;
import com.project.ict502.model.Worker;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet(name = "MenuServlet", value = "/menu")
@MultipartConfig(
            fileSizeThreshold = 1024 * 1024 * 2, // 2MB
            maxFileSize = 1024 * 1024 * 10, // 10MB
            maxRequestSize = 1024 * 1024 * 50
        )
public class MenuServlet extends HttpServlet {
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
            case "getmenus":
                getMenus(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String applicationPath = getServletContext().getRealPath("");

        if(action == null) return;

        switch(action.toLowerCase()) {
            case "createmenu":
                createMenu(request, response, applicationPath);
                break;
        }
    }

    private void getMenus(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        JSONObject json = new JSONObject();
        if(type == null || type.equals("")) {
            json.put("error", "Specify type of menu");
            jsonResponse(response, 400, json);
            return;
        }

        ArrayList<Menu> menus = MenuDA.retrieveMenus(type);

        response.setContentType("application/json");

        if(menus.size() == 0) {
            json.put("error", "None of menu type");
            jsonResponse(response, 400, json);
            return;
        }
        request.setAttribute("menus", menus);

        try {
            response.setStatus(200);
            response.getWriter().println(new JSONArray(menus));
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void createMenu(HttpServletRequest request, HttpServletResponse response, String applicationPath) {
        JSONObject json = new JSONObject();

        Part part;
        String menuName, menuPriceTemp, menuDescription, menuType;

        menuName = request.getParameter("name");
        menuPriceTemp = request.getParameter("price");
        menuDescription = request.getParameter("description");
        menuType = request.getParameter("menu-type");

        if(menuName == null || menuPriceTemp == null || menuDescription == null || menuType == null ||
                menuName.equals("") || menuPriceTemp.equals("") || menuDescription.equals("") || menuType.equals("")) {
            json.put("error", "Input is empty");
            jsonResponse(response, 401, json);
            return;
        }

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            System.out.println("Login first before add worker");
            json.put("error", "Authorization failed! Please login first!");
            jsonResponse(response, 401, json);
            return;
        }
        Worker manager = (Worker) session.getAttribute("workerObj");

        if(manager.getManagerId() != -1) {
            System.out.println("Only manager can create menu");
            json.put("error","Only manager can create menu");
            jsonResponse(response, 401, json);
            return;
        }

        double menuPrice = -1;
        try {
            menuPrice = Double.parseDouble(menuPriceTemp);
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", "Price must a number and not null");
            jsonResponse(response, 400, json);
            return;
        }

        try {
            part = request.getPart("image");
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", err.getMessage());
            jsonResponse(response, 400, json);
            return;
        }

        if(part == null) {
            json.put("error", "Image file is null");
        }

        String imageFileName = part.getSubmittedFileName();
        String urlPathForDB = "/upload/" + imageFileName;
        String savePath = applicationPath + "upload" + File.separator + imageFileName;

        if(!new File(applicationPath + "upload").exists()) {
            boolean created  = new File(applicationPath + "upload").mkdir();
            if(!created) {
                System.out.println("Could not create folder: " + applicationPath + "upload");
            }
        }

        try {
            part.write(savePath);
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", "Could not upload image file!");
            jsonResponse(response, 400, json);
            return;
        }

        Menu menu = new Menu(menuName, menuPrice, menuDescription, urlPathForDB, menuType);

        boolean succeed = MenuDA.createMenu(menu);

        if(succeed) {
            json.put("message", "Menu has been created!");
            jsonResponse(response, 200, json);
            return;
        }

        json.put("error", "Could not create menu.");
        jsonResponse(response, 400, json);
    }
}
