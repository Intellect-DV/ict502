package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.util.QueryHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public abstract class CartDA {
    private static boolean createCart(int itemId, int orderId) {
        boolean succeed = false;

        try {
            String sql = "INSERT INTO cart VALUES (?,?,?)";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{itemId, orderId, 0});

            if (affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static boolean addToCart(int itemId, int orderId) {
        boolean succeed = false;

        try{
            int currentQuantity = retrieveCurrentQuantity(itemId, orderId);

            if(currentQuantity == -1) {
                createCart(itemId, orderId);
                currentQuantity = retrieveCurrentQuantity(itemId, orderId);
            }

            succeed = updateCart(itemId, orderId, currentQuantity+1);
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static JSONArray retrieveCartMenu(int orderId) {
        JSONArray arr = new JSONArray();

        try {
            String sql = "select cart.itemid as itemid, itemname, itemprice, quantity, (itemprice*quantity) as totalprice, itempic from cart, menu where cart.itemid = menu.itemid and orderid=?";

            ResultSet rs = QueryHelper.getResultSet(sql, new Integer[]{orderId});

            while(rs.next()) {
                JSONObject json = new JSONObject();

                int itemId = rs.getInt("itemid");
                String itemName = rs.getString("itemname");
                double itemPrice = rs.getDouble("itemprice");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("totalprice");
                String url = rs.getString("itempic");

                json.put("menu_id", itemId);
                json.put("menu_name", itemName);
                json.put("menu_price", itemPrice);
                json.put("menu_quantity", quantity);
                json.put("total_price", totalPrice);
                json.put("image_url", url);

                arr.put(json);
            }
        } catch(Exception err) {
            err.printStackTrace();
        }

        if(arr.length() == 0) {
            return null;
        } else {
            return arr;
        }
    }

    public static int retrieveCurrentQuantity(int itemId, int orderId) {
        try {
            String sql = "SELECT quantity FROM cart WHERE itemid=? AND orderid=?";

            ResultSet rs = QueryHelper.getResultSet(sql, new Integer[]{itemId,orderId});

            if(rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return -1;
    }

    private static boolean updateCart(int itemId, int orderId, int quantity) {
        boolean succeed = false;

        try {
            String sql =  "UPDATE cart set quantity=? WHERE itemId=? AND orderId=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{quantity, itemId, orderId});

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
