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
import sg.edu.nus.iss.smartpantry.adapters.RecyclerAdapter;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;

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

    public RecyclerAdapter getAdapter() {
        return adapter;
    }

    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
        productList = new ArrayList<Product>();
        productList = productDao.getAllProducts();


        View view =inflater.inflate(R.layout.fragment_card_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(productList,getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refreshData();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



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
