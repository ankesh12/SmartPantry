package sg.edu.nus.iss.smartpantry.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Category;
import sg.edu.nus.iss.smartpantry.dao.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.SqliteHelper;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class CategoryDaoImpl implements CategoryDao {

    private SqliteHelper dbHelper;

    // Category Table Columns names
    private static final String COL_ID = "CategoryId";
    private static final String COL_NAME = "Name";

    public CategoryDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }

    public boolean addCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, category.getCategoryId());
            values.put(COL_NAME, category.getName());

            db.insert(dbHelper.TABLE_CATEGORY, null, values);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public boolean updateCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_ID, category.getCategoryId());
            values.put(COL_NAME, category.getName());

            // updating row
            db.update(dbHelper.TABLE_CATEGORY, values, COL_ID + " = " + category.getCategoryId(), null);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public boolean deleteCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_CATEGORY, COL_ID + " = " + category.getCategoryId(), null);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
    // Getting All Categories
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<Category>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_CATEGORY;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId(cursor.getString(0));
                category.setName(cursor.getString(1));
                // Adding category to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        // return category list
        return categoryList;
    }

   //Get category object by CategoryID
    public Category getCategoryById(String id){
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CATEGORY + " WHERE " + COL_ID + " = '" + id + "'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount() == 0){
            return null;
        }
        cursor.moveToFirst();
        System.out.println("Count: " + cursor.getCount());
        Category category = new Category();
        category.setCategoryId(cursor.getString(0));
        category.setName(cursor.getString(1));
        return category;
    }
}
