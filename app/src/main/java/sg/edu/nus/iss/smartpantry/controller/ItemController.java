package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import sg.edu.nus.iss.smartpantry.Entity.Category;
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

    public void addItem(Context context,  Category category, Product product, Item item){
        ItemDao itemDao= DAOFactory.getItemDao(context);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);
        ProductDao prodDao = DAOFactory.getProductDao(context);
        System.out.println("........Creating Objects....");

//        Category cat = new Category();
//        cat.setCategoryId("CLO");
//        cat.setName("CLOTHING");
//        Product p = new Product("CLO","PUM");
//        p.setProductName("PUMA SHIRT");
//        p.setQuantity(5);
//        p.setThreshold(2);
//        Item i = new Item("PUM",1);
//        i.setPrice(500.00);

        catDao.addCategory(category);
        prodDao.addProduct(product);
        itemDao.addItem(item);

        Toast.makeText(context,product.getProductName(),Toast.LENGTH_SHORT).show();

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
