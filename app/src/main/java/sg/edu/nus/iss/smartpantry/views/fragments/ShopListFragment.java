package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;


public class ShopListFragment extends Fragment {

    ListView listView;
    private ArrayAdapter<ShoppingProduct> listAdapter;
    public static ShopListFragment newInstance(String param1, String param2) {
        ShopListFragment fragment = new ShopListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ShopListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        listView = (ListView) view.findViewById(R.id.display_shop_list);
        //List<Product> productList = DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getYetToBuyProductsInShopLists();
        List<ShoppingProduct> shoppingList = DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getYetToBuyProductsInShopLists();
        for(ShoppingProduct pro: shoppingList){
            System.out.println("From the DB:" + pro.getProduct().getProductName() );
        }
        System.out.println("Display:" + shoppingList.get(0).getProduct().getProductName());
        listAdapter = new ProductArrayAdapter(getActivity().getApplicationContext(),shoppingList);
        listView.setAdapter(listAdapter);
        return view;
    }

    private static class ProductViewHolder{
        private TextView prodTextView ;
        private TextView quantTextText;
        public ProductViewHolder(){}
        /*public ProductViewHolder(TextView textView, CheckBox checkBox){
            this.checkBox = checkBox;
            this.textView = textView;
        }*/
        public ProductViewHolder(TextView prodTextView, TextView quantTextText){
            this.prodTextView = prodTextView;
            this.quantTextText = quantTextText;
        }

        public TextView getProdTextView() {
            return prodTextView;
        }

        public void setProdTextView(TextView prodTextView) {
            this.prodTextView = prodTextView;
        }

        public TextView getQuantTextText() {
            return quantTextText;
        }

        public void setQuantTextText(TextView quantTextText) {
            this.quantTextText = quantTextText;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    private static class ProductArrayAdapter extends ArrayAdapter<ShoppingProduct> {

        private LayoutInflater inflater;

        public ProductArrayAdapter( Context context, List<ShoppingProduct> planetList ) {
            super( context, R.layout.display_shop_list, R.id.display_prod_name, planetList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Planet to display
            //Product productNameList = (Product) this.getItem( position ).getProduct();
            ShoppingProduct shopProdList = (ShoppingProduct) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox ;
            TextView prodTextView ;
            TextView quantTextView;
            // Create a new row view
            convertView = inflater.inflate(R.layout.display_shop_list, null);
            prodTextView = (TextView) convertView.findViewById( R.id.display_prod_name );
            quantTextView = (TextView) convertView.findViewById( R.id.display_quanity);
            /*if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.display_shop_list, null);

                // Find the child views.
                prodTextView = (TextView) convertView.findViewById( R.id.display_prod_name );
                quantTextView = (TextView) convertView.findViewById( R.id.display_quanity);

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new ProductViewHolder(prodTextView,quantTextView) );

                // If CheckBox is toggled, update the planet it is tagged with.
//                checkBox.setOnClickListener( new View.OnClickListener() {
//                    public void onClick(View v) {
//                        CheckBox cb = (CheckBox) v ;
//                        ProductNameList productNameList = (ProductNameList) cb.getTag();
//                        productNameList.setChecked( cb.isChecked() );
//                    }
//                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();
                prodTextView = viewHolder.getProdTextView();
                quantTextView = viewHolder.getQuantTextText();
            }*/

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            //prodTextView.setTag( productNameList );

            // Display planet data
            prodTextView.setText( shopProdList.getProduct().getProductName() );
            quantTextView.setText(String.valueOf(shopProdList.getShopQty()));

            return convertView;
        }

    }

}
