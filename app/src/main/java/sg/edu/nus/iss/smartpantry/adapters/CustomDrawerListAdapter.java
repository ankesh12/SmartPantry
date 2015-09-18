package sg.edu.nus.iss.smartpantry.adapters;

/**
 * Created by sambhav on 9/10/15.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.nus.iss.smartpantry.R;

public class CustomDrawerListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomDrawerListAdapter(Activity context,
                                   String[] web, Integer[] imageId) {
        super(context, R.layout.drawer_list_singleitem, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.drawer_list_singleitem, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.drawerItemText);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.drawerItemImg);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}