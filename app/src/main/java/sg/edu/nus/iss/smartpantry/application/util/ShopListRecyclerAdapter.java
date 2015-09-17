package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
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
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           /* holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());

            holder.itemView.buildDrawingCache(true);
            Bitmap bmap = Bitmap.createBitmap(holder.itemView.getDrawingCache());
            holder.itemView.setDrawingCacheEnabled(false);
            Drawable d = new BitmapDrawable(bmap);
            Resources r = context.getResources();
            Drawable[] layers = new Drawable[2];
            layers[0] = d;//new BitmapDrawable(context.getResources(),prod.getProdImage());
            layers[1] = r.getDrawable(R.mipmap.added_to_cart);
            layers[0].setAlpha(75);
            layers[1].setAlpha(255);
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            holder.itemView.setBackground(layerDrawable);
            //holder.itemView.setAlpha((float) 0.5);
            holder.itemView.invalidate();*/

            Resources r = context.getResources();
            Drawable[] layers = new Drawable[2];
            layers[0] = new BitmapDrawable(context.getResources(),product.getProdImage());
            Drawable dr = r.getDrawable(R.mipmap.bought_icon);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
            Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

            layers[1] = r.getDrawable(R.mipmap.bought_icon);
            layers[0].setAlpha(100);
            layers[1].setAlpha(150);
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            holder.imageView.setImageDrawable(layerDrawable);
            //holder.itemView.setAlpha((float) 0.5);
            holder.itemView.invalidate();
        }

        holder.chckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(context, product.getProductName() + " Checked", Toast.LENGTH_SHORT).show();
                    Resources r = context.getResources();
                    Drawable[] layers = new Drawable[2];
                    layers[0] = new BitmapDrawable(context.getResources(),product.getProdImage());
                    Drawable dr = r.getDrawable(R.mipmap.bought_icon);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

                    layers[1] = r.getDrawable(R.mipmap.bought_icon);
                    layers[0].setAlpha(100);
                    layers[1].setAlpha(150);
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    holder.imageView.setImageDrawable(layerDrawable);
                    //holder.itemView.setAlpha((float) 0.5);
                    holder.itemView.invalidate();
                    updateData(shopProduct, shopProduct.getShopQty(), true);
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    shopProduct.setIsPurchased(true);
                    //refreshData();
                }
                else{
                    //Toast.makeText(context, "UnChecked", Toast.LENGTH_SHORT).show();
                    holder.imageView.setImageBitmap(product.getProdImage());
                    updateData(shopProduct, shopProduct.getShopQty(), false);
                    shopProduct.setIsPurchased(false);
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
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
