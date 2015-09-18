package sg.edu.nus.iss.smartpantry.dao;

import android.content.Context;

import sg.edu.nus.iss.smartpantry.dao.daoClass.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ShoppingListDao;
import sg.edu.nus.iss.smartpantry.dao.daoImpl.CategoryDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.daoImpl.ItemDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.daoImpl.ProductDaoImpl;
import sg.edu.nus.iss.smartpantry.dao.daoImpl.ShoppingListDaoImpl;

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
        if(itemDao==null){
            itemDao = new ItemDaoImpl(mContext);
        }
        return itemDao;
    }
    public static CategoryDao getCategoryDao(Context mContext){
        if(categoryDao==null){
            categoryDao = new CategoryDaoImpl(mContext);
        }
        return categoryDao;
    }
    public static ProductDao getProductDao(Context mContext){
        if(productDao==null){
            productDao = new ProductDaoImpl(mContext);
        }
        return productDao;
    }
    public static ShoppingListDao getShopLitstDao(Context mContext){
        if(shoppingListDao==null){
            shoppingListDao = new ShoppingListDaoImpl(mContext);
        }
        return shoppingListDao;
    }
}
