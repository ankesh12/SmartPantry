package sg.edu.nus.iss.smartpantry.Entity;

/**
 * Created by CHARAN on 6/5/2015.
 */
public class ShoppingProduct {

    private Product product;
    private int shopQty;
    private boolean isPurchased;

    public ShoppingProduct(Product product,int shopQty,boolean isPurchased)
    {
        this.product=product;
        this.shopQty=shopQty;
        this.isPurchased=isPurchased;
    }

    public int getShopQty() {
        return shopQty;
    }

    public Product getProduct() {
        return product;
    }

    public boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(boolean value){
        this.isPurchased = value;
    }

    public void setShopQty(int shopQty) {
        this.shopQty = shopQty;
    }

    @Override
    public int hashCode() {
        return getProduct().getProductName().hashCode() + getProduct().getCategory().getCategoryName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        ShoppingProduct shopProduct = (ShoppingProduct) o;
        return (getProduct().equals(shopProduct.getProduct()));
    }
}
