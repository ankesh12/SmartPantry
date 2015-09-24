package sg.edu.nus.iss.smartpantry.dao.daoImpl;

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
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ShoppingListDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/29/2015.
 */
public class ShoppingListDaoImpl implements ShoppingListDao {

    private SqliteHelper dbHelper;
    private Context mContext;

    public ShoppingListDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
        mContext=context;
    }

    @Override
    public boolean addProductToShopList(String shopListName,Product product,int quantity,boolean isPurchased) {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_SHOPPING_LIST_NAME, shopListName);
            values.put(dbHelper.COL_SHOPPING_LIST_PRODUCT_ID, product.getProductId());
            values.put(dbHelper.COL_SHOPPING_LIST_CATEGORY_ID, product.getCategory().getCategoryId());
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
            values.put(dbHelper.COL_SHOPPING_LIST_PRODUCT_ID, product.getProductId());
            values.put(dbHelper.COL_SHOPPING_LIST_CATEGORY_ID, product.getCategory().getCategoryId());
            values.put(dbHelper.COL_SHOPPING_LIST_QTY, quantity);
            /*if(isPurchased != true)
                isPurchased=false;*/
            values.put(dbHelper.COL_SHOPPING_LIST_IS_PURCHASED, isPurchased);

            db.update(dbHelper.TABLE_SHOPPING_LIST, values, dbHelper.COL_SHOPPING_LIST_NAME + " = '" + shopListName + "' AND " + dbHelper.COL_SHOPPING_LIST_PRODUCT_ID + " = " + product.getProductId(), null);
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
            db.delete(dbHelper.TABLE_SHOPPING_LIST, dbHelper.COL_SHOPPING_LIST_NAME + " = '" + shopListName + "' AND " + dbHelper.COL_SHOPPING_LIST_PRODUCT_ID + " = " + product.getProductId(), null);
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
        String selectShopItemQuery = "SELECT * FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_IS_PURCHASED+"=0";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor shopCursor = db.rawQuery(selectShopItemQuery, null);

        // looping through all rows and adding to list
        if (shopCursor.moveToFirst()) {
            do {
                String shopListNameVal=shopCursor.getString(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_NAME));
                int prodId=shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_PRODUCT_ID));
                int catId=shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_CATEGORY_ID));
                int shopQty =(Integer.parseInt(shopCursor.getString(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_QTY))));
                //Boolean isPurchased =(Boolean.parseBoolean(shopCursor.getString(4)));
                Boolean isPurchased;
                int boolValue = (shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_IS_PURCHASED)));
                System.out.println("Value for boolean " + boolValue);
                if(boolValue == 1){
                    isPurchased = true;
                }
                else{
                    isPurchased = false;
                }

                String selectProdQuery = "SELECT * FROM "+dbHelper.TABLE_PRODUCT+" WHERE "+dbHelper.COL_PROD_ID + " = " + prodId + " AND " +dbHelper.COL_PROD_CATEGORY_ID + " = "+catId;
                Cursor cursor = db.rawQuery(selectProdQuery, null);
                if (cursor.moveToFirst())
                {
                    Product product = new Product(DAOFactory.getCategoryDao(mContext).getCategoryById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_ID))),cursor.getInt(cursor.getColumnIndex(dbHelper.COL_PROD_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
                    product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_QTY))));
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
                    productList.add(new ShoppingProduct(product,shopQty,isPurchased));
                }
            } while (shopCursor.moveToNext());
        }

        // return product list
        return productList;
    }

    @Override
    public List<ShoppingProduct> getProductsByShopListName(String shopListName) {
        List<ShoppingProduct> productList = new ArrayList<ShoppingProduct>();
        // Select All Query
        String selectShopItemQuery = "SELECT * FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_NAME+"='"+shopListName+"'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor shopCursor = db.rawQuery(selectShopItemQuery, null);

        // looping through all rows and adding to list
        if (shopCursor.moveToFirst()) {
            do {
                String shopListNameVal=shopCursor.getString(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_NAME));
                int prodId=shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_PRODUCT_ID));
                int catId=shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_CATEGORY_ID));
                int shopQty =(Integer.parseInt(shopCursor.getString(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_QTY))));
                Boolean isPurchased;
                int boolValue = (shopCursor.getInt(shopCursor.getColumnIndex(dbHelper.COL_SHOPPING_LIST_IS_PURCHASED)));
                System.out.println("Value for boolean " + boolValue);
                if(boolValue == 1){
                    isPurchased = true;
                }
                else{
                    isPurchased = false;
                }

                String selectProdQuery = "SELECT * FROM "+dbHelper.TABLE_PRODUCT+" WHERE "+dbHelper.COL_PROD_ID + " = " + prodId + " AND " +dbHelper.COL_PROD_CATEGORY_ID + " = "+catId;
                Cursor cursor = db.rawQuery(selectProdQuery, null);
                if (cursor.moveToFirst())
                {
                    Product product = new Product(DAOFactory.getCategoryDao(mContext).getCategoryById(cursor.getInt(cursor.getColumnIndex(dbHelper.COL_PROD_CATEGORY_ID))),cursor.getInt(cursor.getColumnIndex(dbHelper.COL_PROD_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndex(dbHelper.COL_PROD_NAME)));
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
                    productList.add(new ShoppingProduct(product, shopQty, isPurchased));
                }
            } while (shopCursor.moveToNext());
        }

        // return product list
        return productList;
    }

    @Override
    public boolean isProductInShopList(String shopListName,Product product) {
        // Select All Query
        String selectShopItemQuery = "SELECT * FROM "+dbHelper.TABLE_SHOPPING_LIST+" WHERE "+dbHelper.COL_SHOPPING_LIST_NAME+"='"+shopListName+"' AND "+dbHelper.COL_SHOPPING_LIST_PRODUCT_ID+" = "+product.getProductId()+" AND "+dbHelper.COL_SHOPPING_LIST_CATEGORY_ID+" = "+product.getCategory().getCategoryId() ;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectShopItemQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return false;
        else
            return  true;
    }
}
