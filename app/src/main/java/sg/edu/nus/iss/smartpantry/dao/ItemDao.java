package sg.edu.nus.iss.smartpantry.dao;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;

/**
 * Created by CHARAN on 5/8/2015.
 */
public interface ItemDao {
    public boolean addItem(Item item);
    public boolean updateItem(Item item);
    public boolean deleteItem(Item item);
    public List<Item> getAllItems();
    public List<Item> getItemsByProductAndCategoryName(String categoryName,String  productName);
    public int generateItemIdForProduct(String productName);
    public List<Item> getItemsNearingExpiry();

}
