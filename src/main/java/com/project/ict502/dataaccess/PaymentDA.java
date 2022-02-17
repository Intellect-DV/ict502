package com.project.ict502.dataaccess;

import com.project.ict502.util.QueryHelper;

import java.util.Date;

public abstract class PaymentDA {
    public static boolean createPayment(int orderId) {
        boolean succeed = false;

        try {
            Date currentDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

            String sql = "INSERT INTO payment(paymentdate,paymentstatus,orderid) VALUES (?,?,?)";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[]{sqlDate, "paid", orderId});

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return  succeed;
    }
}
