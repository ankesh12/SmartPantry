package sg.edu.nus.iss.smartpantry.application.util;

import java.util.Comparator;

import sg.edu.nus.iss.smartpantry.Entity.WatchListProduct;

/**
 * Created by CHARAN on 9/2/2015.
 */
public class WatchListProductComparator implements Comparator<WatchListProduct> {
    @Override
    public int compare(WatchListProduct lhs, WatchListProduct rhs) {
        return lhs.getProductName().compareTo(rhs.getProductName());
    }
}
