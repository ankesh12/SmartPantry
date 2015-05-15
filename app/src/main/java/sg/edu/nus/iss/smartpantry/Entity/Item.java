package sg.edu.nus.iss.smartpantry.Entity;

import java.sql.Date;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class Item {
    private int itemId;
    private String productName;
    private Date expiryDate;
    private double price;

    public Item(String productName,int itemId){

        this.itemId = itemId;
        this.productName = productName;

    }

    public int getItemId() {
        return itemId;
    }

    public String getProductName() {
        return productName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
