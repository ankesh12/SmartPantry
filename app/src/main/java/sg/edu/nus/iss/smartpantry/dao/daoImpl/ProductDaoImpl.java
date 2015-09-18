package sg.edu.nus.iss.smartpantry.dao.daoImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class ProductDaoImpl implements ProductDao {

    private SqliteHelper dbHelper;

    public ProductDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }

    @Override
    public boolean addProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_PROD_NAME, product.getProductName());
            values.put(dbHelper.COL_PROD_CATEGORY_NAME, product.getCategoryName());
            values.put(dbHelper.COL_PROD_QTY, product.getQuantity());
            values.put(dbHelper.COL_PROD_THRESHOLD, product.getThreshold());
            if (product.getProdImage() != null)
            {
                Bitmap image = product.getProdImage();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                values.put(dbHelper.COL_PROD_IMAGE, out.toByteArray());
            }
            if (product.getBarCode() != null)
                values.put(dbHelper.COL_PROD_BARCODE, product.getBarCode());

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

    @Override
    public boolean updateProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_PROD_NAME, product.getProductName());
            values.put(dbHelper.COL_PROD_CATEGORY_NAME, product.getCategoryName());
            values.put(dbHelper.COL_PROD_QTY, product.getQuantity());
            values.put(dbHelper.COL_PROD_THRESHOLD, product.getThreshold());
            if (product.getProdImage() != null)
            {
                Bitmap image = product.getProdImage();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                values.put(dbHelper.COL_PROD_IMAGE, out.toByteArray());
            }

            if (product.getBarCode() != null)
                values.put(dbHelper.COL_PROD_BARCODE, product.getBarCode());

            // updating row
            db.update(dbHelper.TABLE_PRODUCT, values, dbHelper.COL_PROD_NAME + " = '" + product.getProductName() + "' AND "+dbHelper.COL_PROD_CATEGORY_NAME+" = '"+product.getCategoryName()+"'", null);
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
    public boolean deleteProduct(Product product)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_PRODUCT, dbHelper.COL_PROD_NAME + " = '" + product.getProductName() + "' AND "+dbHelper.COL_PROD_CATEGORY_NAME+" = '"+product.getCategoryName()+"'", null);
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
    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null)
                {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));

                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }

    // Getting All Products by category name
    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT + " WHERE "+dbHelper.COL_PROD_CATEGORY_NAME+" = '"+categoryName+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null)
                {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }

    //Get products List by Product Name
    @Override
    public List<Product> getProductsByName(String prodName){
        List<Product> productList = new ArrayList<Product>();
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_PRODUCT + " WHERE " + dbHelper.COL_PROD_NAME + " = '" + prodName + "'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null)
                {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }

    @Override
    public boolean isProductExists(String categoryName,String prodName)
    {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_PRODUCT + " WHERE " + dbHelper.COL_PROD_NAME + " = '" + prodName + "' AND "+dbHelper.COL_PROD_CATEGORY_NAME + " = '"+categoryName +"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0){
            return false;
        }
        else
            return true;
    }

    @Override
    public Product getProduct(String categoryName,String prodName)
    {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_PRODUCT + " WHERE " + dbHelper.COL_PROD_NAME + " = '" + prodName + "' AND "+dbHelper.COL_PROD_CATEGORY_NAME + " = '"+categoryName +"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
            product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
            product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
            if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null)
            {
                byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                product.setProdImage(bmp);
            }

            if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));

            // return product
            return product;
        }
        return null;

    }

    @Override
    public List<Product> getProductsNearingThreshold() {
        List<Product> productList = new ArrayList<Product>();
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_PRODUCT + " WHERE " + dbHelper
                .COL_PROD_QTY + "=0 OR " + dbHelper.COL_PROD_QTY + " <= " + dbHelper.COL_PROD_THRESHOLD;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null) {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));

                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        return productList;
    }

    @Override
    public List<Product> getProductsNearingExpiry() {
        List<Product> productList = new ArrayList<Product>();
        String selectQuery ="SELECT * FROM "+dbHelper.TABLE_PRODUCT+" WHERE "+dbHelper
                .COL_PROD_NAME+" IN (SELECT "+dbHelper.COL_ITEM_PRODUCT_NAME+" FROM "+dbHelper
                .TABLE_ITEM+" WHERE "+dbHelper.COL_ITEM_EXPIRY_DATE+" BETWEEN CURRENT_DATE AND " +
                "date(CURRENT_DATE,'+7 day') OR "+dbHelper.COL_ITEM_EXPIRY_DATE+" < CURRENT_DATE)" +
                " AND "+dbHelper.COL_PROD_CATEGORY_NAME+" IN (SELECT "+dbHelper
                .COL_ITEM_CATEGORY_NAME+" FROM "+dbHelper.TABLE_ITEM+" WHERE "+dbHelper
                .COL_ITEM_EXPIRY_DATE+" BETWEEN CURRENT_DATE AND date(CURRENT_DATE,'+7 day') OR " +
                ""+dbHelper.COL_ITEM_EXPIRY_DATE+" < CURRENT_DATE)";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null) {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));

                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        return productList;
    }

    @Override
    public List<Product> getProductBelowThreshold() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_PRODUCT;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_NAME)),cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_QTY))));
                product.setThreshold(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_THRESHOLD))));
                if (cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE)) != null)
                {
                    byte[] blobVal = cursor.getBlob(cursor.getColumnIndex(dbHelper.COL_PROD_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)) != null)
                    product.setBarCode(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_BARCODE)));

                // Adding product to list
                if (product.getQuantity() <= product.getThreshold()){
                    productList.add(product);
                }

            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
}
