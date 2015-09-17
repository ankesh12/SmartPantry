package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;

/**
 * Created by A0134493A on 10/9/2015.
 */
public class ShopListRecyclerAdapter extends  RecyclerView.Adapter<ShopListRecyclerAdapter.ViewHolder1> {

    ArrayList<ShoppingProduct> shopProducts;
    Activity context;
    Boolean delBtnClicked;

    public ShopListRecyclerAdapter(ArrayList<ShoppingProduct> shopProducts, Activity context, boolean delBtnClicked) {
        this.shopProducts = shopProducts;
        this.context = context;
        this.delBtnClicked = delBtnClicked;
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shoplist_card, parent,false);
        ViewHolder1 viewHolder = new ViewHolder1(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder1 holder, int position) {
        final ShoppingProduct shopProduct = shopProducts.get(position);
        final Product product = shopProduct.getProduct();
        holder.imageView.setImageBitmap(product.getProdImage());
        holder.textView.setText(product.getProductName().toString());
        //holder.cardCategory.setText(product.getCategoryName().toString());

        if(shopProduct.getIsPurchased()){
            holder.chckBox.setChecked(true);
            Toast.makeText(context, " True", Toast.LENGTH_SHORT).show();
        }

        holder.chckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context, product.getProductName() + " Checked", Toast.LENGTH_SHORT).show();
                    //boolean result = DAOFactory.getShopLitstDao(context.getApplicationContext()).updateProductInShopList("ShopList",product,shopProduct.getShopQty(),true);
                    updateData(shopProduct,shopProduct.getShopQty(),true);
                    shopProduct.setIsPurchased(true);
                    //refreshData();
                }
                else{
                    Toast.makeText(context, "UnChecked", Toast.LENGTH_SHORT).show();
                    //boolean result = DAOFactory.getShopLitstDao(context.getApplicationContext()).updateProductInShopList("ShopList",product,shopProduct.getShopQty(),false);
                    updateData(shopProduct,shopProduct.getShopQty(),false);
                    shopProduct.setIsPurchased(false);
                    //refreshData();
                }
            }
        });

        if(delBtnClicked){
            holder.delBtn.setVisibility(View.VISIBLE);
            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DAOFactory.getShopLitstDao(context).deleteProductFromShopList("ShopList",product);
                    refreshData();
                }
            });
        }
        else{
            holder.delBtn.setVisibility(View.INVISIBLE);
        }
        holder.cardProdQty.setText(String.valueOf(shopProduct.getShopQty()));
        holder.cardProdQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValueFromNumPicker(shopProduct,"Quantity", holder.cardProdQty);

            }

        });
    }

    @Override
    public int getItemCount() {
        return shopProducts.size();
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected ImageView imageView;
        protected EditText cardProdQty;
        protected TextView cardCategory;
        protected ImageButton delBtn;
        protected CheckBox chckBox;
        //protected Button cardButton;
        public ViewHolder1(View itemView) {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.shopList_prodname);
            imageView = (ImageView) itemView.findViewById((R.id.shop_card_image));
            cardProdQty = (EditText) itemView.findViewById(R.id.shoplist_qty_value);
            //cardCategory = (TextView) itemView.findViewById(R.id.shoplist_category);
            delBtn = (ImageButton) itemView.findViewById(R.id.shop_del_btn);
            chckBox = (CheckBox) itemView.findViewById(R.id.shopped_chck);

        }
    }
    public void setValueFromNumPicker(final ShoppingProduct prod,String Title,EditText editObj){
        final EditText editText = editObj;
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog);
        if(Title.equals("Quantity")){
            TextView header = (TextView)d.findViewById(R.id.header);
            header.setText("Quantity");
        }
        Button b1 = (Button) d.findViewById(R.id.setBtn);
        Button b2 = (Button) d.findViewById(R.id.cancelBtn);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(String.valueOf(np.getValue()));
                d.dismiss();
                updateData(prod, np.getValue(),prod.getIsPurchased());

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    public void updateData(ShoppingProduct shopProd, int newQty, boolean isPurchased){
        DAOFactory.getShopLitstDao(context).updateProductInShopList("ShopList", shopProd.getProduct(), newQty, isPurchased);
    }

    public void refreshData(){

        shopProducts= (ArrayList)DAOFactory.getShopLitstDao(context.getApplicationContext()).getProductsByShopListName("ShopList");
        ShopListRecyclerAdapter.this.notifyDataSetChanged();

    }

}
