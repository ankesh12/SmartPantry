package sg.edu.nus.iss.smartpantry.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class ProductDaoImpl implements ProductDao {

    private SqliteHelper dbHelper;

    // Category Table Columns names
    private static final String COL_ID = "prodId";
    private static final String COL_NAME = "productName";
    private static final String COL_QTY = "Quantity";
    private static final String COL_CATEGORY_ID = "CategoryId";
    private static final String COL_THRESHOLD = "Threshold";
    private static final String COL_IMAGE = "prodImage";
    private static final String COL_BARCODE = "BarCode";

    public ProductDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }


    public boolean addProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, product.getProdId());
            values.put(COL_NAME, product.getProductName());
            values.put(COL_QTY, product.getQuantity());
            values.put(COL_CATEGORY_ID, product.getCategoryId());
            values.put(COL_THRESHOLD, product.getThreshold());
            values.put(COL_IMAGE, product.getProdImage());
            values.put(COL_BARCODE, product.getBarCode());

            db.insert(dbHelper.TABLE_PRODUCT, null, values);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }


    public boolean updateProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, product.getProdId());
            values.put(COL_NAME, product.getProductName());
            values.put(COL_QTY, product.getQuantity());
            values.put(COL_CATEGORY_ID, product.getCategoryId());
            values.put(COL_THRESHOLD, product.getThreshold());
            values.put(COL_IMAGE, product.getProdImage());
            values.put(COL_BARCODE, product.getBarCode());

            // updating row
            db.update(dbHelper.TABLE_PRODUCT, values, COL_ID + " = " + product.getProdId(), null);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public boolean deleteProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_PRODUCT, COL_ID + " = " + product.getProdId(), null);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    // Getting All Products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(3),cursor.getString(0));
                product.setProductName(cursor.getString(1));
                product.setQuantity(Integer.parseInt(cursor.getString(2)));
                product.setThreshold(Integer.parseInt(cursor.getString(4)));
                product.setProdImage(cursor.getBlob(5));
                product.setBarCode(cursor.getString(6));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
    // Getting All Products by category id
    public List<Product> getProductsByCategoryId(String categoryId) {
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT + " WHERE "+COL_CATEGORY_ID+" = '"+categoryId+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(3),cursor.getString(0));
                product.setProductName(cursor.getString(1));
                product.setQuantity(Integer.parseInt(cursor.getString(2)));
                product.setThreshold(Integer.parseInt(cursor.getString(4)));
                product.setProdImage(cursor.getBlob(5));
                product.setBarCode(cursor.getString(6));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
}
