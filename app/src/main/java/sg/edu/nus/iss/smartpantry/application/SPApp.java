package sg.edu.nus.iss.smartpantry.application;

import android.app.ExpandableListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.CustomAdapter;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;


public class SPApp extends ExpandableListActivity{
    private MainController mainController;
    List<Product> productList = new ArrayList<Product>();
    List<Item> prodItemList = new ArrayList<Item>();
    List<List<Item>> itemList = new ArrayList<List<Item>>();
    ExpandableListView expListView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spapp);
        setActivityBackgroundColor("#455A64");

        /*Category category = new Category();
        category.setCategoryName("Misc");
        DAOFactory.getCategoryDao(this).addCategory(category);*/

        //Get objects for controller
        mainController = ControlFactory.getInstance().getMainController();

        //Add item functionality
        Button addItemBtn = (Button) findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainController mainController = controlFactory.getMainController();
                mainController.addItem(SPApp.this);
            }
        });
        //Expandable List View
        expListView = (ExpandableListView) findViewById(android.R.id.list);
        customAdapter = new CustomAdapter(this,productList,itemList,getApplicationContext());
        expListView.setAdapter(customAdapter);
        //Create the list view for products
        customAdapter.refreshData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spapp, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setActivityBackgroundColor(String color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor(color));
    }
    
}
