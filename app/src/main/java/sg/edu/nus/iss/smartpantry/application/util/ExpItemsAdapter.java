package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by A0134435L on 6/4/2015.
 */
public class ExpItemsAdapter extends BaseExpandableListAdapter {
    @Override
    public int getGroupCount() {
        return productList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(itemList.get(groupPosition) == null)
            return 0;
        else
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
        TextView threshQty = (TextView)convertView.findViewById(R.id.threshold);
        TextView category = (TextView)convertView.findViewById(R.id.category);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.icon);
        TextView qtytext = (TextView)convertView.findViewById(R.id.qtyText);
        TextView threshText = (TextView)convertView.findViewById(R.id.threshText);
        TextView quantity = (TextView)convertView.findViewById(R.id.quant);
        Product product = productList.get(groupPosition);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);
        qtytext.setText("Exp Qty : ");
        threshQty.setVisibility(View.INVISIBLE);
        threshText.setVisibility(View.INVISIBLE);

        threshQty.setText(String.valueOf( product.getThreshold()));
        itemName.setText(product.getProductName());
        category.setText(catDao.getCategoryByName(product.getCategoryName()).getCategoryName());
        imageView.setImageBitmap(product.getProdImage());
        quantity.setText(String.valueOf(DAOFactory.getItemDao(context).getItemsNearingExpiryByProduct(product).size()));
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater itemInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = itemInflater.inflate(R.layout.expitemslist, null);
        }

        TextView dop = (TextView)convertView.findViewById(R.id.dop);
        TextView expiryDate = (TextView)convertView.findViewById(R.id.expiryDate);

        final Item selItem = itemList.get(groupPosition).get(childPosition);

        dop.setText("Date Of Purchase: "+selItem.getDop());

        if(selItem.getExpiryDate() == null)
            expiryDate.setText(null);
        else
            expiryDate.setText("Expiry Date: "+selItem.getExpiryDate().toString());

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

    public ExpItemsAdapter(Activity activity, List<Product> products,List<List<Item>> items, Context context){
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
        productList = productDao.getProductsNearingExpiry();
        itemList = new ArrayList<List<Item>>();
        for(Product p:productList)
        {
            List<Item> prodItemList = new ArrayList<Item>();
            prodItemList= itemDao.getItemsNearingExpiryByProduct(p);
            if(prodItemList.size() == 0)
                itemList.add(null);
            else
                itemList.add(prodItemList);

        }
        ExpItemsAdapter.this.notifyDataSetChanged();
    }

}
