package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;

/**
 * Created by A0134493A on 15/5/2015.
 */
public class CustomAdapter extends BaseExpandableListAdapter {
    @Override
    public int getGroupCount() {
        return productList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return productList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater browseInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = browseInflater.inflate(R.layout.browselist, null);
        }

        TextView itemName = (TextView)convertView.findViewById(R.id.Itemname);
        TextView category = (TextView)convertView.findViewById(R.id.category);
        TextView quantity = (TextView)convertView.findViewById(R.id.quant);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.icon);

        Product product = productList.get(groupPosition);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);

        itemName.setText(product.getProductName());
        category.setText(catDao.getCategoryByName(product.getCategoryName()).getCategoryName());
        quantity.setText(String.valueOf(product.getQuantity()));
        imageView.setImageBitmap(product.getProdImage());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater itemInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = itemInflater.inflate(R.layout.itemlist, null);
        }

        TextView expiryDate = (TextView)convertView.findViewById(R.id.expiryDate);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        ImageView deleteItem =(ImageView) convertView.findViewById(R.id.deleteIcon);

        final Item selItem = itemList.get(groupPosition).get(childPosition);

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDao productDao = DAOFactory.getProductDao(context);
                ItemDao itemDao = DAOFactory.getItemDao(context);
                String prodName = selItem.getProductName();
                String categoryName = selItem.getCategoryName();
                itemDao.deleteItem(selItem);
                Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                itemList.get(groupPosition).remove(childPosition);
                if (itemList.get(groupPosition).isEmpty())
                {
                    productDao.deleteProduct(productDao.getProduct(categoryName,prodName));
                    productList.remove(groupPosition);
                    itemList.remove(groupPosition);
                    Toast.makeText(context, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
                }
                refreshData();
            }
        });


        if(selItem.getExpiryDate() == null)
            expiryDate.setText("Expiry Date: None");
        else
            expiryDate.setText("Expiry Date: "+selItem.getExpiryDate().toString());
        price.setText("Price: "+String.valueOf(selItem.getPrice()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> productList;
    private List<List<Item>> itemList;
    Context context;
    int lastExpandedGroupPosition;

    public CustomAdapter(Activity activity, List<Product> products,List<List<Item>> items, Context context){
        this.activity = activity;
        productList = products;
        itemList=items;
        this.context = context;
        lastExpandedGroupPosition=-1;
    }

    public void refreshData()
    {
        ProductDao productDao = DAOFactory.getProductDao(context);
        ItemDao itemDao = DAOFactory.getItemDao(context);
        productList = new ArrayList<Product>();
        productList = productDao.getAllProducts();
        itemList = new ArrayList<List<Item>>();
        for(Product p:productList)
        {
            List<Item> prodItemList = new ArrayList<Item>();
            prodItemList= itemDao.getItemsByProductAndCategoryName(p.getCategoryName(),p
                    .getProductName());
            if(prodItemList.size() > 0)
                itemList.add(prodItemList);

        }
        CustomAdapter.this.notifyDataSetChanged();
        //CustomAdapter.this.notifyDataSetInvalidated();
    }

}
