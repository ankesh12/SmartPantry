package sg.edu.nus.iss.smartpantry.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.sqlite";

    private static final int DATABASE_VERSION = 1;

    public final String TABLE_CATEGORY = "Category";
    public final String TABLE_PRODUCT = "Product";
    public final String TABLE_ITEM = "Item";

    // Category Table Columns names
    public static final String COL_CAT_NAME = "CategoryName";

    // Product Table Columns names
    public static final String COL_PROD_NAME = "ProductName";
    public static final String COL_PROD_QTY = "Quantity";
    public static final String COL_PROD_CATEGORY_NAME = "CategoryName";
    public static final String COL_PROD_THRESHOLD = "Threshold";
    public static final String COL_PROD_IMAGE = "ProdImage";
    public static final String COL_PROD_BARCODE = "BarCode";

    // Item Table Columns names
    public static final String COL_ITEM_ID = "ItemId";
    public static final String COL_ITEM_PRODUCT_NAME = "ProductName";
    public static final String COL_ITEM_CATEGORY_NAME = "CategoryName";
    public static final String COL_ITEM_EXPIRY_DATE = "ExpiryDate";
    public static final String COL_ITEM_PRICE = "Price";

    private final String CREATE_TABLE_CATEGORY = "CREATE TABLE "+TABLE_CATEGORY+" ("+COL_CAT_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY ON CONFLICT ROLLBACK)";
    private final String CREATE_TABLE_PRODUCT = "CREATE TABLE "+TABLE_PRODUCT+" ("+COL_PROD_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK, "+COL_PROD_CATEGORY_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK REFERENCES "+TABLE_CATEGORY+" ("+COL_CAT_NAME+") ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE, "+COL_PROD_QTY+" INTEGER (10000) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), "+COL_PROD_THRESHOLD+" INTEGER (2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (1), "+COL_PROD_IMAGE+" BLOB, "+COL_PROD_BARCODE+" STRING (300), PRIMARY KEY ("+COL_PROD_NAME+", "+COL_PROD_CATEGORY_NAME+") ON CONFLICT ROLLBACK)";
    private final String CREATE_TABLE_ITEM = "CREATE TABLE "+TABLE_ITEM+" ("+COL_ITEM_ID+" INTEGER (5), "+COL_ITEM_PRODUCT_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK, "+COL_ITEM_CATEGORY_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK, "+COL_ITEM_EXPIRY_DATE+" DATE, "+COL_ITEM_PRICE+" DECIMAL (4, 2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), PRIMARY KEY ("+COL_ITEM_ID+", "+COL_ITEM_PRODUCT_NAME+", "+COL_ITEM_CATEGORY_NAME+") ON CONFLICT ROLLBACK, FOREIGN KEY ("+COL_ITEM_PRODUCT_NAME+", "+COL_ITEM_CATEGORY_NAME+") REFERENCES "+TABLE_PRODUCT+" ("+COL_PROD_NAME+", "+COL_PROD_CATEGORY_NAME+") ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE)";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_ITEM);
        System.out.println("TAbles created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);

    }
}
