package sg.edu.nus.iss.smartpantry.application.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by A0134493A on 4/6/2015.
 */
public class SHopListArrayAdapter extends ArrayAdapter{
    public SHopListArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
