package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.AddItemDialog;
import sg.edu.nus.iss.smartpantry.application.util.CustomAdapter;
import sg.edu.nus.iss.smartpantry.controller.MainController;


public class HomePageFragment extends Fragment {
    private MainController mainController;
    List<Product> productList = new ArrayList<Product>();
    List<Item> prodItemList = new ArrayList<Item>();
    List<List<Item>> itemList = new ArrayList<List<Item>>();
    ExpandableListView expListView;
    CustomAdapter customAdapter;
    private static final int CAMERA_REQUEST = 1888;
    int lastExpandedGroupPosition;
    public HomePageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        lastExpandedGroupPosition=-1;


        //Expandable List View
        expListView = (ExpandableListView) view.findViewById(android.R.id.list);
        customAdapter = new CustomAdapter(getActivity(),productList,itemList,getActivity().getApplicationContext());
        expListView.setAdapter(customAdapter);
        //Create the list view for products
        customAdapter.refreshData();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (groupPosition != lastExpandedGroupPosition)
                    expListView.collapseGroup(lastExpandedGroupPosition);
                lastExpandedGroupPosition = groupPosition;
            }
        });

        registerForContextMenu(expListView);
        return view;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info=
                (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        int groupPos =ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int menuItemIndex = item.getItemId();
        //String[] menuItems = getResources().getStringArray(R.array.menu);
        //String menuItemName = menuItems[menuItemIndex];
        Product selProd = (Product)expListView.getExpandableListAdapter().getGroup(groupPos);
        boolean flag=true;

        switch (menuItemIndex)
        {
            case 0: AddItemDialog itmDialog = new AddItemDialog(getActivity(),selProd,customAdapter);
                itmDialog.show();
            default: flag=false;
        }
        return flag;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==android.R.id.list) {
            ExpandableListView.ExpandableListContextMenuInfo info=
                    (ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
            int groupPos =ExpandableListView.getPackedPositionGroup(info.packedPosition);
            Product selProd = (Product)expListView.getExpandableListAdapter().getGroup(groupPos);
            menu.setHeaderTitle(selProd.getProductName());
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.refreshData();
    }
}
