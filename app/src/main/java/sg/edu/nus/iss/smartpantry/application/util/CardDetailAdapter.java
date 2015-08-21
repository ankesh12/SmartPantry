package sg.edu.nus.iss.smartpantry.application.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;

/**
 * Created by ankesh on 8/16/15.
 */
public class CardDetailAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private String productName;
    private String category;
    //private String[] items;
    private Context context;
    private View view;
    private Product product;

    public CardDetailAdapter(Context context, int resource, ArrayList<Item> items, Product product, View view) {
        super(context, resource);
        this.items = items;
        this.context = context;
        this.product = product;
        this.view = view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater itemInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = itemInflater.inflate(R.layout.itemlist, null);
        }
        TextView dop = (TextView)convertView.findViewById(R.id.dop);
        TextView expiryDate = (TextView)convertView.findViewById(R.id.expiryDate);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        ImageView deleteItem =(ImageView) convertView.findViewById(R.id.deleteIcon);

        //dop.setText(items[position]);
        dop.setText("Date Of Purchase: " + items.get(position).getDop());
        System.out.println("Date Of Purchase: " + items.get(position).getDop());
        System.out.println("DateOfPurchase: " + items.get(position).getDop());

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDao productDao = DAOFactory.getProductDao(context);
                ItemDao itemDao = DAOFactory.getItemDao(context);
                itemDao.deleteItem(items.get(position));
                Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
//
//                itemList.get(groupPosition).remove(childPosition);
//                if (itemList.get(groupPosition).isEmpty())
//                    itemList.set(groupPosition,null);
                refreshData();
            }
        });

        if(items.get(position).getExpiryDate() == null)
            expiryDate.setText(null);
        else
            expiryDate.setText("Expiry Date: "+items.get(position).getExpiryDate().toString());

        if(items.get(position).getPrice() == 0)
            price.setText(null);
        else
            price.setText("Price: "+String.valueOf(items.get(position).getPrice()));


        return convertView;
    }

    public void refreshData(){

        ItemDao itemDao = DAOFactory.getItemDao(context.getApplicationContext());
        items = (ArrayList<Item>) itemDao.getItemsByProductAndCategoryName(product.getCategoryName(),product.getProductName());
        CardDetailAdapter.this.notifyDataSetChanged();
        TextView qty = (TextView) view.findViewById(R.id.quant_card);
        qty.setText(String.valueOf(items.size()));
    }
}
