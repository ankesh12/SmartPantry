package sg.edu.nus.iss.smartpantry.Entity;

/**
 * Created by CHARAN on 5/8/2015.
 */
public class WatchListProduct{
    private Product prod;
    private boolean isSelected;
    private boolean isPresentInShoppingList;

    public boolean isPresentInShoppingList() {
        return isPresentInShoppingList;
    }

    public void setIsPresentInShoppingList(boolean isPresentInShoppingList) {
        this.isPresentInShoppingList = isPresentInShoppingList;
    }

    public WatchListProduct(Product prod){
        this.prod=prod;
        this.isSelected=false;
        this.isPresentInShoppingList= false;
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
        return prod.getProductName().hashCode() + prod.getCategory().getCategoryName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        WatchListProduct product = (WatchListProduct) o;
        return (this.prod.getProductName().equalsIgnoreCase(product.prod.getProductName()) && this.prod.getCategory().getCategoryName().equalsIgnoreCase(product.prod.getCategory().getCategoryName()));
    }
}

