package sg.edu.nus.iss.smartpantry.Entity;

import android.graphics.Bitmap;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class Product {
    private String prodId;
    private String productName;
    private int quantity;
    private String categoryId;
    private int threshold;
    private Bitmap prodImage;
    private String barCode;

    public Product(String categoryId,String prodId){
        this.categoryId=categoryId;
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setProdImage(Bitmap prodImage) {
        this.prodImage = prodImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
