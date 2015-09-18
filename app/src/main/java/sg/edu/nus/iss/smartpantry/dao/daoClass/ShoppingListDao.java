package sg.edu.nus.iss.smartpantry.dao.daoClass;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;

/**
 * Created by CHARAN on 5/29/2015.
 */
public interface ShoppingListDao {
    public boolean addProductToShopList(String shopListName,Product product,int quantity,boolean IsPurchased);
    public boolean updateProductInShopList(String shopListName,Product product,int quantity,boolean IsPurchased);
    public boolean deleteProductFromShopList(String shopListName,Product product);
    public List<ShoppingProduct> getYetToBuyProductsInShopLists();
    public List<ShoppingProduct> getProductsByShopListName(String shopListName);
    public boolean isProductInShopList(String shopListName,Product product);
}
