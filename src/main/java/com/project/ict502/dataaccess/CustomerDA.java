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
            String sql;
            sql = "SELECT custid FROM customer WHERE username=?";

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
            String sql;

            sql = "INSERT INTO customer(username,password,custname,custemail) VALUES (?,?,?,?)";

            Object[] obj = new Object[] {
                    cust.getCustomerUsername(),
                    cust.getCustomerPassword(),
                    cust.getCustomerName(),
                    cust.getCustomerEmail()
            };

            int rowAffected = QueryHelper.insertUpdateDeleteQuery(sql,obj) ;
            if(rowAffected == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static int countCustomer() {
        int count = -1;

        try {
            String sql = "select count(custid) as totalcustomer from customer";

            ResultSet rs = QueryHelper.getResultSet(sql);

            if(rs != null && rs.next()) {
                count = rs.getInt("totalcustomer");
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return count;
    }

    // retrieve customer by username and passowrd
    public static Customer retrieveCustomer(String username, String password) {
        Customer cust = new Customer();

        try {
            String sql;

            sql = "SELECT custid as id, custname as name, custemail as email FROM customer WHERE username=? AND password=?";

            ResultSet rs = QueryHelper.getResultSet(sql, new String[] {username, password});

            if(rs.next()) {
                String name, email;
                int id;
                id = rs.getInt("id");
                name = rs.getString("name");
                email = rs.getString("email");

                cust.setCustomer(id,username,name,email); cust.setValid(true);
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

    public static boolean updateCustomerProfile(Customer updateCust, int id) {
        boolean succeed = false;
        try {
            String sql;

            sql = "UPDATE customer set username=?, custname=?, custemail=? WHERE custid=?";

            int affectedRow  = QueryHelper.insertUpdateDeleteQuery(sql,new Object[]{
                    updateCust.getCustomerUsername(),
                    updateCust.getCustomerName(),
                    updateCust.getCustomerEmail(),
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
