package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.CardDetailAdapter;
import sg.edu.nus.iss.smartpantry.views.Dialog.NewAddItemDialog;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.views.Dialog.EditProductDialog;


public class CardDetailFragment extends Fragment {
    // variables to be used
    TextView prodname;
    FloatingActionButton myFab;
    TextView categ;
    TextView qtycard;
    TextView thresh;
    ImageButton imageButton;
    View view;
    ProductDao productDao;
    CardDetailAdapter cardAdapter;


    // TODO: Rename and change types and number of parameters
    public static CardDetailFragment newInstance(String param1, String param2) {
        CardDetailFragment fragment = new CardDetailFragment();
        return fragment;
    }

    public CardDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card_detail, container, false);
        final ListView cardDetails = (ListView) view.findViewById(R.id.cardItemDetails);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_card);
        imageButton = (ImageButton) view.findViewById(R.id.menu_launcher);
        prodname = (TextView) view.findViewById(R.id.Itemname_card);
        categ = (TextView) view.findViewById(R.id.category_card);
        qtycard = (TextView) view.findViewById(R.id.quant_card);
        thresh = (TextView) view.findViewById(R.id.threshold_card);

        Bundle b = getArguments();
        ArrayList<String> detail = b.getStringArrayList("Details");
        final String productName = detail.get(0);
        productDao = DAOFactory.getProductDao(getActivity().getApplicationContext());

        String category = detail.get(1);
        System.out.println("CategoryName: " + detail.get(1));
        final Product product = productDao.getProductByCategoryNameAndProdName(category, productName);
        imageView.setImageBitmap(product.getProdImage());
        prodname.setText(productName);
        categ.setText(category);
        qtycard.setText(String.valueOf(product.getQuantity()));
        thresh.setText(String.valueOf(product.getThreshold()));

        //FLoating Action Button
        myFab = (FloatingActionButton) view.findViewById(R.id.fab);

        final ItemDao itemDao = DAOFactory.getItemDao(getActivity().getApplicationContext());
        final ArrayList<Item> items = (ArrayList<Item>) itemDao.getItemsByProductAndCategoryName(detail.get(1),detail.get(0));

        cardAdapter = new CardDetailAdapter(getActivity(),R.layout.itemlist, items,product,view);

        //ImageButton
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup(product,cardAdapter);
            }
        });

        //OnCLickListener for FAB
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewAddItemDialog itemDialog = new NewAddItemDialog(getActivity(),product,cardAdapter);
                itemDialog.show();
            }
        });
        cardDetails.setAdapter(cardAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void refreshData(String prodName, String catName){
        thresh = (TextView) view.findViewById(R.id.threshold_card);
        Product prod = DAOFactory.getProductDao(getActivity()).getProductByCategoryNameAndProdName(prodName,catName);
        thresh.setText(String.valueOf(prod.getThreshold()));

    }

    public void popup(final Product product, final CardDetailAdapter cardAdapter){
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(),imageButton);
        popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Edit Product")){
                    EditProductDialog itemDialog = new EditProductDialog(getActivity(),product,cardAdapter,view);
                    itemDialog.show();
                }
                if(item.getTitle().equals("Delete Product")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Deleting Product would delete the items for the Product as well. Would you like to Delete?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//
                            productDao.deleteProduct(product);
                            Toast.makeText(getActivity(), "Deleted Product", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();

                }
                //Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        popup.show();
    }
}
