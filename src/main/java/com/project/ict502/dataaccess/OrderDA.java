package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Order;
import com.project.ict502.util.QueryHelper;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static JSONObject retrieveOrderDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            String sql = "select (select sum(totalprice) from orders where orderstatus != 'uncompleted') as totalsales,(select count(orderid) from orders where orderstatus = 'ongoing') as ongoing,(select count(orderid) from orders where orderstatus = 'complete') as complete";

            if(Database.getDbType().equals("oracle")) {
                sql += " from dual";
            }

            ResultSet rs = QueryHelper.getResultSet(sql);

            if(rs != null && rs.next()) {
                double totalSales = rs.getDouble("totalsales");
                int ongoing = rs.getInt("ongoing");
                int complete = rs.getInt("complete");

                jsonObject.put("total_sales", totalSales);
                jsonObject.put("order_ongoing", ongoing);
                jsonObject.put("order_complete", complete);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONArray retrieveOrders() {
        JSONArray jsonArray = new JSONArray();

        try {
            String sql = "select orderid, orderstatus, orderdate, totalprice, username, custemail from orders odr, customer cs where orderstatus != 'uncompleted' and odr.custid = cs.custid order by orderid desc";

            ResultSet rsOdr = QueryHelper.getResultSet(sql);

            while(rsOdr != null && rsOdr.next()) {
                JSONObject jsonOdr = new JSONObject();

                int orderId = rsOdr.getInt("orderid");
                String orderStatus = rsOdr.getString("orderstatus");
                java.sql.Date orderDate = rsOdr.getDate("orderdate");
                double totalPrice = rsOdr.getDouble("totalprice");
                String custUsername = rsOdr.getString("username");
                String custEmail = rsOdr.getString("custemail");

                jsonOdr.put("order_id", orderId);
                jsonOdr.put("order_status", orderStatus);
                jsonOdr.put("order_date", orderDate);
                jsonOdr.put("order_total_price", totalPrice);
                jsonOdr.put("cust_username", custUsername);
                jsonOdr.put("cust_email", custEmail);

                sql = "select itemname, quantity from cart, menu where cart.itemid = menu.itemid and orderid = ?";

                ResultSet rsCart = QueryHelper.getResultSet(sql, new Integer[] {orderId});
                JSONArray jsonCartArr = new JSONArray();

                while(rsCart != null && rsCart.next()) {
                    JSONObject jsonCart = new JSONObject();

                    String itemName = rsCart.getString("itemname");
                    String itemQuantity = rsCart.getString("quantity");

                    jsonCart.put("item_name", itemName);
                    jsonCart.put("item_quantity", itemQuantity);

                    jsonCartArr.put(jsonCart);
                }

                jsonOdr.put("order_menus", jsonCartArr);

                jsonArray.put(jsonOdr);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return jsonArray;
    }

    public static JSONArray retrieveOrders (int custId) {
        JSONArray jsonArray = new JSONArray();

        try {
            String sql = "select * from orders where orderstatus != 'uncompleted' and custid=? order by orderid desc";

            ResultSet rsOdr = QueryHelper.getResultSet(sql, new Integer[] {custId});

            while(rsOdr != null && rsOdr.next()) {
                JSONObject jsonOdr = new JSONObject();

                int orderId = rsOdr.getInt("orderid");
                String orderStatus = rsOdr.getString("orderstatus");
                java.sql.Date orderDate = rsOdr.getDate("orderdate");
                double totalPrice = rsOdr.getDouble("totalprice");

                jsonOdr.put("order_id", orderId);
                jsonOdr.put("order_status", orderStatus);
                jsonOdr.put("order_date", orderDate);
                jsonOdr.put("order_total_price", totalPrice);

                sql = "select itemname, itempic from cart, menu where cart.itemid = menu.itemid and orderid = ?";

                ResultSet rsCart = QueryHelper.getResultSet(sql, new Integer[] {orderId});
                JSONArray jsonCartArr = new JSONArray();

                while(rsCart != null && rsCart.next()) {
                    JSONObject jsonCart = new JSONObject();

                    String itemName = rsCart.getString("itemname");
                    String itemPic = rsCart.getString("itempic");

                    jsonCart.put("item_name", itemName);
                    jsonCart.put("item_pic", itemPic);

                    jsonCartArr.put(jsonCart);
                }

                jsonOdr.put("order_menus", jsonCartArr);

                jsonArray.put(jsonOdr);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return jsonArray;
    }

    public static Order retrieveUncompletedOrder(int custId) {
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

    public static boolean autoUpdateTotalPrice(int custId) {
        boolean succeed = false;

        try{
            String sql = "select coalesce(sum(itemprice*quantity), 0) as totalprice from cart, menu, orders where orderStatus='uncompleted' and cart.itemid=menu.itemid and cart.orderid=orders.orderid and orders.custid=?";

            ResultSet rs = QueryHelper.getResultSet(sql, new Integer[]{custId});

            if(!rs.next()) {
                return false;
            }

            double totalPrice = rs.getDouble("totalprice");

            sql = "UPDATE orders SET totalprice=? where orderstatus='uncompleted' and custid=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[] {totalPrice, custId});

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return succeed;
    }

    public static boolean updateOrderStatus(int orderId, String orderStatus) {
        boolean succeed = false;

        try {
            String sql = "UPDATE orders SET orderstatus=? WHERE orderid=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[]{orderStatus, orderId});

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static boolean updateDateNow(int orderId) {
        boolean succeed = false;

        try {
            Date currentDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

            String sql = "UPDATE orders set orderdate=? WHERE orderid=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[]{sqlDate, orderId});

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
