package sg.edu.nus.iss.smartpantry.dao.daoClass;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Category;

/**
 * Created by CHARAN on 5/8/2015.
 */
public interface CategoryDao {

    public boolean addCategory(Category category);
    public boolean updateCategory(Category category);
    public boolean deleteCategory(Category category);
    public List<Category> getAllCategories();
    public Category getCategoryByName(String categoryName);
    public boolean isCategoryExists(int categoryId);
    public int generateCategoryId();
    public Category getCategoryById(int categoryId);
}
