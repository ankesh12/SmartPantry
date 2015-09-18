package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.application.util.ScanBarcode;
import sg.edu.nus.iss.smartpantry.dao.daoClass.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.dto.ItemDetailDTO;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class  ItemController {

    public Intent showAddItemScreen(Context context){
        Intent intent = new Intent(context,ScanBarcode.class);
        return intent;
    }

    public void addItem(Context context, ItemDetailDTO itemDetailDTO){
        ItemDao itemDao= DAOFactory.getItemDao(context);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);
        ProductDao prodDao = DAOFactory.getProductDao(context);
        Product product = prodDao.getProduct(itemDetailDTO.getCategoryName(), itemDetailDTO.getProductName());
        Random rand = new Random();
        if(product == null)
        {
            Product newProduct = new Product(itemDetailDTO.getCategoryName(), itemDetailDTO.getProductName());
            newProduct.setThreshold(itemDetailDTO.getThresholdQty());
            newProduct.setProdImage(itemDetailDTO.getBitmap());
            prodDao.addProduct(newProduct);
        }else{
            if(product.getThreshold()!= itemDetailDTO.getThresholdQty()) {
                product.setThreshold(itemDetailDTO.getThresholdQty());
                prodDao.updateProduct(product);
            }
        }
        for(int i=0;i< itemDetailDTO.getQuantity();i++) {
            int itemId = itemDao.generateItemIdForProduct(itemDetailDTO.getProductName());
            if (itemId != -1) {
                Item itm = new Item(itemDetailDTO.getCategoryName(), itemDetailDTO.getProductName(), itemId);
                itm.setPrice(itemDetailDTO.getPrice());
                if (itemDetailDTO.getExpiryDate() != null)
                    itm.setExpiryDate(new java.sql.Date(itemDetailDTO.getExpiryDate().getTime()));
                itemDao.addItem(itm);
            } else {
                Toast.makeText(context, "Error in generating the Item ID!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*public void addItem_old(Context context,  String categoryName,String productName, Bitmap bitmap, Date expiryDate, int thresholdQty, double price, int quantity) throws ParseException {
        ItemDao itemDao= DAOFactory.getItemDao(context);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);
        ProductDao prodDao = DAOFactory.getProductDao(context);
        Product product = prodDao.getProduct(categoryName, productName);
        Random rand = new Random();
        if(product == null)
        {
            Product newProduct = new Product(categoryName,productName);
            newProduct.setThreshold(thresholdQty);
            newProduct.setProdImage(bitmap);
            prodDao.addProduct(newProduct);
        }else{
            if(product.getThreshold()!=thresholdQty) {
                product.setThreshold(thresholdQty);
                prodDao.updateProduct(product);
            }
        }
        for(int i=0;i<quantity;i++) {
            int itemId = itemDao.generateItemIdForProduct(productName);
            if (itemId != -1) {
                Item itm = new Item(categoryName, productName, itemId);
                itm.setPrice(price);
                if (expiryDate != null)
                    itm.setExpiryDate(new java.sql.Date(expiryDate.getTime()));
                itemDao.addItem(itm);
            } else {
                Toast.makeText(context, "Error in generating the Item ID!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
