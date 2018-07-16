package com.example.nikhil.devopedia.Fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nikhil.devopedia.Adapters.CartAdapter;
import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Loaders.CustomLoaderData;
import com.example.nikhil.devopedia.Items.CartItem;
import com.example.nikhil.devopedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * fragment for cart items
 */
public class CartFragment extends Fragment{
    /// constants
    public static final String REQUEST_URL_DEVOPEDIA = Constants.URL_CART;
    private static final int LOADER_ID = 2;

    // context of main activity
    private Context context;

    //  layout of fragment
    private View rootView;

    // string that store data from api
    private String apiData;

    // object to store data
    private ArrayList<CartItem> cartItems;

    // custom adapter
    private CartAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mycourses_and_cart,container,false);

        context = getActivity();

        cartItems = new ArrayList<>();

        // initiating loader for api
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID,null,CartApi);

        }
        else{

            Toast.makeText(context,"check your internet connection",Toast.LENGTH_SHORT).show();

        }

        // setting up adapter
        adapter = new CartAdapter(getActivity(),cartItems,getLoaderManager());
        ListView listView = (ListView) rootView.findViewById(R.id.course_list);
        listView.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting title of action bar according to fragment
        getActivity().setTitle("Cart");
    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> CartApi
            = new LoaderManager.LoaderCallbacks<String>(){

        public Loader<String> onCreateLoader(int i, Bundle bundle) {
            return new CustomLoaderData(context,REQUEST_URL_DEVOPEDIA);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            apiData = data;
            handleUI();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) { }
    };

    private void handleUI(){
        extractFeatureFromJson();
    }

    /**
    * utility function to extract features from incoming json file
     */
    private void extractFeatureFromJson(){
        try {
            JSONObject base = new JSONObject(apiData);
            JSONArray array = base.getJSONArray("items");

            for(int i=0; i<array.length(); i++){
                JSONObject current = array.getJSONObject(i);
                JSONObject innerObj = current.getJSONObject("course");
                CartItem obj = new CartItem(
                        innerObj.getString("_id"),
                        current.getString("_id"),
                        innerObj.getString("title"),
                        innerObj.getString("introduction"),
                        innerObj.getString("img"),
                        innerObj.getString("video"),
                        innerObj.getInt("price")
                );

                cartItems.add(obj);
            }

            adapter.notifyDataSetChanged();


        }
        catch (JSONException e){
            Log.e("CartFragment","Problem parsing data from api",e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("no of items : ", cartItems.size()+"");
        adapter.clear();
    }

}
