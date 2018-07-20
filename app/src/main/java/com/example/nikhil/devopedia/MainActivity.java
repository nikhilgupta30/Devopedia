package com.example.nikhil.devopedia;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.devopedia.About.AboutActivity;
import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Fragments.CartFragment;
import com.example.nikhil.devopedia.Fragments.CatalogFragment;
import com.example.nikhil.devopedia.Fragments.MyCoursesFragment;
import com.example.nikhil.devopedia.LogIn.LoginActivity;

/**
 * main class of App
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // todo : remove token initialization when make fragments
    private String token;

    private FragmentManager fragmentManager;

    private int fragment_id;

    // to store token when user quits the app
    SharedPreferences mPrefs;

    private View navHeader;

    private TextView usernameView;
    private TextView userInitials;

    private String username = "xyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mPrefs = getSharedPreferences("label", 0);

        if(getIntent().hasExtra("token")){
            token = getIntent().getStringExtra("token");
            username = getIntent().getStringExtra("username");

            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("token", token).commit();
            mEditor.putString("username", username).commit();
            Constants.setToken(token);

        }else {
            token = mPrefs.getString("token",null);
            Constants.setToken(token);
            username = mPrefs.getString("username","xyz");

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fragment_id
        fragment_id = 3;
        if(getIntent().hasExtra("fragment_id")) {
            fragment_id = getIntent().getIntExtra("fragment_id", 3);
        }

        // fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Chat functionality is under development", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);

        Log.v("check ","oncreate");

        usernameView = (TextView) navHeader.findViewById(R.id.username);
        usernameView.setText(username);

        userInitials = (TextView) navHeader.findViewById(R.id.user_initials);
        char firstLetter = username.charAt(0);
        firstLetter = Character.toUpperCase(firstLetter);
        userInitials.setText(firstLetter+"");

        // todo : calling fragment testing
        fragmentManager = getFragmentManager();
        if(fragment_id == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new CatalogFragment())
                    .commit();
        }
        else if( fragment_id == 2 ){
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new CartFragment())
                    .commit();
        }
        else{
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new MyCoursesFragment())
                    .commit();
        }

    }

    /**
     * closing the drawer (if opened) on pressing back button
     */
    // todo : search how to exit the app when back is pressed again
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            exitByBackKey();
        }
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                token = null;
                username = "xyz";
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("token", token).commit();
                mEditor.putString("username", username).commit();
                Constants.setToken(token);
                usernameView.setText(username);
                userInitials.setText('X'+"");

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);


            } else {
                Toast.makeText(MainActivity.this,"Problem Signing You Out",
                        Toast.LENGTH_SHORT).show();
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        Log.v("check ","start");
        // token
        token = mPrefs.getString("token", null);
        Constants.setToken(token);

        // start login activity if the user is not logged in
        if( token == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v("status onstop : ",username);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("token", token).commit();
        mEditor.putString("username", username).commit();
        super.onStop();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {

            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new CatalogFragment())
                    .commit();

        } else if (id == R.id.nav_cart) {

            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new CartFragment())
                    .commit();

        } else if (id == R.id.nav_my_courses) {

            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, new MyCoursesFragment())
                    .commit();

        } else if (id == R.id.nav_share) {

            // intent to send share content
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

            sharingIntent.setType("text/plain");
            String shareBody = "Visit Our Site\n http://devopedia.herokuapp.com/";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Devopedia");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_send_feedback) {

            //email intent
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","devopedia@gmail.com", null));

            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Devopedia Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Add your Feedback here");

            startActivity(Intent.createChooser(emailIntent, "Complete Action Using"));

        } else if (id == R.id.nav_about){

            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
