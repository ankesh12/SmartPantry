package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.ExpItemsAdapter;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;

public class ExpiringItemFragment extends Fragment {
    private MainController mainController;
    List<Product> productList = new ArrayList<Product>();
    List<Item> prodItemList = new ArrayList<Item>();
    List<List<Item>> itemList = new ArrayList<List<Item>>();
    ExpandableListView expListView;
    ExpItemsAdapter customAdapter;
    int lastExpandedGroupPosition;
    public ExpiringItemFragment() {
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
        View view = inflater.inflate(R.layout.fragment_expiring_item, container, false);
        lastExpandedGroupPosition=-1;

        mainController = ControlFactory.getInstance().getMainController();
        expListView = (ExpandableListView) view.findViewById(android.R.id.list);
        customAdapter = new ExpItemsAdapter(getActivity(),productList,itemList,getActivity().getApplicationContext());
        expListView.setAdapter(customAdapter);
        customAdapter.refreshData();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (groupPosition != lastExpandedGroupPosition)
                    expListView.collapseGroup(lastExpandedGroupPosition);
                lastExpandedGroupPosition = groupPosition;
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


}
