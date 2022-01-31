package com.project.ict502.dataaccess;

import com.project.ict502.connection.Database;
import com.project.ict502.model.Menu;
import com.project.ict502.util.QueryHelper;

import java.sql.ResultSet;

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
}
