package sg.edu.nus.iss.smartpantry.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.views.fragments.AddItemConfirm;

public class ItemDetails extends ActionBarActivity implements AddItemConfirm.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle b = getIntent().getExtras();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddItemConfirm addItemConfirm = new AddItemConfirm();
        addItemConfirm.setArguments(b);
        fragmentTransaction.add(R.id.fragment_container, addItemConfirm, "AddItemFrag");
        fragmentTransaction.commit();
        setActivityBackgroundColor("#084B8A");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_details, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println("Call back");
    }

    public void setActivityBackgroundColor(String color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor(color));
    }
}
