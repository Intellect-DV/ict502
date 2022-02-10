package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.util.QueryHelper;

import javax.xml.transform.Result;
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
