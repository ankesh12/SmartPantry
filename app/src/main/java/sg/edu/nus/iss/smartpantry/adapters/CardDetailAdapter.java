package sg.edu.nus.iss.smartpantry.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.ItemController;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.views.Dialog.EditItemDialog;

/**
 * Created by ankesh on 8/16/15.
 */
public class CardDetailAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private String productName;
    private String category;
    //private String[] items;
    private Context context;
    private Activity actContext;
    private View view;
    private Product product;
    private ItemController itemController;
    int selectedRow;

    public CardDetailAdapter(Activity context, int resource, ArrayList<Item> items, Product product, View view) {
        super(context, resource);
        this.actContext=context;
        this.items = items;
        this.context = context.getApplicationContext();
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
        final View mView = convertView;
        final CardDetailAdapter currAdapter =this;
        final Item passItem = items.get(position);
        System.out.println("#####################################");
        System.out.println("Card Adapter");
        System.out.println("price: "+String.valueOf(passItem.getPrice()));
        System.out.println("expDate: "+String.valueOf(passItem.getExpiryDate()));
        System.out.println("dop: "+String.valueOf(passItem.getDop()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialog editItemDialog = new EditItemDialog(actContext,passItem,currAdapter);
                editItemDialog.show();
            }
        });
        TextView dop = (TextView)convertView.findViewById(R.id.dop);
        TextView expiryDate = (TextView)convertView.findViewById(R.id.expiryDate);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        ImageView deleteItem =(ImageView) convertView.findViewById(R.id.deleteIcon);

        //dop.setText(items[position]);
        dop.setText("Date Of Purchase: " + items.get(position).getDop());
        System.out.println("Date Of Purchase: " + items.get(position).getDop());
        System.out.println("DateOfPurchase: " + items.get(position).getDop());

        final Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //CardDetailAdapter.this.remove(CardDetailAdapter.this.getItem(selectedRow));
                //CardDetailAdapter.this.notifyDataSetChanged();
                refreshData();
            }
        });

        itemController = ControlFactory.getInstance().getItemController();
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemController.deleteItem(context,items.get(position));
                mView.startAnimation(animation);
                selectedRow = position;
                Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                //refreshData();
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
        //items = (ArrayList<Item>) itemDao.getItemsByProductAndCategoryName(product.getCategory().getCategoryName(),product.getProductName());
        items = (ArrayList<Item>) itemDao.getItemsByProductId(product.getProductId());
        CardDetailAdapter.this.notifyDataSetChanged();
        TextView qty = (TextView) view.findViewById(R.id.quant_card);
        qty.setText(String.valueOf(items.size()));
    }
}
