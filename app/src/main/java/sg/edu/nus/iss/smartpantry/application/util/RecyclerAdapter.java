package sg.edu.nus.iss.smartpantry.application.util;

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

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;
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

        holder.textView.setText(product.getProductName());
        holder.imageView.setImageBitmap(product.getProdImage());
        holder.cardProdQty.setText("Qty: " + String.valueOf(product.getQuantity()));
        //holder.cardProdQty.setText(product.getCategoryName());
        holder.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailFragment cardHomeFragment= new CardDetailFragment();
                Bundle args = new Bundle();
                ArrayList<String> detail = new ArrayList<String>();
                detail.add(product.getProductName());
                detail.add(product.getCategoryName());
                args.putStringArrayList("Details",detail);
                /*args.putString("Product_Name", product.getProductName());
                args.putString("Category_Name", product.getCategoryName());*/
                cardHomeFragment.setArguments(args);
                FragmentManager fragmentManager = context.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                fragmentTransaction.replace(R.id.bluescan, cardHomeFragment, "MatDesignCDetail");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                /*Toast.makeText(context, product.getProductName().toString(), Toast.LENGTH_SHORT).show();
//                DialogPlus dialog = DialogPlus.newDialog(context)
//
//                        .setContentHolder(new ListHolder())
//                        .setAdapter(adapter)
//                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
//                        .create();
//                dialog.show();
                ItemDao itemDao = DAOFactory.getItemDao(context.getApplicationContext());
                items = itemDao.getItemsByProductAndCategoryName(product.getProductName(),product.getCategoryName());
                String[] dateList = new String[items.size()];
                int i=0;
                for(Item item: items){
                    dateList[i++] = String.valueOf(item.getExpiryDate());
                    System.out.println("Expiry date: " + String.valueOf(item.getExpiryDate()));
                }
                DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dateList))
                        .setCancelable(true)
                        .setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {

                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {

                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {

                            }
                        })
                        .create();

                dialogPlus.show();*/
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
        protected TextView cardProdQty;
        protected Button cardButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.info_text_footer);
            imageView = (ImageView) itemView.findViewById((R.id.info_image));
            cardProdQty = (TextView) itemView.findViewById(R.id.cardQty_text);
            cardButton = (Button) itemView.findViewById(R.id.card_button);

        }
    }

    public void refreshData(){

        ProductDao productDao = DAOFactory.getProductDao(context.getApplicationContext());
        products = productDao.getAllProducts();
        RecyclerAdapter.this.notifyDataSetChanged();
    }
}
