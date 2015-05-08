package sg.edu.nus.iss.smartpantry.application;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;


public class SPApp extends ActionBarActivity {
    private ControlFactory controlFactory;
    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spapp);
        setActivityBackgroundColor("#084B8A");
        //Get objects for controller
        controlFactory = new ControlFactory();
        mainController = controlFactory.getMainController();
//        mainController = new MainController();

        Button addItemBtn = (Button) findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainController mainController = controlFactory.getMainController();
                mainController.addItem(SPApp.this);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spapp, menu);

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
    public void setActivityBackgroundColor(String color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor(color));
    }
}
