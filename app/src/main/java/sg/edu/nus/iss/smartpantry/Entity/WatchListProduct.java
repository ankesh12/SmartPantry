package sg.edu.nus.iss.smartpantry.Entity;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class WatchListProduct extends Product{
    private Product prod;
    private boolean isSelected;

    public WatchListProduct(Product prod){
        super(prod.getCategoryName(),prod.getProductName());
        this.prod=prod;
        this.isSelected=false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Product getProd() {
        return prod;
    }


    @Override
    public int hashCode() {
        return getProductName().hashCode() + getCategoryName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
//        if(o.getClass().equals(ShoppingProduct.class)){
//            ShoppingProduct shopProd = (ShoppingProduct) o;
//            return (this.getProductName().equalsIgnoreCase(shopProd.getProduct().getProductName()) && this.getCategoryName().equalsIgnoreCase(shopProd.getProduct().getCategoryName()));
//        }

        WatchListProduct product = (WatchListProduct) o;
        return (this.getProductName().equalsIgnoreCase(product.getProductName()) && this.getCategoryName().equalsIgnoreCase(product.getCategoryName()));
    }
}

