package sg.edu.nus.iss.smartpantry.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry";

    private static final int DATABASE_VERSION = 1;

    public final String TABLE_CATEGORY = "Category";
    public final String TABLE_PRODUCT = "Product";
    public final String TABLE_ITEM = "Item";

    private final String CREATE_TABLE_CATEGORY = "CREATE TABLE Category (CategoryName STRING NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY ON CONFLICT ROLLBACK)";
    private final String CREATE_TABLE_PRODUCT = "CREATE TABLE Product (ProductName STRING NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY ON CONFLICT ROLLBACK, Quantity INTEGER (10000) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), CategoryName STRING NOT NULL ON CONFLICT ROLLBACK REFERENCES Category (CategoryName) ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE, Threshold INTEGER (2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (1), ProdImage BLOB, BarCode STRING (300))";
    private final String CREATE_TABLE_ITEM = "CREATE TABLE Item (ItemId INTEGER (5), ProductName STRING REFERENCES Product (productName) ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE, ExpiryDate DATE, Price DECIMAL (4, 2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), PRIMARY KEY (ItemId, ProductName) ON CONFLICT ROLLBACK)";

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
