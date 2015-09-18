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
        dbHelper = new SqliteHelper(context);
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
            values.put(dbHelper.COL_ITEM_PRODUCT_NAME, item.getProductName());
            values.put(dbHelper.COL_ITEM_CATEGORY_NAME, item.getCategoryName());
            if (item.getExpiryDate() != null)
                values.put(dbHelper.COL_ITEM_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(dbHelper.COL_ITEM_PRICE, item.getPrice());

            db.insert(dbHelper.TABLE_ITEM, null, values);

            String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper
                    .COL_ITEM_PRODUCT_NAME+" = '"+item.getProductName()+"' AND "+dbHelper
                    .COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String updateQuery = "UPDATE  " + dbHelper.TABLE_PRODUCT + " SET "+dbHelper
                    .COL_PROD_QTY+"="+String.valueOf(cursor.getCount())+" WHERE "+dbHelper
                    .COL_PROD_NAME+" = '"+item.getProductName()+"' AND "+dbHelper
                    .COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'";
            db.execSQL(updateQuery);

            db.close();
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
            values.put(dbHelper.COL_ITEM_PRODUCT_NAME, item.getProductName());
            values.put(dbHelper.COL_ITEM_CATEGORY_NAME, item.getCategoryName());
            if (item.getExpiryDate() != null)
                values.put(dbHelper.COL_ITEM_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(dbHelper.COL_ITEM_PRICE, item.getPrice());

            // updating row
            db.update(dbHelper.TABLE_ITEM, values, dbHelper.COL_ITEM_ID + " = '" + item.getItemId
                            ()+"' and "+dbHelper.COL_ITEM_PRODUCT_NAME + " = '" + item.getProductName()
                            +"' AND "+dbHelper.COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'",
                    null);
            db.close();
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
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_ITEM, dbHelper.COL_ITEM_ID + " = " + item.getItemId()+" and " +
                    ""+dbHelper.COL_ITEM_PRODUCT_NAME + " = '" + item.getProductName()+"' AND " +
                    ""+dbHelper.COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'", null);

            String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper
                    .COL_ITEM_PRODUCT_NAME+" = '"+item.getProductName()+"' AND "+dbHelper
                    .COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String updateQuery = "UPDATE  " + dbHelper.TABLE_PRODUCT + " SET "+dbHelper
                    .COL_PROD_QTY+"="+String.valueOf(cursor.getCount())+" WHERE "+dbHelper
                    .COL_PROD_NAME+" = '"+item.getProductName()+"' AND "+dbHelper
                    .COL_ITEM_CATEGORY_NAME+" = '"+item.getCategoryName()+"'";
            db.execSQL(updateQuery);
            db.close();
            return true;
        }
        catch (Exception e)
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
                Item item = new Item(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_NAME)),Integer.parseInt
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
    public List<Item> getItemsByProductAndCategoryName(String categoryName,String  productName) {
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+dbHelper
                .COL_ITEM_PRODUCT_NAME+" = '"+productName+"' AND "+dbHelper
                .COL_ITEM_CATEGORY_NAME+" = '"+categoryName+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_NAME)),Integer.parseInt
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
    public int generateItemIdForProduct(String productName)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String testQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+ dbHelper
                    .COL_ITEM_PRODUCT_NAME + " = '"+ productName +"'";
            String selectQuery = "SELECT  MAX("+dbHelper.COL_ITEM_ID+") As MaxId FROM " + dbHelper
                    .TABLE_ITEM + " WHERE "+ dbHelper.COL_ITEM_PRODUCT_NAME + " = '"+ productName
                    +"'";

            Cursor cursor = db.rawQuery(testQuery, null);
            cursor.moveToFirst();
            int ctr=cursor.getCount();
            if (cursor.getCount() == 0) {
                db.close();
                return 1;
            }
            else {
                cursor = db.rawQuery(selectQuery, null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex("MaxId")) + 1;
                db.close();
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
                .COL_ITEM_PRODUCT_NAME+" = '"+prod.getProductName()+"' AND "+dbHelper
                .COL_ITEM_CATEGORY_NAME+" = '"+prod.getCategoryName()+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM_PRODUCT_NAME)),Integer.parseInt
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
