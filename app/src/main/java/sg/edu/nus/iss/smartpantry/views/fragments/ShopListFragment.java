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
import sg.edu.nus.iss.smartpantry.application.util.ShopListRecyclerAdapter;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;


public class ShopListFragment extends Fragment {

    private RecyclerView recyclerViewShop;
    private RecyclerView.LayoutManager layoutManager;
    private ShopListRecyclerAdapter adapter;
    ArrayList<ShoppingProduct> shopProds;
    Button changeable;
    ImageButton del_btn;

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
        shopProds = (ArrayList)DAOFactory.getShopLitstDao(getActivity().getApplicationContext()).getProductsByShopListName("ShopList");
        changeable = (Button) view.findViewById(R.id.complete_btn);
        del_btn = (ImageButton) view.findViewById(R.id.del_launcher);
        changeable.setText("Shopping List");

        recyclerViewShop = (RecyclerView) view.findViewById(R.id.shoplist_recycler_view);
        recyclerViewShop.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewShop.setLayoutManager(layoutManager);
        adapter = new ShopListRecyclerAdapter(shopProds,getActivity());
        recyclerViewShop.setAdapter(adapter);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

}
