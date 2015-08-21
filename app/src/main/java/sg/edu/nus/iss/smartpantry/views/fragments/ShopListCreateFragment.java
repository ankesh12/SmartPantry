package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;

/*
 */
public class ShopListCreateFragment extends Fragment {
    private ListView mainListView ;
    private Button createShpBtn;
    //private Planet[] planets ;
    private ProductNameList[] products;
    //private ArrayAdapter<Planet> listAdapter ;
    private ArrayAdapter<ProductNameList> listAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopListCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopListCreateFragment newInstance(String param1, String param2) {
        ShopListCreateFragment fragment = new ShopListCreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShopListCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_list_create, container, false);
        // Inflate the layout for this fragment
        // Find the ListView resource.
        mainListView = (ListView) view.findViewById( R.id.shopCreateList );

        // When item is tapped, toggle checked properties of CheckBox and Planet.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                ProductNameList productNameList = listAdapter.getItem( position );
                productNameList.toggleChecked();
                ProductViewHolder viewHolder = (ProductViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked( productNameList.isChecked() );
            }
        });

        // Create and populate planets.
        //planets = (Planet[])  ;
        /*if ( planets == null ) {
            planets = new Planet[] {
                    new Planet("Mercury"), new Planet("Venus"), new Planet("Earth"),
                    new Planet("Mars"), new Planet("Jupiter"), new Planet("Saturn"),
                    new Planet("Uranus"), new Planet("Neptune"), new Planet("Ceres"),
                    new Planet("Pluto"), new Planet("Haumea"), new Planet("Makemake"),
                    new Planet("Eris")
            };
        }*/
        //final List<Product> yetToBuyProd;
        final List<ShoppingProduct> yetToBuyShopProd;
        yetToBuyShopProd = DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getYetToBuyProductsInShopLists();
        //yetToBuyProd = DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getYetToBuyProductsInShopLists();
        final ArrayList<ProductNameList> productList = new ArrayList<ProductNameList>();
        List<Product> prodList;
        //Get Expiring Item Product Names
        prodList = DAOFactory.getProductDao(getActivity().getApplicationContext()).getProductsNearingExpiry();
        for(Product product: prodList){
            if(!yetToBuyShopProd.contains(new ShoppingProduct(product,0,false))) {
                productList.add(new ProductNameList(product.getProductName(), product.getCategoryName(), product.getThreshold(),false));
            }
        }
        //Get Below Threshold Product Names
        prodList = DAOFactory.getProductDao(getActivity().getApplicationContext()).getProductBelowThreshold();
        for(Product product: prodList){
            ProductNameList checkProd = new ProductNameList(product.getProductName(),product.getCategoryName(),product.getThreshold(),true);
            if(!yetToBuyShopProd.contains(new ShoppingProduct(product,0,false))) {
                if (!productList.contains(checkProd)) {
                    System.out.println("Hai be ");
                    productList.add(new ProductNameList(product.getProductName(), product.getCategoryName(), product.getThreshold(),true));
                }
            }
        }
        /*ArrayList<ProductNameList> productList = new ArrayList<ProductNameList>();
        productList.addAll( Arrays.asList(products) );*/

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new ProductArrayAdapter(getActivity(), productList);
        mainListView.setAdapter( listAdapter );
        mainListView.invalidateViews();
        listAdapter.notifyDataSetChanged();
        //mainListView.not

        //Logic for creating shopping list
        createShpBtn = (Button) view.findViewById(R.id.createShpBtn);
        createShpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ProductNameList productNameList : productList){
                    if(productNameList.isChecked()){
                        Product product = DAOFactory.getProductDao(getActivity().getApplicationContext()).getProduct(productNameList.getCatName(),productNameList.getName());
                        if(!yetToBuyShopProd.contains(new ShoppingProduct(product,0,false))) {
                            System.out.println("This item is checked: " + productNameList.getName() + " " + productNameList.getQuantity());
                            DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).addProductToShopList("ShopList1", product, productNameList.getQuantity(), false);
                            System.out.println("This item is checked: " + productNameList.getName());
                        }
                    }
                }
                ShopListFragment shopListFragment = new ShopListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, shopListFragment, "ShopListPage");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainListView.invalidateViews();
        listAdapter.notifyDataSetChanged();
    }
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

    /** Holds planet data. */
    /*private static class Planet {
        private String name = "" ;
        private boolean checked = false ;
        public Planet() {}
        public Planet( String name ) {
            this.name = name ;
        }
        public Planet( String name, boolean checked ) {
            this.name = name ;
            this.checked = checked ;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        public String toString() {
            return name ;
        }
        public void toggleChecked() {
            checked = !checked ;
        }
    }*/

    private static class ProductNameList {
        private String name = "" ;
        private boolean checked = false ;
        private String catName;
        private int quantity;
        private boolean flag = true;

        public ProductNameList() {}
        public ProductNameList( String name , String catName , int quantity, boolean flag) {
            this.name = name ;
            this.catName = catName;
            this.quantity = quantity;
            this.flag = flag;
        }
//        public ProductNameList( String name, boolean checked ) {
//            this.name = name ;
//            this.checked = checked ;
//        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        public String toString() {
            return name ;
        }
        public void toggleChecked() {
            checked = !checked ;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + catName.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            ProductNameList product = (ProductNameList) o;
            return (this.name.equalsIgnoreCase(product.getName()) && this.catName.equalsIgnoreCase(product.getCatName()));

        }
    }

    /** Holds child views for one row. */
    /*private static class PlanetViewHolder {
        private CheckBox checkBox ;
        private TextView textView ;
        public PlanetViewHolder() {}
        public PlanetViewHolder( TextView textView, CheckBox checkBox ) {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }*/

    private static class ProductViewHolder{
        private CheckBox checkBox ;
        private TextView textView ;
        private EditText editText;
        public ProductViewHolder(){}
        /*public ProductViewHolder(TextView textView, CheckBox checkBox){
            this.checkBox = checkBox;
            this.textView = textView;
        }*/
        public ProductViewHolder(TextView textView, CheckBox checkBox, EditText editText){
            this.checkBox = checkBox;
            this.textView = textView;
            this.editText = editText;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
        public EditText getEditText() {
            return editText;
        }

        public void setEditText(EditText editText) {
            this.editText = editText;
        }

    }
    private static class ProductArrayAdapter extends ArrayAdapter<ProductNameList> {

        private LayoutInflater inflater;

        public ProductArrayAdapter( Context context, List<ProductNameList> planetList ) {
            super( context, R.layout.shop_create_list, R.id.prodName, planetList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Planet to display
            final ProductNameList productNameList = (ProductNameList) this.getItem( position );

            // The child views in each row.
            CheckBox checkBox ;
            final TextView textView ;
            final EditText editText;
            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.shop_create_list, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.prodName );
                checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );
                editText = (EditText) convertView.findViewById( R.id.qty01);

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new ProductViewHolder(textView,checkBox,editText) );

                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        ProductNameList productNameList = (ProductNameList) cb.getTag();
                        productNameList.setChecked( cb.isChecked() );
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
                editText = viewHolder.getEditText() ;
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag( productNameList );

            // Display planet data
            checkBox.setChecked( productNameList.isChecked() );
            textView.setText( productNameList.getName() );
            TextView flag = (TextView)convertView.findViewById(R.id.alertText);
            if(productNameList.flag){

                flag.setText("Below Threshold");
            }
            else{
                flag.setText("Expiring");
            }

            editText.setText(String.valueOf(productNameList.getQuantity()));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    productNameList.setQuantity(Integer.parseInt(editText.getText().toString()));
                }
            });

            return convertView;
        }

    }

    public Object onRetainNonConfigurationInstance() {
        return products ;
    }

}
