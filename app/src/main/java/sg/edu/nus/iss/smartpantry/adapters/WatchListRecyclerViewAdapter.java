package sg.edu.nus.iss.smartpantry.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.WatchListProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.views.fragments.CardDetailFragment;

/**
 * Created by A0134630R on 8/14/2015.
 */
public class WatchListRecyclerViewAdapter extends RecyclerView.Adapter<WatchListRecyclerViewAdapter.MainScreenViewHolder>{

    List<WatchListProduct> productList;
    private int rowLayout;
    private Context mContext;
    private FragmentManager fragMgr;
    private boolean chkBxVisible;
    private Boolean selStatus;
    private ArrayList<WatchListProduct> selectedProd;
    private View actionStrip;
    public WatchListRecyclerViewAdapter(FragmentManager fragMgr,List<WatchListProduct> productList, int rowLayout, Context mContext, boolean chkBxVisible, Boolean selStatus, View actionStrip) {
        this.fragMgr=fragMgr;
        this.productList = productList;
        this.rowLayout=rowLayout;
        this.mContext=mContext;
        this.chkBxVisible=chkBxVisible;
        this.selStatus=selStatus;
        this.actionStrip=actionStrip;
        this.selectedProd = new ArrayList<WatchListProduct>();
    }

    @Override
    public MainScreenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(rowLayout, parent, false);
        return new MainScreenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MainScreenViewHolder holder, final int position) {

        holder.vChkBox.setEnabled(false);
        if(getItemCount() > 0)
        {
            final WatchListProduct WatchListProd = productList.get(position);
            final Product prod = WatchListProd.getProd();

            holder.vProdImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.layer));
            if(WatchListProd.isPresentInShoppingList())
            {

                Resources r = mContext.getResources();
                Drawable[] layers = new Drawable[2];
                layers[0] = new BitmapDrawable(mContext.getResources(),prod.getProdImage());
                layers[1] = r.getDrawable(R.mipmap.added_to_cart);
                layers[0].setAlpha(75);
                layers[1].setAlpha(255);
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                holder.vProdImage.setImageDrawable(layerDrawable);
                //holder.itemView.setAlpha((float) 0.5);
                holder.itemView.invalidate();
            }else
                holder.vProdImage.setImageBitmap(prod.getProdImage());
            String productName =prod.getProductName();
            if(productName.length() > 12){
                productName = productName.substring(0,12);
                productName = productName.concat("...");
            }


            holder.vProdName.setText(productName);
            if(prod.getQuantity() <= prod.getThreshold())
            {
                holder.vQtyLbl.setVisibility(View.VISIBLE);
                holder.vQty.setText(String.valueOf(prod.getQuantity()));
                holder.vQty.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.vQtyLbl.setVisibility(View.INVISIBLE);
                holder.vQty.setVisibility(View.INVISIBLE);
                holder.vQty.setText(null);
            }
            if(selStatus != null && selStatus) {
                WatchListProd.setIsSelected(true);
                selectedProd.add(WatchListProd);
            }
            else if(selStatus != null && !selStatus) {
                WatchListProd.setIsSelected(false);
                selectedProd.remove(WatchListProd);
            }

            holder.vChkBox.setChecked(WatchListProd.isSelected());
            final Button selAllBtn = (Button)actionStrip.findViewById(R.id.selectAll_Btn);
            final ImageButton done_icon = (ImageButton)actionStrip.findViewById(R.id.addToCart);
            if (selectedProd.size() == 0 && selAllBtn.getText().toString().equals("Deselect All")) {
                selAllBtn.setText("Select All");
                done_icon.setImageResource(0);
                done_icon.setEnabled(false);
            }
            if(this.chkBxVisible) {
                holder.vChkBox.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.vChkBox.isChecked()) {
                            holder.vChkBox.setChecked(false);
                            selectedProd.remove(WatchListProd);
                            if (selectedProd.size() == 0 && selAllBtn.getText().toString().equals("Deselect All")) {
                                selAllBtn.setText("Select All");
                                //done_icon.setImageDrawable(null);
                                done_icon.setImageResource(0);
                                done_icon.setEnabled(false);
                            }
                        } else {
                            done_icon.setImageResource(R.mipmap.done_icon);
                            done_icon.setEnabled(true);
                            holder.vChkBox.setChecked(true);
                            selectedProd.add(WatchListProd);
                            if (selectedProd.size() == getItemCount() && selAllBtn.getText().toString().equals("Select All"))
                                selAllBtn.setText("Deselect All");
                        }
                    }
                });

            }
            else {
                holder.vChkBox.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> argList = new ArrayList<String>();
                        argList.add(prod.getProductName());
                        argList.add(prod.getCategory().getCategoryName());
                        Bundle b = new Bundle();
                        b.putStringArrayList("Details", argList);
                        CardDetailFragment cardDetailFragment = new CardDetailFragment();
                        cardDetailFragment.setArguments(b);
                        FragmentTransaction fragmentTransaction = fragMgr.beginTransaction();
                        fragmentTransaction.replace(R.id.shop_container, cardDetailFragment, "CardDetail");
                        fragmentTransaction.addToBackStack("CardDetail");
                        fragmentTransaction.commit();
                    }
                });
            }

            ItemDao itmDao = DAOFactory.getItemDao(mContext);
            long expiryDays = -1;
            boolean flag=false;
            for (Item itm:itmDao.getItemsByProductId(prod.getProductId()))
            {
                if(itm.getExpiryDate() == null)
                    continue;
                long diffDays = (long) Math.ceil ((itm.getExpiryDate().getTime() - new Date().getTime())/(1000.0 * 60 * 60 * 24));
//                if(diffDays > 0 && diffDays <= 7 && (expiryDays == -1 || diffDays < expiryDays))
                if(diffDays <= 7 && (expiryDays == -1 || diffDays < expiryDays))
                {
                    expiryDays=diffDays;
                    flag =true;
                }
            }
            if(flag == false)
            {
                holder.vExpiryNotify.setVisibility(View.INVISIBLE);
                holder.vExpiryNotify.setText(null);
            }
            else
            {
                if(expiryDays < 0)
                    holder.vExpiryNotify.setText("Expired");
                else
                    holder.vExpiryNotify.setText(String.valueOf(expiryDays) + " day(s) left");
                holder.vExpiryNotify.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ArrayList<WatchListProduct> getSelectedList() {
        return selectedProd;
    }

    public class MainScreenViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vProdImage;
        protected TextView vProdName;
        protected TextView vQtyLbl;
        protected TextView vQty;
        protected TextView vExpiryNotify;
        protected CheckBox vChkBox;
        protected View itemView;
        public MainScreenViewHolder(View itemView) {
            super(itemView);
            vProdImage = (ImageView)itemView.findViewById(R.id.prodImage);
            vProdName = (TextView)itemView.findViewById(R.id.prodName);
            vQtyLbl = (TextView)itemView.findViewById(R.id.qtyText);
            vQty = (TextView)itemView.findViewById(R.id.quantVal);
            vExpiryNotify = (TextView) itemView.findViewById(R.id.expiryNotify);
            vChkBox = (CheckBox) itemView.findViewById(R.id.isSel);
            this.itemView = itemView;
        }
    }
}
