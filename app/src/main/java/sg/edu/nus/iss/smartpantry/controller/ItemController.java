package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.application.util.ScanBarcodeActivity;
import sg.edu.nus.iss.smartpantry.dao.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;
import sg.edu.nus.iss.smartpantry.views.AddItem;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class ItemController {
    AddItem sphome = new AddItem();

    public Intent showAddItem(Context context){
        Intent intent = new Intent(context,ScanBarcodeActivity.class);
        return intent;
    }

    public void addItem(Context context,  String categoryName, String productName, Bitmap bitmap) throws ParseException {
        ItemDao itemDao= DAOFactory.getItemDao(context);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);
        ProductDao prodDao = DAOFactory.getProductDao(context);
        System.out.println("........Creating Objects....");

        //Category category = new Category();
        //category.setName(categoryName);
        boolean isExistingProduct = prodDao.isProductExists(categoryName,productName);
        Random rand = new Random();
        if(isExistingProduct == false)
        {
            Product product = new Product(categoryName,productName);
            product.setThreshold(rand.nextInt(10));
            product.setProdImage(bitmap);
            prodDao.addProduct(product);
        }

        int itemId = itemDao.generateItemIdForProduct(productName);
        if(itemId != -1)
        {
            try {
                Item itm = new Item(categoryName,productName, itemId);

                itm.setPrice(rand.nextInt(100));
                String dateStr = "28/05/2015";
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj = curFormater.parse(dateStr);
                itm.setExpiryDate(new java.sql.Date(dateObj.getTime()));

                itemDao.addItem(itm);
            }
            catch (ParseException p){
                p.printStackTrace();
            }
        }
        else
            Toast.makeText(context,"Error in generating the Item ID!!!!",Toast.LENGTH_SHORT).show();


        //Toast.makeText(context,String.valueOf(itemDao.getItemsByProductName(productName).get(0).getPrice()),Toast.LENGTH_SHORT).show();

    }
//    public void addItem(Context context, Category category, Product product, Item item){
//        ItemDao itemDao= DAOFactory.getItemDao(context);
//        CategoryDao catDao = new CategoryDaoImpl(context);
//        ProductDao prodDao = new ProductDaoImpl(context);
////        ItemDao itmDao = new ItemDaoImpl(context);
//
//        System.out.println("........Creating Objects....");
//
//        Category cat = new Category();
//        cat.setCategoryId("CLO");
//        cat.setName("CLOTHING");
//        Product p = new Product("CLO","PUM");
//        p.setProductName("PUMA SHIRT");
//        p.setQuantity(5);
//        p.setThreshold(2);
//        Item i = new Item("PUM",1);
//        i.setPrice(500.00);
//
//        catDao.addCategory(cat);
//        prodDao.addProduct(p);
//        itemDao.addItem(i);
//        Toast.makeText(context,"Added Product",Toast.LENGTH_SHORT).show();
//
//    }
}
