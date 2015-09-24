package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Random;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.application.util.ScanBarcode;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.CategoryDao;
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
        Product product = prodDao.getProductByCategoryNameAndProdName(itemDetailDTO.getCategoryName(), itemDetailDTO.getProductName());
        Random rand = new Random();
        if(product == null)
        {
            product = new Product(catDao.getCategoryByName(itemDetailDTO.getCategoryName()), prodDao.generateProductId(catDao.getCategoryByName(itemDetailDTO.getCategoryName()).getCategoryId()));
            product.setProductName(itemDetailDTO.getProductName());
            product.setThreshold(itemDetailDTO.getThresholdQty());
            product.setProdImage(itemDetailDTO.getBitmap());
            prodDao.addProduct(product);
        }else{
            if(product.getThreshold()!= itemDetailDTO.getThresholdQty()) {
                product.setThreshold(itemDetailDTO.getThresholdQty());
                prodDao.updateProduct(product);
            }
        }
        for(int i=0;i< itemDetailDTO.getQuantity();i++) {
            int itemId = itemDao.generateItemIdForProduct(product.getProductId());
            if (itemId != -1) {
                Item itm = new Item(product, itemId);
                itm.setPrice(itemDetailDTO.getPrice());
                if (itemDetailDTO.getExpiryDate() != null)
                    itm.setExpiryDate(new java.sql.Date(itemDetailDTO.getExpiryDate().getTime()));
                itemDao.addItem(itm);
            } else {
                Toast.makeText(context, "Error in generating the Item ID!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteItem(Context context,Item item)
    {
        ItemDao itemDao = DAOFactory.getItemDao(context);
        itemDao.deleteItem(item);
    }

}
