package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.Entity.WatchListProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.WatchListProductComparator;
import sg.edu.nus.iss.smartpantry.application.util.WatchListRecyclerViewAdapter;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;

/**
 * Created by A0134630R on 8/27/2015.
 */
public class WatchListFragment extends Fragment {
    List<Product> productList = new ArrayList<Product>();
    RecyclerView mRecyclerView;
    WatchListRecyclerViewAdapter mAdapter;
    private boolean chkBoxVisible;
    private int addToCartBtnId;
    private String btnLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chkBoxVisible=getArguments().getBoolean("chkBxVisible", false);
        this.addToCartBtnId=getArguments().getInt("addToCartBtnId", R.mipmap.cart_icon);
        this.btnLabel=null;
    }

    public static WatchListFragment newInstance(boolean chkBoxVisible,int addToCartBtnId) {
        WatchListFragment fragment = new WatchListFragment();
        Bundle args = new Bundle();
        args.putBoolean("chkBxVisible", chkBoxVisible);
        args.putInt("addToCartBtnId", addToCartBtnId);
        fragment.setArguments(args);
        return fragment;
    }

//    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist_container, container, false);
        //View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        //lastExpandedGroupPosition=-1;
        mRecyclerView = (RecyclerView)view.findViewById(R.id.watchlist_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new WatchListRecyclerViewAdapter(getFragmentManager(), getWatchProdList(), R.layout.fragment_watchlist, getActivity().getApplicationContext(),chkBoxVisible,null,view);
        mRecyclerView.setAdapter(mAdapter);

        final ImageButton backBtn = (ImageButton)view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        final ImageButton addToCart = (ImageButton) view.findViewById(R.id.addToCart);
        final Button selectAllBtn = (Button)view.findViewById(R.id.selectAll_Btn);
        final View actionStrip = view;
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnLabel != null && selectAllBtn.getText().toString().equals("Select All")) {
                    btnLabel = "Deselect All";
                    mAdapter = new WatchListRecyclerViewAdapter(getFragmentManager(), getWatchProdList(), R.layout.fragment_watchlist, getActivity().getApplicationContext(), chkBoxVisible, true, actionStrip);
                    mRecyclerView.setAdapter(mAdapter);
                    addToCart.setImageResource(R.mipmap.done_icon);
                    addToCart.setEnabled(true);
                } else if (btnLabel != null && selectAllBtn.getText().toString().equals("Deselect All")) {
                    btnLabel = "Select All";
                    mAdapter = new WatchListRecyclerViewAdapter(getFragmentManager(), getWatchProdList(), R.layout.fragment_watchlist, getActivity().getApplicationContext(), chkBoxVisible, false, actionStrip);
                    mRecyclerView.setAdapter(mAdapter);
                    addToCart.setImageResource(0);
                    addToCart.setEnabled(false);
                }

                selectAllBtn.setText(btnLabel);
                mAdapter.notifyDataSetChanged();
            }
        });

        if(addToCartBtnId == R.mipmap.cart_icon) {
            selectAllBtn.setText(null);
            btnLabel=null;
            addToCart.setImageResource(addToCartBtnId);
        }
        else if(addToCartBtnId == R.mipmap.done_icon) {
            selectAllBtn.setText("Select All");
            btnLabel="Select All";
            addToCart.setImageResource(-1);
            addToCart.setEnabled(false);
        }

//        addToCart.setImageResource(addToCartBtnId);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCartBtnId == R.mipmap.cart_icon) {
                    selectAllBtn.setText("Select All");
                    addToCart.setImageResource(0);
                    addToCartBtnId = R.mipmap.done_icon;
                    WatchListFragment watchListFragment = WatchListFragment.newInstance(true, addToCartBtnId);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.shop_container, watchListFragment, "WatchListWithCheckBox");
                    //fragmentTransaction.addToBackStack("WatchListWithCheckBox");
                    fragmentTransaction.commit();
                } else if (addToCartBtnId == R.mipmap.done_icon) {

                    //Add to shopping list logic
                    for(WatchListProduct wprod: mAdapter.getSelectedList())
                        DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).addProductToShopList("ShopList",wprod.getProd(),1,false);
                    mAdapter.notifyDataSetChanged();

                    ShopListFragment shopListFragment= new ShopListFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.shop_container, shopListFragment);
                    //fragmentTransaction.addToBackStack("WatchListWithoutCheckBox");
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    public List<WatchListProduct> getWatchProdList()
    {
        ProductDao prodDao = DAOFactory.getProductDao(getActivity().getApplicationContext());
        List<Product> thresholdList = prodDao.getProductsNearingThreshold();
        List<Product> expiryList = prodDao.getProductsNearingExpiry();
        productList.addAll(thresholdList);
        productList.addAll(expiryList);
        productList=new ArrayList<Product>(new HashSet<Product>(productList));
        ArrayList<WatchListProduct> watchProdList= new ArrayList<WatchListProduct>();
        for(Product p: productList) {
            WatchListProduct currProd = new WatchListProduct(p);
            currProd.setIsPresentInShoppingList(DAOFactory.getShopLitstDao(getActivity()).isProductInShopList("ShopList",p));
            watchProdList.add(currProd);
        }
        Collections.sort(watchProdList, new WatchListProductComparator());
        return  watchProdList;
    }
}
