package sg.edu.nus.iss.smartpantry.Entity;

import android.graphics.Bitmap;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class Product {
    private String productName;
    private int quantity;
    private int productId;
    private Category category;
    private int threshold;
    private Bitmap prodImage;
    private String barCode;

    public Product(Category category, int productId){
        this.category = category;
        this.productId=productId;
    }

    public String getProductName() {
        return productName;
    }
    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public Bitmap getProdImage() {
        return prodImage;
    }

    public int getProductId() {
        return productId;
    }

    public void setProdImage(Bitmap prodImage) {
        this.prodImage = prodImage;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        return getProductName().hashCode() + getCategory().getCategoryName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
//        if(o.getClass().equals(ShoppingProduct.class)){
//            ShoppingProduct shopProd = (ShoppingProduct) o;
//            return (this.getProductName().equalsIgnoreCase(shopProd.getProduct().getProductName()) && this.getCategoryName().equalsIgnoreCase(shopProd.getProduct().getCategoryName()));
//        }

        Product product = (Product) o;
        return (this.getProductName().equalsIgnoreCase(product.getProductName()) && this.getCategory().getCategoryName().equalsIgnoreCase(product.getCategory().getCategoryName()));
    }
}

