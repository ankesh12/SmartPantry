package sg.edu.nus.iss.smartpantry.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;
import sg.edu.nus.iss.smartpantry.dao.ShoppingListDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/29/2015.
 */
public class ShoppingListDaoImpl implements ShoppingListDao {

    private SqliteHelper dbHelper;

    public ShoppingListDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }

    @Override
    public boolean addProductToShopList(String shopListName,Product product,int quantity,boolean isPurchased) {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_SHOPPING_LIST_NAME, shopListName);
            values.put(dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME, product.getProductName());
            values.put(dbHelper.COL_SHOPPING_LIST_CATEGORY_NAME, product.getCategoryName());
            values.put(dbHelper.COL_SHOPPING_LIST_QTY, quantity);
            if(isPurchased != true)
                isPurchased=false;
            values.put(dbHelper.COL_SHOPPING_LIST_IS_PURCHASED, isPurchased);

            db.insert(dbHelper.TABLE_SHOPPING_LIST, null, values);
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
    public boolean updateProductInShopList(String shopListName,Product product,int quantity,boolean
            isPurchased) {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_SHOPPING_LIST_NAME, shopListName);
            values.put(dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME, product.getProductName());
            values.put(dbHelper.COL_SHOPPING_LIST_CATEGORY_NAME, product.getCategoryName());
            values.put(dbHelper.COL_SHOPPING_LIST_QTY, quantity);
            if(isPurchased != true)
                isPurchased=false;
            values.put(dbHelper.COL_SHOPPING_LIST_IS_PURCHASED, isPurchased);

            db.update(dbHelper.TABLE_SHOPPING_LIST, values, dbHelper.COL_SHOPPING_LIST_NAME + " = '" + shopListName + "' AND " + dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME + " = '" + product.getProductName() + "' AND " + dbHelper.COL_SHOPPING_LIST_CATEGORY_NAME + " = '" + product.getCategoryName() + "'", null);
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
    public boolean deleteProductFromShopList(String shopListName,Product product) {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_SHOPPING_LIST, dbHelper.COL_SHOPPING_LIST_NAME + " = '" + shopListName + "' AND " + dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME + " = '" + product.getProductName() + "' AND " + dbHelper.COL_SHOPPING_LIST_CATEGORY_NAME + " = '" + product.getCategoryName() + "'", null);
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
    public List<ShoppingProduct> getYetToBuyProductsInShopLists() {
        List<ShoppingProduct> productList = new ArrayList<ShoppingProduct>();
        // Select All Query
        String selectQuery = "SELECT "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_NAME+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_CATEGORY_NAME+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_QTY+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_THRESHOLD+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_IMAGE+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_BARCODE+","+dbHelper.TABLE_SHOPPING_LIST+"."+dbHelper.COL_SHOPPING_LIST_QTY+","+dbHelper.TABLE_SHOPPING_LIST+"."+dbHelper.COL_SHOPPING_LIST_IS_PURCHASED+" FROM "+dbHelper.TABLE_PRODUCT+","+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_NAME+" IN (SELECT "+dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME+" FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_IS_PURCHASED+"=0) AND "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_CATEGORY_NAME+" IN (SELECT "+dbHelper.COL_SHOPPING_LIST_CATEGORY_NAME+" FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_IS_PURCHASED+"=0)";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(1),cursor.getString(0));
                product.setQuantity(Integer.parseInt(cursor.getString(2)));
                product.setThreshold(Integer.parseInt(cursor.getString(3)));
                if (cursor.getBlob(4) != null)
                {
                    byte[] blobVal = cursor.getBlob(4);
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(5) != null)
                    product.setBarCode(cursor.getString(5));

                int shopQty =(Integer.parseInt(cursor.getString(6)));
                Boolean isPurchased =(Boolean.parseBoolean(cursor.getString(7)));
                // Adding product to list
                productList.add(new ShoppingProduct(product,shopQty,isPurchased));
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }

    @Override
    public List<ShoppingProduct> getProductsByShopListName(String shopListName) {
        List<ShoppingProduct> productList = new ArrayList<ShoppingProduct>();
        // Select All Query
        String selectQuery = "SELECT "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_NAME+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_CATEGORY_NAME+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_QTY+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_THRESHOLD+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_IMAGE+","+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_BARCODE+","+dbHelper.TABLE_SHOPPING_LIST+"."+dbHelper.COL_SHOPPING_LIST_QTY+","+dbHelper.TABLE_SHOPPING_LIST+"."+dbHelper.COL_SHOPPING_LIST_IS_PURCHASED+" FROM "+dbHelper.TABLE_PRODUCT+","+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_NAME+" IN (SELECT "+dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME+" FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_NAME+" = '"+shopListName+"') AND "+dbHelper.TABLE_PRODUCT+"."+dbHelper.COL_PROD_CATEGORY_NAME+" IN (SELECT "+dbHelper.COL_SHOPPING_LIST_PRODUCT_NAME+" FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_NAME+" = '"+shopListName+"')";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(1),cursor.getString(0));
                product.setQuantity(Integer.parseInt(cursor.getString(2)));
                product.setThreshold(Integer.parseInt(cursor.getString(3)));
                if (cursor.getBlob(4) != null)
                {
                    byte[] blobVal = cursor.getBlob(4);
                    Bitmap bmp = BitmapFactory.decodeByteArray(blobVal, 0, blobVal.length);
                    product.setProdImage(bmp);
                }

                if (cursor.getString(5) != null)
                    product.setBarCode(cursor.getString(5));

                int shopQty =(Integer.parseInt(cursor.getString(6)));
                Boolean isPurchased =(Boolean.parseBoolean(cursor.getString(7)));
                // Adding product to list
                productList.add(new ShoppingProduct(product,shopQty,isPurchased));
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
}
