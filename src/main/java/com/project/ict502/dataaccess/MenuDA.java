package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Menu;
import com.project.ict502.util.QueryHelper;

import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class MenuDA {
    public static boolean createMenu(Menu menu) {
        // create menu
        boolean succeed = false;
        try {
            String sql;

            sql = "INSERT INTO menu(itemname,itemprice,itemdesc,itempic,itemtype) VALUES (?,?,?,?,?)";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[] {
                    menu.getItemName(),
                    menu.getItemPrice(),
                    menu.getItemDescription(),
                    menu.getItemPicUrl(),
                    menu.getItemType()
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return succeed;
    }

    public static ArrayList<Menu> retrieveAllMenus() {
        ArrayList <Menu> menus = new ArrayList<>();
        try {
            String sql;

            sql = "SELECT itemid as id, itemname as name, itemprice as price, itemdesc as description, itempic as pic_path, itemtype as type FROM menu order by itemtype";

            ResultSet rs = QueryHelper.getResultSet(sql);

            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                String path = rs.getString("pic_path");
                String type = rs.getString("type");

                Menu temp = new Menu(id, name, price, description, path, type);

                menus.add(temp);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return menus;
    }

    public static ArrayList<Menu> retrieveMenus(String type) {
        // retrieve menu
        ArrayList <Menu> menus = new ArrayList<>();
        try {
            String sql;
            ResultSet rs;

            sql = "SELECT itemid as id, itemname as name, itemprice as price, itemdesc as description, itempic as pic_path FROM menu WHERE itemtype=?";

            rs = QueryHelper.getResultSet(sql,new String[]{type});

            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                String path = rs.getString("pic_path");

                Menu temp = new Menu(id, name, price, description, path, type);

                menus.add(temp);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return menus;
    }

    public static Menu retrieveMenuById(int id) {
        Menu menu = new Menu();

        try {
            String sql = "SELECT itemid as id, itemname as name, itemprice as price, itemdesc as description, itempic as pic_path FROM menu WHERE itemid=?";

            ResultSet rs = QueryHelper.getResultSet(sql,new Integer[]{id});

            if(rs.next()) {
                menu.setItemId(rs.getInt("id"));
                menu.setItemPrice(rs.getDouble("price"));
                menu.setItemName(rs.getString("name"));
                menu.setItemDescription(rs.getString("description"));
                menu.setItemPicUrl(rs.getString("pic_path"));
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return menu;
    }

    public static boolean updateMenuInfo(int id, String name, double price, String description) {
        // update menu
        boolean succeed = false;

        try {
            String sql = "UPDATE menu SET itemname=?, itemprice=?, itemdesc=? WHERE itemid=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql,new Object[] {
                    name, price, description, id
            });

            if (affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static boolean deleteMenu(int id) {
        // delete menu
        boolean succeed = false;

        try {
            String sql = "DELETE FROM menu WHERE itemid=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static int countMenu() {
        int count = -1;

        try {
            String sql = "select count(itemid) as totalmenu from menu";

            ResultSet rs = QueryHelper.getResultSet(sql);

            if(rs != null && rs.next()) {
                count = rs.getInt("totalmenu");
            }
        } catch(Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return count;
    }
}