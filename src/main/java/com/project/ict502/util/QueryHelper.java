package com.project.ict502.util;

import com.project.ict502.connection.Database;

import java.sql.*;

public class QueryHelper {
    // SELECT STATEMENT - return ResultSet from executed query
    /**
     *
     * @param sql Accept full query without prepared statement
     * @return ResultSet after execute the query
     */
    public static ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        Statement stmt;

        try {
            stmt = Database.getConnection().createStatement();
            rs = stmt.executeQuery(sql);

        } catch(SQLException err) {
            err.printStackTrace();
        }
        return rs;
    }

    /**
     *
     * @param sql Accept query with prepared statement; Example: SELECT * FROM username WHERE username=?
     * @param data Accept array of object (could be multiple primitive data type); Example: new Object[] {"string",234}
     * @return ResultSet after execute the query
     */
    public static ResultSet getResultSet(String sql, Object [] data) {
        ResultSet rs = null;

        try {
            rs = getPrepStmt(sql, data).executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return rs;
    }

    /**
     *
     * @param sql Accept INSERT query with prepared statement; Example: INSERT INTO (...) VALUES (?,?,?)
     * @param data Array of Object; Example: new Object[]{"string",234}
     * @return Either -1 for failed, 0 for not updating, > 0 updating 1 or more rows in table
     */
    // INSERT OR UPDATE STATEMENT - return row updated (-1 for failed) (0 for not updating row)
    public static int insertUpdateDeleteQuery(String sql, Object [] data) {
        PreparedStatement prepStmt = null;
        int updatedRow = -1;

        try {
            prepStmt = getPrepStmt(sql, data);
            updatedRow = prepStmt.executeUpdate();
            prepStmt.close();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return updatedRow;
    }

    /**
     *
     * @param sql Accept string; Only use ? in WHERE statement
     *            Example: SELECT * FROM Customer WHERE username=? and password=?
     * @param data Accept array object
     *             Put the parameter here
     *             Example: new Object[] { "String", 99, true }
     * @return PreparedStatement - use for ResultSet
     */
    private static PreparedStatement getPrepStmt(String sql, Object [] data) {
        PreparedStatement prepStmt = null;

        try {
            prepStmt = Database.getConnection().prepareStatement(sql);

            final int SIZE = data.length;
            for(int i = 0; i < SIZE; i++) {
                if(data[i] instanceof String) {
                    prepStmt.setString(i + 1, (String) data[i]);
                } else if (data[i] instanceof Integer) {
                    prepStmt.setInt(i + 1, (Integer) data[i]);
                }else if (data[i] instanceof Double) {
                    prepStmt.setDouble(i + 1, (Double) data[i]);
                } else if (data[i] instanceof Boolean) {
                    prepStmt.setBoolean(i + 1, (Boolean) data[i]);
                } else  if(data[i] instanceof Date) {
                    prepStmt.setDate(i + 1, (Date) data[i]);
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return prepStmt;
    }
}
