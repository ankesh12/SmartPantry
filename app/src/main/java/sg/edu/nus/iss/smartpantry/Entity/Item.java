package sg.edu.nus.iss.smartpantry.Entity;

import java.sql.Date;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class Item {
    private int itemId;
    private Product product;
    private Date expiryDate;
    private double price;
    private Date dop;

    public Item(Product product,int itemId){

        this.itemId = itemId;
        this.product=product;
    }

    public int getItemId() {
        return itemId;
    }

    public Product getProduct() {
        return product;
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

    public Date getDop() {
        return dop;
    }

    public void setDop(Date dop) {
        this.dop = dop;
    }
}
