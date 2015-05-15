package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.CategoryDao;

/**
 * Created by A0134493A on 15/5/2015.
 */
public class CustomAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> productList;
    Context context;

    public CustomAdapter(Activity activity, List<Product> products, Context context){
        this.activity = activity;
        productList = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.browselist, null);
        TextView itemName = (TextView)convertView.findViewById(R.id.Itemname);
        TextView category = (TextView)convertView.findViewById(R.id.category);
        //TextView quantity = (TextView)convertView.findViewById(R.id.quantity);

        Product product = productList.get(i);
        CategoryDao catDao = DAOFactory.getCategoryDao(context);

        itemName.setText(product.getProductName());
        category.setText(catDao.getCategoryById(product.getCategoryId()).getName());
        //quantity.setText(product.getQuantity());

        return convertView;
    }
}
