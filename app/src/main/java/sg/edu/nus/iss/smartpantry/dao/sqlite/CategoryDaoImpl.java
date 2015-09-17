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

    public CategoryDaoImpl(Context context)
    {
        dbHelper = new SqliteHelper(context);
    }

    @Override
    public boolean addCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_CAT_NAME, category.getCategoryName());

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

    @Override
    public boolean updateCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_CAT_NAME, category.getCategoryName());

            // updating row
            db.update(dbHelper.TABLE_CATEGORY, values, dbHelper.COL_CAT_NAME + " = '" + category.getCategoryName()+"'", null);
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
    public boolean deleteCategory(Category category)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.TABLE_CATEGORY, dbHelper.COL_CAT_NAME + " = '" + category.getCategoryName()+"'", null);
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
    @Override
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
                category.setCategoryName(cursor.getString(cursor.getColumnIndex(dbHelper.COL_CAT_NAME)));
                // Adding category to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        // return category list
        return categoryList;
    }

    //Get category object by CategoryName
    @Override
    public Category getCategoryByName(String categoryName){
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CATEGORY + " WHERE " + dbHelper.COL_CAT_NAME + " = '" + categoryName + "'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0){
            return null;
        }

        Category category = new Category();
        category.setCategoryName(cursor.getString(cursor.getColumnIndex(dbHelper.COL_CAT_NAME)));
        return category;
    }

    @Override
    public boolean isCategoryExists(String categoryName)
    {
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CATEGORY + " WHERE " + dbHelper.COL_CAT_NAME + " = '" + categoryName + "'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0){
            return false;
        }
        else
            return true;
    }
}
