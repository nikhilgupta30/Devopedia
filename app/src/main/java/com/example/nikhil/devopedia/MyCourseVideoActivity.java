package com.example.nikhil.devopedia;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Items.LessonItem;
import com.example.nikhil.devopedia.Items.MyCourseItem;
import com.example.nikhil.devopedia.Loaders.CustomLoaderData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * class for video in my_courses
 */
public class MyCourseVideoActivity extends AppCompatActivity {

    private static final String TAG = MyCourseVideoActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ArrayList<String> youtubeVideoArrayList;

    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;

    // string that store data from api
    private String apiData;

    //object to store data
    private ArrayList<LessonItem> lessonItems;

    //constants
    public static String REQUEST_URL_DEVOPEDIA = Constants.URL_MY_COURSES_ITEM;
    public static final int LOADER_ID = 5;
    public String currCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_video);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currCourseId = getIntent().getStringExtra("CurrCourseId");
        REQUEST_URL_DEVOPEDIA = REQUEST_URL_DEVOPEDIA + currCourseId;

        final String actionBarTitle = getIntent().getStringExtra("ActionBarTitle");
        getSupportActionBar().setTitle(actionBarTitle);

        lessonItems = new ArrayList<>();

        // initiating loader for api
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID,null,lessonApi);

        }
        else{

            Toast.makeText(this,"check your internet connection",Toast.LENGTH_SHORT).show();

        }


    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> lessonApi
            = new LoaderManager.LoaderCallbacks<String>(){

        public Loader<String> onCreateLoader(int i, Bundle bundle) {
            return new CustomLoaderData(MyCourseVideoActivity.this,REQUEST_URL_DEVOPEDIA);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            apiData = data;
            handleUI();

            // generating video ids from data
            generateVideoList();

            // initialize video player
            initializeYoutubePlayer();

            // recycler view
            setUpRecyclerView();
            populateRecyclerView();
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
            JSONArray array = base.getJSONArray("lessons");

            for(int i=0; i<array.length(); i++){
                JSONObject current = array.getJSONObject(i);
                LessonItem obj = new LessonItem(
                        current.getString("_id"),
                        current.getString("title"),
                        current.getString("description"),
                        current.getString("img"),
                        current.getString("video")
                );

                lessonItems.add(obj);
            }

        }
        catch (JSONException e){
            Log.e("MyCourseFragment","Problem parsing data from api",e);
        }
    }


    /**
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    if(youtubeVideoArrayList.size()>0){
                        youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

    /**
     * setup the recycler view here
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recycler view and implement the click event here
     */
    private void populateRecyclerView() {
        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(
                new RecyclerViewOnClickListenerMyCourseVideo(
                        this,
                        new RecyclerViewOnClickListenerMyCourseVideo.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
                    adapter.setSelectedPosition(position);

                    //load selected video
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(position));
                }

            }
        }));
    }


    /**
     * method to generate dummy array list of videos
     */
    private void generateVideoList() {
        youtubeVideoArrayList = new ArrayList<>();

        //add all videos to array list
        for(int i=0; i<lessonItems.size(); i++){
            String tempString = lessonItems.get(i).getVideoUrl();
            Log.v("video ids",tempString);
            tempString = tempString.substring(Constants.VIDEO_EMBED.length());
            youtubeVideoArrayList.add(tempString);
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
