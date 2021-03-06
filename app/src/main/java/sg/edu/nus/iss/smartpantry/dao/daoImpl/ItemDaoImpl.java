package sg.edu.nus.iss.smartpantry.dao.daoImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class ItemDaoImpl implements ItemDao {

    private SqliteHelper dbHelper;
    private Context mContext;

    public ItemDaoImpl(Context context)
    {
        dbHelper = SqliteHelper.getInstance(context);
        mContext=context;
    }

    @Override
    public boolean addItem(Item item)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(dbHelper.COL_ITEM_ID, item.getItemId());
            values.put(dbHelper.COL_ITEM_PRODUCT_ID, item.getProduct().getProductId());
            values.put(dbHelper.COL_ITEM_CATEGORY_ID, item.getProduct().getCategory().getCategoryId());
            if (item.getExpiryDate() != null)
                values.put(dbHelper.COL_ITEM_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(dbHelper.COL_ITEM_PRICE, item.getPrice());

            db.insert(dbHelper.TABLE_ITEM, null, values);
            Product product = DAOFactory.getProductDao(mContext).getProductById(item.getProduct().getProductId());
            String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper.COL_ITEM_PRODUCT_ID+" = "+item.getProduct().getProductId() + " AND " + dbHelper.COL_ITEM_CATEGORY_ID+" = "+item.getProduct().getCategory().getCategoryId();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            //Calling ProductDAO for update to Product Table
            product.setQuantity(cursor.getCount());
            DAOFactory.getProductDao(mContext).updateProduct(product);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean updateItem(Item item)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_ITEM_ID, item.getItemId());
            values.put(dbHelper.COL_ITEM_PRODUCT_ID, item.getProduct().getProductId());
            values.put(dbHelper.COL_ITEM_CATEGORY_ID, item.getProduct().getCategory().getCategoryId());
            if (item.getExpiryDate() != null)
                values.put(dbHelper.COL_ITEM_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(dbHelper.COL_ITEM_PRICE, item.getPrice());

            // updating row
            db.update(dbHelper.TABLE_ITEM, values, dbHelper.COL_ITEM_ID + " = " + item.getItemId()+" AND "+dbHelper.COL_ITEM_PRODUCT_ID + " = " + item.getProduct().getProductId()+" AND "+dbHelper.COL_ITEM_CATEGORY_ID + " = " + item.getProduct().getCategory().getCategoryId(),null);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean deleteItem(Item item)
    {
        try
        {

            Product product = DAOFactory.getProductDao(mContext).getProductById(item.getProduct().getProductId());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_ITEM, dbHelper.COL_ITEM_ID + " = " + item.getItemId()+" and " +
                    ""+dbHelper.COL_ITEM_PRODUCT_ID + " = " + item.getProduct().getProductId(), null);

            String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper.COL_ITEM_PRODUCT_ID+" = "+item.getProduct().getProductId() + " AND " + dbHelper.COL_ITEM_CATEGORY_ID+" = "+item.getProduct().getCategory().getCategoryId();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();

            //Calling ProductDAO for update to Product Table
            product.setQuantity(cursor.getCount());
            DAOFactory.getProductDao(mContext).updateProduct(product);

            return true;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }

    // Getting All Items
    @Override
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(DAOFactory.getProductDao(mContext).getProductById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_ID))),Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_ID))));
                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE)) != null)
                    item.setExpiryDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE))));
                item.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRICE))));
                item.setDop(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_DOP))));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }

    @Override
    public List<Item> getItemsByProductId(int productId) {
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper
                .COL_ITEM_PRODUCT_ID+" = "+productId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(DAOFactory.getProductDao(mContext).getProductById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_ID))),Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_ID))));
                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE)) != null)
                    item.setExpiryDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE))));
                item.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRICE))));
                item.setDop(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_DOP))));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }

    @Override
    public List<Item> getItemsByProductAndCategoryName(String categoryName, String productName) {
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper
                .COL_ITEM_PRODUCT_ID+" = "+DAOFactory.getProductDao(mContext).getProductByCategoryNameAndProdName(categoryName,productName).getProductId();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(DAOFactory.getProductDao(mContext).getProductById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_ID))),Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_ID))));
                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE)) != null)
                    item.setExpiryDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE))));
                item.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRICE))));
                item.setDop(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_DOP))));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }

    @Override
    public int generateItemIdForProduct(int productId)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String testQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+ dbHelper
                    .COL_ITEM_PRODUCT_ID + " = "+ productId;
            String selectQuery = "SELECT  MAX("+dbHelper.COL_ITEM_ID+") As MaxId FROM " + dbHelper
                    .TABLE_ITEM + " WHERE "+ dbHelper.COL_ITEM_PRODUCT_ID + " = "+ productId;

            Cursor cursor = db.rawQuery(testQuery, null);
            cursor.moveToFirst();
            int ctr=cursor.getCount();
            if (cursor.getCount() == 0) {
                return 1;
            }
            else {
                cursor = db.rawQuery(selectQuery, null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex("MaxId")) + 1;
                return id;
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Item> getItemsNearingExpiryByProduct(Product prod) {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT * FROM "+dbHelper.TABLE_ITEM+" WHERE ("+dbHelper
                .COL_ITEM_EXPIRY_DATE+" BETWEEN CURRENT_DATE AND date(CURRENT_DATE,'+7 day') OR " +
                ""+dbHelper.COL_ITEM_EXPIRY_DATE+" < CURRENT_DATE) AND "+dbHelper
                .COL_ITEM_PRODUCT_ID+" = "+prod.getProductId();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(DAOFactory.getProductDao(mContext).getProductById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_ID))), Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_ID))));
                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE)) != null)
                    item.setExpiryDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_EXPIRY_DATE))));
                item.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRICE))));
                item.setDop(Date.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_DOP))));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }
}
