package com.example.nikhil.devopedia;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private Button button;

    private static final String authUrl = "http://devopedia.herokuapp.com/api/student/authenticate";

    private static final int LOADER_ID = 1;

    private String result;

    private EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button)findViewById(R.id.login);
        button.setOnClickListener(this);

        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.login:
//                toastMessage = "Password: " + password.getText().toString();
//                Log.v(MainActivity.class.getSimpleName(),toastMessage);
                authenticate();

        }
    }

    public void authenticate(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            //LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().restartLoader(LOADER_ID, null, this);
            //getLoaderManager().restartLoader(LOADER_ID, null, dataApi_1);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        String[] input = new String[2];
        input[0] = new String();
        input[0] = email.getText().toString();
        input[1] = new String();
        input[1] = password.getText().toString();

        return new CustomLoaderAuth(LoginActivity.this,authUrl);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(data == "false"){
            Toast.makeText(LoginActivity.this,"log in failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        result = "";
    }


    private LoaderManager.LoaderCallbacks<String> dataApi_1
            = new LoaderManager.LoaderCallbacks<String>(){

        @Override
        public Loader<String> onCreateLoader(int i, Bundle bundle) {
            String[] input = new String[2];
            input[0] = new String();
            input[0] = email.getText().toString();
            input[1] = new String();
            input[1] = password.getText().toString();

            return new CustomLoaderAuth(LoginActivity.this,authUrl);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Log.v("data",data);
            if(data == null){
                Toast.makeText(LoginActivity.this,"log in failed",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            result = "";
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
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
}
