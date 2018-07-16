package com.example.nikhil.devopedia.Fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nikhil.devopedia.Adapters.MyCourseAdapter;
import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Items.MyCourseItem;
import com.example.nikhil.devopedia.Loaders.CustomLoaderData;
import com.example.nikhil.devopedia.MyCourseVideo.MyCourseVideoActivity;
import com.example.nikhil.devopedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * fragment for my-courses
 */
public class MyCoursesFragment extends Fragment {

    // constants
    public static final String REQUEST_URL_DEVOPEDIA = Constants.URL_MY_COURSES;

    private static final int LOADER_ID = 1;

    // context of main activity
    private Context context;

    //  layout of fragment
    private View rootView;

    // string that store data from api
    private String apiData;

    // object to store data
    private ArrayList<MyCourseItem> myCourseItems;

    // custom adapter
    private MyCourseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mycourses,container,false);

        context = getActivity();

        myCourseItems = new ArrayList<>();

        // initiating loader for api
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID,null,myCoursesApi);

        }
        else{

            Toast.makeText(context,"check your internet connection",Toast.LENGTH_SHORT).show();

        }

        // setting up adapter
        adapter = new MyCourseAdapter(getActivity(),myCourseItems);
        ListView listView = (ListView) rootView.findViewById(R.id.course_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCourseItem currCourse = myCourseItems.get(position);

                Intent intent = new Intent(getActivity(), MyCourseVideoActivity.class);
                intent.putExtra("CurrCourseId",currCourse.getCourseId());
                intent.putExtra("ActionBarTitle",currCourse.getTitle());
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting title of action bar according to fragment
        getActivity().setTitle("My Courses");
    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> myCoursesApi
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
            JSONArray array = new JSONArray(apiData);

            for(int i=0; i<array.length(); i++){
                JSONObject current = array.getJSONObject(i);
                MyCourseItem obj = new MyCourseItem(
                        current.getString("_id"),
                        current.getString("title"),
                        current.getString("introduction"),
                        current.getString("img"),
                        current.getString("video")
                        );

                myCourseItems.add(obj);
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
