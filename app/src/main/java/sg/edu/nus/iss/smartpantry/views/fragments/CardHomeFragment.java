package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.RecyclerAdapter;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;
import sg.edu.nus.iss.smartpantry.dao.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.ProductDao;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link sg.edu.nus.iss.smartpantry.views.fragments.CardHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sg.edu.nus.iss.smartpantry.views.fragments.CardHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardHomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] dataArray = new String[]{"India","Australia","USA","U.K","Japan","Russia","Germany","Pakistan","Bangladesh","Africa","Brazil","Rome","italy"};
    private OnFragmentInteractionListener mListener;
    private List<Product> productList = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();
    private MainController mainController;
    private static final int CAMERA_REQUEST = 1888;

    // TODO: Rename and change types and number of parameters
    public static CardHomeFragment newInstance(String param1, String param2) {
        CardHomeFragment fragment = new CardHomeFragment();

        return fragment;
    }

    public CardHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ProductDao productDao = DAOFactory.getProductDao(getActivity().getApplicationContext());
        ItemDao itemDao = DAOFactory.getItemDao(getActivity().getApplicationContext());
        productList = new ArrayList<Product>();
        productList = productDao.getAllProducts();
        itemList = itemDao.getAllItems();


        View view =inflater.inflate(R.layout.fragment_card_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(productList,getActivity());
        recyclerView.setAdapter(adapter);

        //Events for the Image Buttons
//
//        mainController = ControlFactory.getInstance().getMainController();
//        ImageButton addItemBtn = (ImageButton) view.findViewById(R.id.addItem_btn);
//        addItemBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mainController.addItem(getActivity());
//            }
//        });
//
//
//        ImageButton camButton = (ImageButton)view.findViewById(R.id.addItem_cam);
//        camButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        });
//
//        ImageButton btrButton = (ImageButton)view.findViewById(R.id.addItem_bt);
//        btrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBTReaderDialog();
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.refreshDrawableState();
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            image=Bitmap.createScaledBitmap(image, 150, 150, false);
//            Intent intent = new Intent(getActivity().getApplicationContext(), ItemDetails.class);
//            Bundle b = new Bundle();
//            b.putString("PRODUCT_NAME", ""); //Your id
//            b.putParcelable("PRODUCT_IMG", image);
//            intent.putExtras(b);
//            startActivity(intent);
//        }
//    }
//
//
//    public void showBTReaderDialog() {
//        final Dialog d = new Dialog(getActivity());
//        d.setTitle("Barcode Reader Connected");
//        d.setContentView(R.layout.btrdialog);
//        final EditText barcode = (EditText)d.findViewById(R.id.barCodeText);
//        barcode.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (i == KeyEvent.KEYCODE_ENTER)) {
//                    getProductDetailsAndAdd(barcode.getText().toString(), d);
//                    barcode.setEnabled(false);
//                    return true;
//                }
//                return false;
//            }
//        });
//        Button doneBtn = (Button)d.findViewById(R.id.doneBtn);
//        doneBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                d.dismiss();
//            }
//        });
//        d.show();
//    }
//
//    public void getProductDetailsAndAdd(String code, Dialog dialog) {
//        final String barcode = code;
//        final Dialog dlg = dialog;
//        new AsyncTask<Void, Void, ArrayList<String>>() {
//            Drawable d;
//
//            @Override
//            protected ArrayList<String> doInBackground(Void... params) {
//                ArrayList<String> details = null;
//                try {
//                    details = new ItemLookup(getActivity().getApplicationContext()).GetProductDetails(barcode);
//                    InputStream is = (InputStream) new URL(details.get(1)).getContent();
//                    d = Drawable.createFromStream(is, null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ItemNotFoundException e) {
//                    details = null;
//                }
//                return details;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<String> s) {
//                if (s == null) {
//                    addProduct(null, null, dlg);
//                } else {
//                    System.out.println("PRODUCT XXXXXXXXXXXX" + s.get(0));
//                    addProduct(s.get(0), d, dlg);
//                }
//            }
//        }.execute();
//    }
//
//    private void addProduct(String prodTitle,Drawable image, Dialog dlg) {
//        if(prodTitle==null){
//            Toast.makeText(getActivity(), "Product Not Found.", Toast.LENGTH_SHORT).show();
//            MediaPlayer player = MediaPlayer.create(getActivity().getApplicationContext(),R.raw.beep);
//            player.start();
//        }else{
//            Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
//            bitmap=Bitmap.createScaledBitmap(bitmap, 150,150,false);
//            try {
//                ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(), "MISC", prodTitle, bitmap, null, 1);
//                Toast.makeText(getActivity(), "Product Added", Toast.LENGTH_SHORT).show();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        final EditText barcode = (EditText)dlg.findViewById(R.id.barCodeText);
//        barcode.setEnabled(true);
//        barcode.setText("");
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //@Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
