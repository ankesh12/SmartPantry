package sg.edu.nus.iss.smartpantry.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.XMLUtil;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.views.fragments.CardDetailFragment;

/**
 * Created by A0134493A on 13/8/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] dataSource;
    private List<Product> products;
    //private List<Item> items;
    Activity context;
    //public RecyclerAdapter(List<Product> products, List<Item> itemList,Activity context){
    public RecyclerAdapter(List<Product> products, Activity context){
        this.products = products;
        //this.items = itemList;
        this.context = context;
        //dataSource = dataArgs;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cardesign, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product product = products.get(position);


        String productName = product.getProductName();
        if(productName.length() > 15){
            productName = productName.substring(0,15);
            productName = productName.concat("...");
        }
        holder.textView.setText(productName);
        holder.imageView.setImageBitmap(product.getProdImage());
        holder.cardProdQty.setText(String.valueOf(product.getQuantity()));

        //Click feedback for the card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context.getApplicationContext(),product.getProductName().toString(),Toast.LENGTH_SHORT).show();
                //Calling the Details Fragment
                CardDetailFragment cardHomeFragment= new CardDetailFragment();
                //Creating bundle to pass on the product details to the card detail fragment
                Bundle args = new Bundle();
                ArrayList<String> detail = new ArrayList<String>();
                detail.add(product.getProductName());
                detail.add(product.getCategoryName());
                args.putStringArrayList("Details",detail);

                cardHomeFragment.setArguments(args);
                FragmentManager fragmentManager = context.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                fragmentTransaction.replace(R.id.bluescan, cardHomeFragment, "MatDesignCDetail");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //holder.cardProdQty.setText(product.getCategoryName());
        //Click feedback for the Add-to-Cart Button
        holder.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shop_list_name= new XMLUtil().getElementText("SHOP_LIST_NAME", context.getResources().openRawResource(R.raw.app_settings));
                DAOFactory.getShopLitstDao(context.getApplicationContext()).addProductToShopList(shop_list_name,product,1,false);
                Toast.makeText(context,product.getProductName() + " added to cart!",Toast.LENGTH_SHORT).show();
                //Toast.makeText(context.getApplicationContext(), product.getProductName().toString(), Toast.LENGTH_SHORT).show();

            }
//
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected ImageView imageView;
        protected TextView cardProdQtyText;
        protected TextView cardProdQty;
        //protected ImageView cardButton;
        protected Button cardButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.Itemname);
            imageView = (ImageView) itemView.findViewById((R.id.icon));
            cardProdQtyText = (TextView) itemView.findViewById(R.id.qtyText);
            cardProdQty = (TextView) itemView.findViewById(R.id.quant);
            cardButton = (Button) itemView.findViewById(R.id.consume);

        }
    }

    public void refreshData(){

        ProductDao productDao = DAOFactory.getProductDao(context.getApplicationContext());
        products = productDao.getAllProducts();
        RecyclerAdapter.this.notifyDataSetChanged();
    }
}
