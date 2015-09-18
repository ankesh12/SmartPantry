package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class MainController {

    private ControlFactory controlFactory;
    private Intent intent;
    private Context context;

    public MainController(){

    }

    public void addItem(Context context){
        this.context = context;
        intent = ControlFactory.getInstance().getItemController().showAddItemScreen(context);
        callIntent(intent);
    }


    public void callIntent(Intent intent){
        context.startActivity(intent);
    }
}
