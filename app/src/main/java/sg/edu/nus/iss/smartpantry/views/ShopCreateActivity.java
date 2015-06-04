package sg.edu.nus.iss.smartpantry.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.views.fragments.ShopListCreateFragment;

public class ShopCreateActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_create);
        if (savedInstanceState == null) {
            ShopListCreateFragment shopListCreateFragment = new ShopListCreateFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //AddItemConfirm addItemConfirm = new AddItemConfirm();
            //addItemConfirm.setArguments(b);
            fragmentTransaction.add(android.R.id.content, shopListCreateFragment, "ShopList");
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_create, menu);
        return true;
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
}
