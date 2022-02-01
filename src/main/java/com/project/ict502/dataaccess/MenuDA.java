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

            if(Database.getDbType().equals("postgres")) {
                sql = "INSERT INTO menu(name, price, description, pic_path, type) VALUES (?,?,?,?,?)";
                int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[] {
                        menu.getItemName(),
                        menu.getItemPrice(),
                        menu.getItemDescription(),
                        menu.getItemPicUrl(),
                        menu.getItemType()
                });

                if(affectedRow == 1) succeed = true;
            } else {
                // check type
                String type = menu.getItemType();

                // sql for inserting into menu table
                int affectedRowMenu = QueryHelper.insertUpdateDeleteQuery("INSERT INTO menu VALUES (itemid_seq.nextval, ?)", new String[]{type});

                if(affectedRowMenu != 1) {
                    return false;
                }

                // get item created item id
                ResultSet rs = QueryHelper.getResultSet("SELECT MAX(itemid) as itemid FROM menu");

                if(!rs.next()) {
                    return false;
                }
                int itemId = rs.getInt("itemid");

                // sql for inserting into (maincourse, beverage, dessert)
                switch (type) {
                    case "maincourse":
                        sql = "INSERT INTO maincourse(courseName,coursePrice,courseDesc,coursePic, itemID) VALUES (?,?,?,?,?)";
                        break;
                    case "beverage":
                        sql = "INSERT INTO beverage(beverageName,beveragePrice,beverageDesc,beveragePic, itemID) VALUES (?,?,?,?,?)";
                        break;
                    default:
                        sql = "INSERT INTO dessert(dessertName,dessertPrice,dessertDesc,dessertPic, itemID) VALUES (?,?,?,?,?)";
                        break;
                }

                int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Object[]{
                        menu.getItemName(),
                        menu.getItemPrice(),
                        menu.getItemDescription(),
                        menu.getItemPicUrl(),
                        itemId
                });

                if(affectedRow == 1) succeed = true;
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return succeed;
    }

    public static ArrayList<Menu> retrieveMenus(String type) {
        // retrieve menu
        ArrayList <Menu> menus = new ArrayList<>();
        try {
            String sql;
            ResultSet rs;
            if(Database.getDbType().equals("postgres")) {
                sql = "SELECT * FROM menu WHERE type=?";
                rs = QueryHelper.getResultSet(sql,new String[]{type});
            } else {
                switch (type.toLowerCase()) {
                    case "maincourse":
                        sql = "SELECT mainID as id, courseName as name, coursePrice as price, courseDesc as description, coursePic as pic_path, itemid FROM MAINCOURSE";
                        break;
                    case "beverage":
                        sql = "SELECT beverageID as id, beverageName as name, beveragePrice as price, beverageDesc as description, beveragePic as pic_path, itemid FROM BEVERAGE";
                        break;
                    case "dessert":
                        sql = "SELECT dessertID as id, dessertName as name, dessertPrice as price, dessertDesc as description, dessertPic as pic_path, itemid FROM DESSERT";
                        break;
                    default:
                        sql = null;
                        break;
                }
                if(sql == null) return menus;
                rs = QueryHelper.getResultSet(sql);
            }

            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                String path = rs.getString("pic_path");

                Menu temp = new Menu(id, name, price, description, path, type);
                if(Database.getDbType().equals("oracle")) {
                    temp.setParentId(rs.getInt("itemid"));
                }
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
            String sql = "SELECT id, name, price, description, pic_path FROM menu WHERE id=?";

            ResultSet rs = QueryHelper.getResultSet(sql,new Integer[]{id});

            if(rs.next()) {
                menu.setItemId(rs.getInt("id"));
                menu.setItemPrice(rs.getDouble("price"));
                menu.setItemName(rs.getString("name"));
                menu.setItemDescription(rs.getString("description"));
                menu.setItemPicUrl(rs.getString("pic_path"));

                if(Database.getDbType().equals("oracle")) {
                    menu.setParentId(id);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return menu;
    }

    public static Menu retrieveMenuByIdAndTypeForOracle(int id, String type) {
        Menu menu = new Menu();

        try {
            String sql;

            switch (type.toLowerCase()) {
                case "maincourse":
                    sql = "SELECT mainid as id, coursename as name, courseprice as price, coursedesc as description, coursepic as pic_path, itemid as parentid FROM maincourse WHERE mainid=?";
                    break;
                case "beverage":
                    sql = "SELECT beverageid as id, beveragename as name, beverageprice as price, beveragedesc as description, beveragepic as pic_path, itemid as parentid FROM beverage WHERE beverageid=?";
                    break;
                default:
                    sql = "SELECT dessertid as id, dessertname as name, dessertprice as price, dessertdesc as description, dessertpic as pic_path, itemid as parentid FROM dessert WHERE dessertid=?";
                    break;
            }

            ResultSet rs = QueryHelper.getResultSet(sql,new Integer[]{ id });

            if(rs.next()) {
                menu.setItemId(rs.getInt("id"));
                menu.setItemPrice(rs.getDouble("price"));
                menu.setItemName(rs.getString("name"));
                menu.setItemDescription(rs.getString("description"));
                menu.setItemPicUrl(rs.getString("pic_path"));
                menu.setParentId(rs.getInt("parentid"));

                if(Database.getDbType().equals("oracle")) {
                    menu.setParentId(id);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            Database.closeConnection();
        }

        return menu;
    }

    public static boolean deleteMenu(int id) {
        // delete menu
        boolean succeed = false;

        try {
            String sql = "DELETE FROM menu WHERE id=?";

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{
                    id
            });

            if(affectedRow == 1) succeed = true;
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }

    public static boolean deleteMenuForOracle(int parentId, String type) {
        // delete menu for ORACLE
        boolean succeed = false;

        try {
            String sql;

            switch (type.toLowerCase()) {
                case "maincourse":
                    sql = "DELETE FROM maincourse WHERE itemID=?";
                    break;
                case "beverage":
                    sql = "DELETE FROM beverage WHERE itemID=?";
                    break;
                default:
                    sql = "DELETE FROM dessert WHERE itemID=?";
                    break;
            }

            int affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{
                    parentId
            });

            if(affectedRow == 1) succeed = true;

            if(succeed) {
                succeed = false;

                sql = "DELETE FROM menu WHERE itemID=?";

                affectedRow = QueryHelper.insertUpdateDeleteQuery(sql, new Integer[]{
                        parentId
                });

                if(affectedRow == 1) succeed = true;
            } else {
                succeed = false;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return succeed;
    }
}