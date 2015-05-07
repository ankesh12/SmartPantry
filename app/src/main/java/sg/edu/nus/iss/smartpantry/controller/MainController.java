package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class MainController {

    private ControlFactory controlFactory;
    private ItemController itemController;
    private Intent intent;
    private Context context;

    public MainController(){
        controlFactory = new ControlFactory();
        itemController = controlFactory.getItemController();
    }

    public void addItem(Context context){
        this.context = context;
        intent = itemController.addItem(context);
        callIntent(intent);
    }
    public void callIntent(Intent intent){
        context.startActivity(intent);
    }
}
