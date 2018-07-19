package com.example.nikhil.devopedia.LogIn;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Items.CartItem;
import com.example.nikhil.devopedia.Items.MyCourseItem;
import com.example.nikhil.devopedia.Loaders.CustomLoaderAuth;
import com.example.nikhil.devopedia.MainActivity;
import com.example.nikhil.devopedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * User Authentication Activity
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private Button button;

    private static final String authUrl = Constants.URL_USER_AUTH;

    private static final int LOADER_ID = 1;

    private String result;

    private EditText emailField,passwordField;

    private TextView register;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button)findViewById(R.id.login);
        button.setOnClickListener(this);

        passwordField = (EditText)findViewById(R.id.password);
        emailField = (EditText)findViewById(R.id.email);

        register = (TextView)findViewById(R.id.register);

        // underlining the text
        SpannableString content = new SpannableString("Register Here.");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        register.setText(content);

        //email = emailField.getText().toString().trim();

        // checks regularly if user types write format of email
//        emailField.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//
//                if (isValidEmail(email))
//                {
//                    Toast.makeText(LoginActivity.this,"valid email address",
//                            Toast.LENGTH_SHORT).show();
//                    // or
//                    //textView.setText("valid email");
//                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this,"Invalid email address",
//                            Toast.LENGTH_SHORT).show();
//                    //or
//                    //textView.setText("invalid email");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });

    }

    // helper function to check whether a user has entered a right email format
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.login:
                authenticate();
                break;

            case R.id.register:
                //registerUser();
                break;

        }
    }

    public void authenticate(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().restartLoader(LOADER_ID, null, this);

        }
        else{

            Toast.makeText(LoginActivity.this, "check your internet connection",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        HashMap<String,String> credentials = new HashMap<>();
        credentials.put("email",email);
        credentials.put("password",password);

        return new CustomLoaderAuth(LoginActivity.this,authUrl, credentials);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(data.equals("false") ){
            Toast.makeText(LoginActivity.this,"log in failed",Toast.LENGTH_SHORT).show();
        }
        else{
            String token = extractFeatureFromJson(data);
            if(token != null){
                Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_SHORT).show();

                String username = email;
                int index=1;
                for(int i=0;i<username.length();i++){
                    if(username.charAt(i)=='@'){
                        index = i;
                        break;
                    }
                }

                username = username.substring(0,index);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                finish();
                startActivity(intent);
            }

        }

    }

    /**
     * utility function to extract features from incoming json file
     */
    private String extractFeatureFromJson(String apiData){

        String token = null;
        try {
            JSONObject base = new JSONObject(apiData);
            if(base.getBoolean("success")){
                token =  base.getString("token");
            }

        }
        catch (JSONException e){
            Log.e("Login Window","Problem parsing data from api",e);
        }

        return token;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        result = "";
    }


    // back key operation
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

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
