package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.Entity.ShoppingProduct;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.ShopListRecyclerAdapter;
import sg.edu.nus.iss.smartpantry.application.util.XMLUtil;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;


public class ShopListFragment extends Fragment {

    private RecyclerView recyclerViewShop;
    private RecyclerView.LayoutManager layoutManager;
    private ShopListRecyclerAdapter adapter;
    ArrayList<ShoppingProduct> shopProds;
    Button changeable;
    ImageButton del_btn;
    private ImageButton shop_bck_btn;
    private String shop_list_name;

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

        this.shop_list_name= new XMLUtil().getElementText("SHOP_LIST_NAME", getActivity().getResources().openRawResource(R.raw.app_settings));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        shopProds = (ArrayList)DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getProductsByShopListName(shop_list_name);
        changeable = (Button) view.findViewById(R.id.complete_btn);
        del_btn = (ImageButton) view.findViewById(R.id.del_launcher);
        shop_bck_btn = (ImageButton) view.findViewById(R.id.shopList_back_btn);
        changeable.setText("Shopping List");

        recyclerViewShop = (RecyclerView) view.findViewById(R.id.shoplist_recycler_view);
        recyclerViewShop.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewShop.setLayoutManager(layoutManager);
        adapter = new ShopListRecyclerAdapter(shopProds,getActivity(),false);
        recyclerViewShop.setAdapter(adapter);

        shop_bck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProds = (ArrayList)DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getProductsByShopListName(shop_list_name);
                adapter = new ShopListRecyclerAdapter(shopProds, getActivity(), true);
                recyclerViewShop.setAdapter(adapter);
                changeable.setText("Done");
            }
        });

        changeable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeable.getText().toString().equals("Done")) {
                    shopProds = (ArrayList) DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getProductsByShopListName(shop_list_name);
                    adapter = new ShopListRecyclerAdapter(shopProds, getActivity(), false);
                    recyclerViewShop.setAdapter(adapter);
                    changeable.setText("Shopping List");
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

}
