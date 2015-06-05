package sg.edu.nus.iss.smartpantry.dao;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;

/**
 * Created by CHARAN on 5/8/2015.
 */
public interface ProductDao {
    public boolean addProduct(Product product);
    public boolean updateProduct(Product product);
    public boolean deleteProduct(Product product);
    public List<Product> getAllProducts();
    public List<Product> getProductsByCategoryName(String categoryName);
    public List<Product> getProductsByName(String prodName);
    public boolean isProductExists(String categoryName,String prodName);
    public Product getProduct(String categoryName,String prodName);
    public List<Product> getProductBelowThreshold();
    public List<Product> getProductsNearingThreshold();
    public List<Product> getProductsNearingExpiry();
}
