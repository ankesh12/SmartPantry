package sg.edu.nus.iss.smartpantry.dto;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by ankesh on 9/18/15.
 */
public class ItemDetailDTO {
    String categoryName;
    String productName;
    Bitmap bitmap;
    Date expiryDate;
    int thresholdQty;
    double price;
    int quantity;

    public ItemDetailDTO(String categoryName, String productName, Bitmap bitmap, Date expiryDate, int thresholdQty, double price, int quantity) {
        this.categoryName = categoryName;
        this.productName = productName;
        this.bitmap = bitmap;
        this.expiryDate = expiryDate;
        this.thresholdQty = thresholdQty;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getThresholdQty() {
        return thresholdQty;
    }

    public void setThresholdQty(int thresholdQty) {
        this.thresholdQty = thresholdQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
