package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;

import sg.edu.nus.iss.smartpantry.dao.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;
import sg.edu.nus.iss.smartpantry.dao.ShoppingListDao;
import sg.edu.nus.iss.smartpantry.dao.sqlite.CategoryDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.sqlite.ItemDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.sqlite.ProductDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.sqlite.ShoppingListDaoImpl;

/**
 * Created by A0134493A on 14/5/2015.
 */
public class DAOFactory {
    private static ItemDao itemDao=null;
    private static CategoryDao categoryDao = null;
    private static ProductDao productDao = null;
    private static ShoppingListDao shoppingListDao = null;

    private static DAOFactory singInstance =  new DAOFactory();

    public static DAOFactory getInstance(){
        return singInstance;
    }

    public static ItemDao getItemDao(Context mContext){
        System.out.println("inside");
        if(itemDao==null){
            itemDao = new ItemDaoImpl(mContext);
        }
        return itemDao;
    }
    public static CategoryDao getCategoryDao(Context mContext){
        System.out.println("inside");
        if(categoryDao==null){
            categoryDao = new CategoryDaoImpl(mContext);
        }
        return categoryDao;
    }
    public static ProductDao getProductDao(Context mContext){
        System.out.println("inside");
        if(productDao==null){
            productDao = new ProductDaoImpl(mContext);
        }
        return productDao;
    }
    public static ShoppingListDao getShopLitstDao(Context mContext){
        System.out.println("inside");
        if(shoppingListDao==null){
            shoppingListDao = new ShoppingListDaoImpl(mContext);
        }
        return shoppingListDao;
    }
}
