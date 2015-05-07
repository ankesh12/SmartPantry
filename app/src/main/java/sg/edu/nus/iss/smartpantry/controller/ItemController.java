package sg.edu.nus.iss.smartpantry.controller;

import android.content.Context;
import android.content.Intent;

import sg.edu.nus.iss.smartpantry.views.AddItem;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class ItemController {
    AddItem sphome = new AddItem();

    public Intent addItem(Context context){
        Intent intent = new Intent(context,AddItem.class);
        return intent;
    }
}
