package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Customer;
import com.project.ict502.util.QueryHelper;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerDA {
    // check username if existed in database
    public static Customer isUsernameExisted(String username) {
        Customer cust = new Customer();

        try {
            String sql = null;
            if(Database.getDbType().equals("oracle")) {
                sql = "SELECT custid FROM customer WHERE username=?";
            } else {
                sql = "SELECT id FROM customer WHERE username=?";
            }
            ResultSet rs = QueryHelper.getResultSet(sql, new String[]{username});

            if (rs.next()) {
                cust.setValid(true);
            } else {
                cust.setValid(false);
            }

            rs.close();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return cust;
    }

    // add new customer
    public static boolean createCustomer(Customer cust) {
        boolean succeed = false;
        try {
            String sql = null;

            if(Database.getDbType().equals("oracle")) {
                sql = "INSERT INTO customer(username,password,custname,custemail) VALUES (?,?,?,?)";
            } else {
                sql = "INSERT INTO customer(username, password, name, email) VALUES (?,?,?,?)";
            }

            Object[] obj = new Object[] {
                    cust.getCustomerUsername(),
                    cust.getCustomerPassword(),
                    cust.getCustomerName(),
                    cust.getCustomerEmail()
            };

            int rowAffected = QueryHelper.insertUpdateQuery(sql,obj) ;
            if(rowAffected == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
