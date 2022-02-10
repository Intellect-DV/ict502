package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Order;
import com.project.ict502.util.QueryHelper;

import java.sql.ResultSet;
import java.util.Date;

public abstract class OrderDA {
    public static boolean createOrder(int custId) {
        boolean succeed = false;

        try {
            Date currentDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

            String sql = "INSERT INTO ORDERS(orderstatus,orderdate,totalprice, custid) VALUES (?,?,?,?)";
            Order temp = new Order("uncompleted", sqlDate, 0, custId);

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql,new Object[]{
                    temp.getOrderStatus(),
                    temp.getOrderDate(),
                    temp.getTotalPrice(),
                    temp.getCustomerId()
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err){
            err.printStackTrace();
        }

        return succeed;
    }

    public static Order retrieveOrder(int custId) {
        Order temp = null;

        try{
            String sql;
            sql = "SELECT orderId, orderStatus, orderDate, totalPrice, custId FROM ORDERS WHERE custId=?";

            ResultSet rs = QueryHelper.getResultSet(sql, new Integer[]{custId});

            if(rs.next()){
                int orderId = rs.getInt("orderId");
                String orderStatus = rs.getString("orderStatus");
                Date orderDate = rs.getDate("orderDate");
                double totalPrice = rs.getDouble("totalPrice");

                temp = new Order(orderId ,orderStatus, orderDate, totalPrice, custId);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return temp;
    }

    public static Order retrieveUncompleteOrder(int custId) {
        Order temp = null;

        try{
            String sql;
            sql = "SELECT orderId, orderStatus, orderDate, totalPrice, custId FROM ORDERS WHERE custId=? and orderStatus='uncompleted'";

            ResultSet rs = QueryHelper.getResultSet(sql, new Integer[]{custId});

            if(rs.next()){
                int orderId = rs.getInt("orderId");
                String orderStatus = rs.getString("orderStatus");
                Date orderDate = rs.getDate("orderDate");
                double totalPrice = rs.getDouble("totalPrice");

                temp = new Order(orderId ,orderStatus, orderDate, totalPrice, custId);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return temp;
    }
}
