package sg.edu.nus.iss.smartpantry.dao.daoClass;

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
    public List<Product> getProductsByCategory(int categoryId);
    public List<Product> getProductsByName(String prodName);
    public Product getProductById(int productId);
    public boolean isProductExists(int productId);
    public List<Product> getProductBelowThreshold();
    public List<Product> getProductsNearingThreshold();
    public List<Product> getProductsNearingExpiry();
    public Product getProductByCategoryNameAndProdName(String categoryName,String prodName);
    public int generateProductId(int categoryId);

    }
