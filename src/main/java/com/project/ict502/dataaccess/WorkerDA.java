package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Worker;
import com.project.ict502.util.QueryHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class WorkerDA {

    public static Worker isUsernameExisted(String username) {
        Worker worker = new Worker();

        try {
            String sql;
            if(Database.getDbType().equals("oracle")) {
                sql = "SELECT workerid as id FROM worker WHERE username=?";
            } else {
                sql = "SELECT id FROM worker WHERE username=?";
            }

            ResultSet rs = QueryHelper.getResultSet(sql, new String[]{username});

            if(rs.next()) {
                worker.setValid(true);
            } else {
                worker.setValid(false);
            }
            rs.close();

        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return worker;
    }

    public static Worker retrieveWorker(String username, String password) {
        Worker worker = new Worker();

        try {
            // COALESCE = NVL in oracle
            String sql = null;

            if(Database.getDbType().equals("oracle")) {
                sql = "SELECT workerid as id, workername as name, workeremail as email, NVL(to_char(managerid),-1) AS manager_id FROM worker WHERE username=? AND password=?";
            } else {
                sql = "SELECT id, name, email, COALESCE(manager_id,-1) AS manager_id FROM worker WHERE username=? AND password=?";
            }

            ResultSet rs = QueryHelper.getResultSet(sql, new String[] {username, password});

            if(rs.next()) {
                String name, email;
                int id, managerId;
                id = rs.getInt("id");
                name = rs.getString("name");
                email = rs.getString("email");
                managerId = rs.getInt("manager_id");

                worker.setWorker(id,username,name,email,managerId); worker.setValid(true);
            } else {
                worker.setValid(false);
            }
            rs.close();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return worker;
    }

    public static boolean updateWorkerProfile(Worker updateWorker, int id) {
        boolean succeed = false;
        try {
            String sql;

            if(Database.getDbType().equals("oracle")) {
                sql = "UPDATE worker set username=?, workername=?, workeremail=? WHERE workerid=?";
            } else {
                sql = "UPDATE worker set username=?, name=?, email=? WHERE id=?";
            }

            int affectedRow  = QueryHelper.insertUpdateQuery(sql,new Object[]{
                    updateWorker.getWorkerUsername(),
                    updateWorker.getWorkerName(),
                    updateWorker.getWorkerEmail(),
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
