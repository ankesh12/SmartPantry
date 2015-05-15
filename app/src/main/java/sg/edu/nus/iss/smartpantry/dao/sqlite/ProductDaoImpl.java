package sg.edu.nus.iss.smartpantry.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
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
    private static final String COL_NAME = "ProductName";
    private static final String COL_QTY = "Quantity";
    private static final String COL_CATEGORY_NAME = "CategoryName";
    private static final String COL_THRESHOLD = "Threshold";
    private static final String COL_IMAGE = "ProdImage";
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
            values.put(COL_NAME, product.getProductName());
            values.put(COL_QTY, product.getQuantity());
            values.put(COL_CATEGORY_NAME, product.getCategoryName());
            values.put(COL_THRESHOLD, product.getThreshold());
            if (product.getProdImage() != null)
            {
                Bitmap image = product.getProdImage();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                values.put(COL_IMAGE, out.toByteArray());
            }
            if (product.getBarCode() != null)
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
            values.put(COL_NAME, product.getProductName());
            values.put(COL_QTY, product.getQuantity());
            values.put(COL_CATEGORY_NAME, product.getCategoryName());
            values.put(COL_THRESHOLD, product.getThreshold());
            if (product.getProdImage() != null)
            {
                Bitmap image = product.getProdImage();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                values.put(COL_IMAGE, out.toByteArray());
            }

            if (product.getBarCode() != null)
                values.put(COL_BARCODE, product.getBarCode());

            // updating row
            db.update(dbHelper.TABLE_PRODUCT, values, COL_NAME + " = " + product.getProductName(), null);
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
            db.delete(dbHelper.TABLE_PRODUCT, COL_NAME + " = " + product.getProductName(), null);
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
                Product product = new Product(cursor.getString(2),cursor.getString(0));
                product.setQuantity(Integer.parseInt(cursor.getString(1)));
                product.setThreshold(Integer.parseInt(cursor.getString(3)));
                if (cursor.getBlob(4) != null)
                {
                    byte[] blobVal = cursor.getBlob(4);
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(5) != null)
                    product.setBarCode(cursor.getString(5));

                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
    // Getting All Products by category name
    public List<Product> getProductsByCategoryName(String categoryName) {
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT + " WHERE "+COL_CATEGORY_NAME+" = '"+categoryName+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(2),cursor.getString(0));
                product.setQuantity(Integer.parseInt(cursor.getString(1)));
                product.setThreshold(Integer.parseInt(cursor.getString(3)));
                if (cursor.getBlob(4) != null)
                {
                    byte[] blobVal = cursor.getBlob(4);
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(5) != null)
                    product.setBarCode(cursor.getString(5));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }

    //Get product object by Product Name
    public Product getProductByName(String prodName){
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CATEGORY + " WHERE " + COL_NAME + " = '" + prodName + "'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0){
            return null;
        }
        Product product = new Product(cursor.getString(2),cursor.getString(0));
        product.setQuantity(Integer.parseInt(cursor.getString(1)));
        product.setThreshold(Integer.parseInt(cursor.getString(3)));
        if (cursor.getBlob(4) != null)
        {
            byte[] blobVal = cursor.getBlob(4);
            Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
            product.setProdImage(bmp);
        }

        if (cursor.getString(5) != null)
            product.setBarCode(cursor.getString(5));
        return product;
    }

    public boolean isProductExists(String prodName)
    {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_PRODUCT + " WHERE " + COL_NAME + " = '" + prodName + "'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount() == 0){
            return false;
        }
        else
            return true;
    }
}
