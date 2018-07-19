package com.example.nikhil.devopedia.Preview;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Items.CatalogItem;
import com.example.nikhil.devopedia.Loaders.CustomLoaderData;
import com.example.nikhil.devopedia.MainActivity;
import com.example.nikhil.devopedia.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = PreviewActivity.class.getSimpleName();

    // constants
    public String REQUEST_URL_DEVOPEDIA = Constants.URL_BUY_COURSE;

    private static final int LOADER_ID = 6;

    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ArrayList<String> youtubeVideoArrayList;

    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;

    private CatalogItem currItem;

    private TextView introText;
    private TextView price;

    // floating action button
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // extracting curr item info from obj pass through intent
        currItem = (CatalogItem) getIntent().getSerializableExtra("serializeData");

        // action bar title to course title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( currItem.getTitle() );

        // price
        price = (TextView) findViewById(R.id.price);
        String priceValue = "₹ " + currItem.getPrice();
        price.setText(priceValue);

        // introduction set
        introText = (TextView) findViewById(R.id.intro);
        introText.setText(currItem.getIntro());

        // url updating
        REQUEST_URL_DEVOPEDIA = REQUEST_URL_DEVOPEDIA + currItem.getCourseId();
        Log.v("url : ",REQUEST_URL_DEVOPEDIA);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initiating loader for api
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    getLoaderManager().initLoader(LOADER_ID,null,buyApi);

                }
                else{

                    Toast.makeText(PreviewActivity.this,
                            "check your internet connection",Toast.LENGTH_SHORT).show();

                }


            }
        });

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        String videoID = currItem.getVideoUrl();
        if(URLUtil.isValidUrl(videoID)){
            generateVideoList();
            initializeYoutubePlayer();
        }
        else{
            TextView noVideoMessage = (TextView)findViewById(R.id.no_video);
            noVideoMessage.setVisibility(View.VISIBLE);
        }



    }

    /**
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private void initializeYoutubePlayer() {

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
     * method to generate dummy array list of videos
     */
    private void generateVideoList() {
        youtubeVideoArrayList = new ArrayList<>();
        String videoUrl = currItem.getVideoUrl();
        videoUrl = videoUrl.substring(Constants.VIDEO_EMBED.length());
        youtubeVideoArrayList.add(videoUrl);
    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> buyApi
            = new LoaderManager.LoaderCallbacks<String>(){

        public Loader<String> onCreateLoader(int i, Bundle bundle) {
            return new CustomLoaderData(PreviewActivity.this,REQUEST_URL_DEVOPEDIA);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {

            handleUi(data);

        }

        @Override
        public void onLoaderReset(Loader<String> loader) { }
    };

    private void handleUi(String data){

        Snackbar snackbar;

        if(data.equals("")){
            snackbar = Snackbar.make(findViewById(R.id.preview_window), "Course Already present in Cart",
                    Snackbar.LENGTH_LONG).setAction("Action", null);
        }else{
            snackbar = Snackbar.make(findViewById(R.id.preview_window), "Course Added to Your Cart",
                    Snackbar.LENGTH_LONG).setAction("Action", null);

            // changing fab
            fab.setImageResource(R.drawable.ic_preview_done);

            int color = ContextCompat.getColor(PreviewActivity.this, R.color.preview_done);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            fab.setBackgroundTintList(colorStateList);
            fab.setClickable(false);
        }

        snackbar.setAction("View Cart", new goToCartListener());
        snackbar.show();
    }

    public class goToCartListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PreviewActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("fragment_id",2);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
