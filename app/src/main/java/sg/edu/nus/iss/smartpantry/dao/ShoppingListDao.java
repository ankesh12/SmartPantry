package sg.edu.nus.iss.smartpantry.dao;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;

/**
 * Created by CHARAN on 5/29/2015.
 */
public interface ShoppingListDao {
    public boolean addProductToShopList(String shopListName,Product product,boolean IsPurchased);
    public boolean updateProductInShopList(String shopListName,Product product,boolean IsPurchased);
    public boolean deleteProductFromShopList(String shopListName,Product product);
    public List<Product> getAllProductsInShopLists();
    public List<Product> getProductsByShopListName(String shopListName);
}
