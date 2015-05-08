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

    private final String CREATE_TABLE_CATEGORY = "CREATE TABLE Category (CategoryId STRING PRIMARY KEY ASC ON CONFLICT ROLLBACK, Name STRING NOT NULL ON CONFLICT ROLLBACK)";
    private final String CREATE_TABLE_PRODUCT = "CREATE TABLE Product (prodId STRING PRIMARY KEY ASC ON CONFLICT ROLLBACK, productName STRING NOT NULL ON CONFLICT ROLLBACK, Quantity INTEGER (10000) NOT NULL ON CONFLICT ROLLBACK DEFAULT (0), CategoryId STRING REFERENCES Category (CategoryId) ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE NOT NULL ON CONFLICT ROLLBACK, Threshold INTEGER (2) NOT NULL ON CONFLICT ROLLBACK DEFAULT (1), prodImage BLOB, BarCode STRING (300))";
    private final String CREATE_TABLE_ITEM = "CREATE TABLE Item (ItemId INTEGER (5), productId STRING REFERENCES Product (prodId) ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE, ExpiryDate DATE, Price DECIMAL (4, 2) NOT NULL ON CONFLICT ROLLBACK, PRIMARY KEY (ItemId, productId) ON CONFLICT ROLLBACK)";

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
