package sg.edu.nus.iss.smartpantry.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class ItemDaoImpl implements ItemDao {

    private SqliteHelper dbHelper;

    // Category Table Columns names
    private static final String COL_ID = "itemId";
    private static final String COL_PRODUCT_ID = "productId";
    private static final String COL_EXPIRY_DATE = "ExpiryDate";
    private static final String COL_PRICE = "Price";

    public ItemDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }


    public boolean addItem(Item item)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, item.getItemId());
            values.put(COL_PRODUCT_ID, item.getProductId());
            values.put(COL_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(COL_PRICE, item.getPrice());

            db.insert(dbHelper.TABLE_ITEM, null, values);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }


    public boolean updateItem(Item item)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, item.getItemId());
            values.put(COL_PRODUCT_ID, item.getProductId());
            values.put(COL_EXPIRY_DATE, item.getExpiryDate().toString());
            values.put(COL_PRICE, item.getPrice());

            // updating row
            db.update(dbHelper.TABLE_ITEM, values, COL_ID + " = '" + item.getItemId()+"' and "+COL_PRODUCT_ID + " = '" + item.getProductId()+"'", null);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public boolean deleteItem(Item item)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_ITEM, COL_ID + " = " + item.getItemId()+" and "+COL_PRODUCT_ID + " = " + item.getProductId(), null);
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
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1),Integer.parseInt(cursor.getString(0)));
                item.setExpiryDate(Date.valueOf(cursor.getString(2)));
                item.setPrice(Double.parseDouble(cursor.getString(3)));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }
    // Getting All Items by product id
    public List<Item> getItemsByProductId(String productId) {
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_ITEM + " WHERE "+COL_PRODUCT_ID+" = '"+productId+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1),Integer.parseInt(cursor.getString(0)));
                item.setExpiryDate(Date.valueOf(cursor.getString(2)));
                item.setPrice(Double.parseDouble(cursor.getString(3)));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }
}
