package sg.edu.nus.iss.smartpantry.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static SqliteHelper instance;

    private static final String DATABASE_NAME = "SmartPantry.sqlite";

    private static final int DATABASE_VERSION = 1;

    public final String TABLE_CATEGORY = "Category";
    public final String TABLE_PRODUCT = "Product";
    public final String TABLE_ITEM = "Item";
    public final String TABLE_SHOPPING_LIST = "ShoppingList";

    // Category Table Columns names
    public static final String COL_CAT_ID="CategoryId";
    public static final String COL_CAT_NAME = "CategoryName";

    // Product Table Columns names
    public static final String COL_PROD_ID = "ProductId";
    public static final String COL_PROD_NAME = "ProductName";
    public static final String COL_PROD_QTY = "Quantity";
    public static final String COL_PROD_CATEGORY_ID="CategoryId";
    public static final String COL_PROD_THRESHOLD = "Threshold";
    public static final String COL_PROD_IMAGE = "ProdImage";
    public static final String COL_PROD_BARCODE = "BarCode";

    // Item Table Columns names
    public static final String COL_ITEM_ID = "ItemId";
    public static final String COL_ITEM_PRODUCT_ID = "ProductId";
    public static final String COL_ITEM_CATEGORY_ID = "CategoryId";
    public static final String COL_ITEM_EXPIRY_DATE = "ExpiryDate";
    public static final String COL_ITEM_PRICE = "Price";
    public static final String COL_ITEM_DOP = "DateOfPurchase";

    // Shopping List Table Columns names
    public static final String COL_SHOPPING_LIST_NAME = "ShopListName";
    public static final String COL_SHOPPING_LIST_PRODUCT_ID = "ProductId";
    public static final String COL_SHOPPING_LIST_CATEGORY_ID = "CategoryId";
    public static final String COL_SHOPPING_LIST_QTY= "Quantity";
    public static final String COL_SHOPPING_LIST_IS_PURCHASED = "IsPurchased";

    private final String CREATE_TABLE_CATEGORY = "CREATE TABLE "+TABLE_CATEGORY+" ("+COL_CAT_ID+" INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "+COL_CAT_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK UNIQUE ON CONFLICT ROLLBACK)";

    private final String CREATE_TABLE_PRODUCT = "CREATE TABLE "+TABLE_PRODUCT+" ("+COL_PROD_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK, "+COL_PROD_CATEGORY_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK REFERENCES "+TABLE_CATEGORY+" ("+COL_CAT_ID+") ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE, "+COL_PROD_NAME+" STRING NOT NULL ON CONFLICT ROLLBACK, " +COL_PROD_QTY+" INTEGER (10000) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), "+COL_PROD_THRESHOLD+" INTEGER (2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (1), "+COL_PROD_IMAGE+" BLOB, "+COL_PROD_BARCODE+" STRING (300), PRIMARY KEY ("+COL_PROD_ID+","+COL_PROD_CATEGORY_ID+") ON CONFLICT ROLLBACK, UNIQUE ("+COL_PROD_NAME+","+COL_PROD_CATEGORY_ID+") ON CONFLICT ROLLBACK)";

    private final String CREATE_TABLE_ITEM = "CREATE TABLE "+TABLE_ITEM+" ("+COL_ITEM_ID+" INTEGER (5) NOT NULL ON CONFLICT ROLLBACK, "+COL_ITEM_PRODUCT_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK, "+COL_ITEM_CATEGORY_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK, "+COL_ITEM_EXPIRY_DATE+" DATE, "+COL_ITEM_PRICE+" DECIMAL (4, 2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), "+COL_ITEM_DOP+"  DATE DEFAULT (CURRENT_DATE) NOT NULL ON CONFLICT ROLLBACK, PRIMARY KEY ("+COL_ITEM_ID+", "+COL_ITEM_PRODUCT_ID+", "+COL_ITEM_CATEGORY_ID+") ON CONFLICT ROLLBACK, FOREIGN KEY ("+COL_ITEM_PRODUCT_ID+", "+COL_ITEM_CATEGORY_ID+") REFERENCES "+TABLE_PRODUCT+" ("+COL_PROD_ID+", "+COL_PROD_CATEGORY_ID+") ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE)";

    private final String CREATE_TABLE_SHOPPING_LIST="CREATE TABLE "+TABLE_SHOPPING_LIST+"  ("+COL_SHOPPING_LIST_NAME+"  STRING NOT NULL ON CONFLICT ROLLBACK, "+COL_SHOPPING_LIST_PRODUCT_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK, "+COL_SHOPPING_LIST_CATEGORY_ID+" INTEGER NOT NULL ON CONFLICT ROLLBACK, "+COL_SHOPPING_LIST_QTY+" INTEGER DEFAULT (1), "+COL_SHOPPING_LIST_IS_PURCHASED+" BOOLEAN DEFAULT (0), PRIMARY KEY ("+COL_SHOPPING_LIST_NAME+", "+COL_SHOPPING_LIST_PRODUCT_ID+", "+COL_SHOPPING_LIST_CATEGORY_ID+") ON CONFLICT ROLLBACK, FOREIGN KEY ("+COL_SHOPPING_LIST_PRODUCT_ID+", "+COL_SHOPPING_LIST_CATEGORY_ID+") REFERENCES "+TABLE_PRODUCT+" ("+COL_PROD_ID+", "+COL_PROD_CATEGORY_ID+") ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE)";


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        instance=null;
    }

    public static synchronized SqliteHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new SqliteHelper(context);

        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_SHOPPING_LIST);
        System.out.println("Tables created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);

    }
}