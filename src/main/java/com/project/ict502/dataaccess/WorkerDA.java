package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Worker;
import com.project.ict502.util.QueryHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public static boolean createWorker(Worker worker, int managerId) {
        boolean succeed = false;

        try {
            String sql;

            if(Database.getDbType().equals("oracle")){
                sql = "INSERT INTO worker (username, password, workername, workeremail, managerid) VALUES (?,?,?,?,?)";
            } else {
                sql = "INSERT INTO worker (username, password, name, email, manager_id) VALUES (?,?,?,?,?)";
            }

            Object[] obj = new Object[] {
                    worker.getWorkerUsername(),
                    worker.getWorkerPassword(),
                    worker.getWorkerName(),
                    worker.getWorkerEmail(),
                    managerId
            };

            int rowAffected = QueryHelper.insertUpdateDeleteQuery(sql,obj) ;
            if(rowAffected == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static int getWorkerCount() {
        int count = -1;

        try {
            String sql = "select count(workerid) as totalworker from worker";

            ResultSet rs = QueryHelper.getResultSet(sql);

            if(rs != null && rs.next()) {
                count = rs.getInt("totalworker");
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return count;
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

    public static ArrayList<Worker> retrieveAllWorkerBelowManager(int id) {
        ArrayList<Worker> workers = new ArrayList<>();

        try {
            String sql;

            if(Database.getDbType().equals("oracle")) {
                sql = "SELECT workerid as id, username, workername as name, workeremail as email FROM worker WHERE managerid=? ORDER BY workerid ASC";
            } else {
                sql = "SELECT id, username, name, email FROM worker WHERE manager_id=? ORDER BY id ASC";
            }

            ResultSet rs = QueryHelper.getResultSet(sql,new Integer[]{
                    id
            });

            if(rs != null) {
                while(rs.next()) {
                    Worker temp = new Worker();
                    temp.setWorkerId(rs.getInt("id"));
                    temp.setWorkerUsername(rs.getString("username"));
                    temp.setWorkerName(rs.getString("name"));
                    temp.setWorkerEmail(rs.getString("email"));
                    temp.setManagerId(id);
                    temp.setValid(true);

                    workers.add(temp);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return workers;
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

            int affectedRow  = QueryHelper.insertUpdateDeleteQuery(sql,new Object[]{
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

    public static boolean updateWorkerPassword(String newPassword, int id) {
        boolean succeed = false;
        try{
            String sql;

            if(Database.getDbType().equals("oracle")) {
                sql = "UPDATE worker set password=? WHERE workerid=?";
            } else {
                sql = "UPDATE worker set password=? WHERE id=?";
            }

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[]{
                    newPassword,
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return  succeed;
    }

    public static boolean deleteWorker(int id) {
        boolean succeed = false;
        try{
            String sql;

            if(Database.getDbType().equals("oracle")) {
                sql = "DELETE FROM worker WHERE workerid=?";
            } else {
                sql = "DELETE FROM worker WHERE id=?";
            }

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[] {
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}
