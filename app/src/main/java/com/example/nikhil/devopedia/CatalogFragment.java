package com.example.nikhil.devopedia;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * fragment for my-courses
 */
public class CatalogFragment extends Fragment {

    // constants
    public static final String REQUEST_URL_DEVOPEDIA = "http://devopedia.herokuapp.com/api/student/courses";
    private static final int LOADER_ID = 3;

    // context of main activity
    private Context context;

    //  layout of fragment
    private View rootView;

    // string that store data from api
    private String apiData;

    // object to store data
    private ArrayList<CatalogItem> catalogItems;

    // custom adapter
    private CatalogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_catalog,container,false);

        context = getActivity();

        catalogItems = new ArrayList<>();

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID,null,catalogApi);

        }
        else{

            Toast.makeText(context,"check your internet connection",Toast.LENGTH_SHORT).show();

        }

        adapter = new CatalogAdapter(getActivity(),catalogItems);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting title of action bar according to fragment
        getActivity().setTitle("Course Catalog");
    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> catalogApi
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

    private void extractFeatureFromJson(){
        try {
            JSONArray array = new JSONArray(apiData);

            for(int i=0; i<array.length(); i++){
                JSONObject current = array.getJSONObject(i);

                String currStatus = current.getString("approved");

                if( currStatus.equals("true") ) {
                    CatalogItem obj = new CatalogItem(
                            current.getString("_id"),
                            current.getString("title"),
                            current.getString("introduction"),
                            current.getString("img"),
                            current.getString("video"),
                            current.getInt("price")
                    );

                    catalogItems.add(obj);
                }
            }

            adapter.notifyDataSetChanged();


        }
        catch (JSONException e){
            Log.e("MyCourseFragment","Problem parsing data from api",e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();
    }

}
