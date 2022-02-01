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
                        sql = "SELECT mainID as id, courseName as name, coursePrice as price, courseDesc as description, coursePic as pic_path FROM MAINCOURSE";
                        break;
                    case "beverage":
                        sql = "SELECT beverageID as id, beverageName as name, beveragePrice as price, beverageDesc as description, beveragePic as pic_path FROM BEVERAGE";
                        break;
                    case "dessert":
                        sql = "SELECT dessertID as id, dessertName as name, dessertPrice as price, dessertDesc as description, dessertPic as pic_path FROM DESSERT";
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

                menus.add(new Menu(id, name, price, description, path, type));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return menus;
    }
}
