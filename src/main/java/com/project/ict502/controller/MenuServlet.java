package com.project.ict502.controller;

import com.project.ict502.connection.Database;
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
            case "getmenuinfo":
                getMenuInfo(request, response);
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
            case "updatemenuinfo":
                updateMenuInfo(request, response);
                break;
            case "deletemenu":
                deleteMenu(request, response, applicationPath);
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

    private void getMenuInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String menuIdTemp = request.getParameter("id");
        String type = request.getParameter("type");

        if(menuIdTemp == null || menuIdTemp.equals("") || type == null || type.equals("")) {
            json.put("error","Id or type is empty");
            jsonResponse(response, 400, json);
            return;
        }

        int menuId = -1;
        try {
            menuId = Integer.parseInt(menuIdTemp);
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", "Id must be number");
            jsonResponse(response, 400, json);
        }

        if(menuId == -1) return;

        Menu menu;
        if(Database.getDbType().equals("oracle")) {
            menu = MenuDA.retrieveMenuByIdAndTypeForOracle(menuId, type);
        } else {
            menu = MenuDA.retrieveMenuById(menuId);
        }

        if(menu.getItemId() == -1) {
            json.put("error", "No menu with id provided");
            jsonResponse(response, 400, json);
            return;
        }

        JSONObject menuJson = new JSONObject();
        menuJson.put("menuId", menu.getItemId());
        menuJson.put("menuName", menu.getItemName());
        menuJson.put("menuPrice", menu.getItemPrice());
        menuJson.put("menuDescription", menu.getItemDescription());

        json.put("content", menuJson);

        jsonResponse(response, 200, json);
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
            System.out.println("Login first before create menu");
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

        String host = request.getScheme() + "://" + request.getHeader("host") + "/";
        String imageFileName = part.getSubmittedFileName();
        String urlPathForDB = host + "upload/" + imageFileName;
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

    private void updateMenuInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            json.put("error", "Authorization failed! Please login first.");
            jsonResponse(response, 401, json);
            return;
        }

        Worker worker = (Worker) session.getAttribute("workerObj");

        if(worker.getManagerId() != -1) {
            json.put("error", "Authorization failed! Only manager can update menu.");
            jsonResponse(response, 401, json);
            return;
        }

        String idTemp, name, priceTemp, description, menuType;

        idTemp = request.getParameter("id");
        name = request.getParameter("name");
        priceTemp = request.getParameter("price");
        description = request.getParameter("description");
        menuType = request.getParameter("menu-type");

        if(idTemp == null || name == null || priceTemp == null || description == null || menuType == null
                || idTemp.equals("") || name.equals("") || priceTemp.equals("") || description.equals("") || menuType.equals("")) {
            json.put("error", "Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        int id = -1; double price = -1;
        try {
            id = Integer.parseInt(idTemp);
            price = Double.parseDouble(priceTemp);
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", "Id must be number / Price must be double");
            jsonResponse(response, 400, json);
        }

        if(id == -1 || price == -1) return;

        boolean succeed;

        if(Database.getDbType().equals("oracle")) {
            succeed = MenuDA.updateMenuInfoForOracle(id,name,price,description,menuType);
        } else {
            succeed = MenuDA.updateMenuInfo(id,name,price,description);
        }

        if(succeed) {
            json.put("message", "Menu has been updated");
            json.put("menuId", id);
        } else {
            json.put("error", "Cannot update menu");
        }

        jsonResponse(response, succeed ? 200 : 400, json);
    }

    private void deleteMenu(HttpServletRequest request, HttpServletResponse response, String applicationPath) {
        JSONObject json = new JSONObject();
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("workerObj") == null) {
            json.put("error", "Authorization failed! Please login first.");
            jsonResponse(response, 401, json);
            return;
        }

        Worker worker = (Worker) session.getAttribute("workerObj");

        if(worker.getManagerId() != -1) {
            json.put("error", "Authorization failed! Only manager can delete menu.");
            jsonResponse(response, 401, json);
            return;
        }

        String idTemp = request.getParameter("id");
        String parentIdTemp = request.getParameter("parentId");
        String type = request.getParameter("type");

        if(idTemp == null || idTemp.equals("") || type == null || type.equals("")) {
            json.put("error", "Input empty");
            jsonResponse(response, 400, json);
            return;
        }

        int id = -1, parentId = -1;

        try {
            id = Integer.parseInt(idTemp);

            if(!(parentIdTemp == null || parentIdTemp.equals(""))) {
                parentId = Integer.parseInt(parentIdTemp);
            }
        } catch (Exception err) {
            err.printStackTrace();
            json.put("error", "Id must be number and not null");
            jsonResponse(response, 400, json);
            return;
        }

        if(id == -1) return;

        String picUrl;

        if(Database.getDbType().equals("postgres")) {
            picUrl = MenuDA.retrieveMenuById(id).getItemPicUrl();
        } else {
            picUrl = MenuDA.retrieveMenuByIdAndTypeForOracle(id, type).getItemPicUrl();
        }

        String realPath = applicationPath + "upload" + File.separator + picUrl.split("/")[2];

        boolean succeed;
        if(Database.getDbType().equals("oracle")) {
            succeed = MenuDA.deleteMenuForOracle(parentId, type);
        } else {
            succeed = MenuDA.deleteMenu(id);
        }

        if(succeed) {
            File file = new File(realPath);
            if(file.exists()) {
                file.delete(); // delete file from upload folder
            }

            json.put("message", "The menu has been successfully deleted!");
            jsonResponse(response, 200, json);
            return;
        }

        json.put("error", "Cannot delete menu!");
        jsonResponse(response, 400, json);
    }
}