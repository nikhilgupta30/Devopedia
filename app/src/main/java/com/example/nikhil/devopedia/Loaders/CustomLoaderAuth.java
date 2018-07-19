package com.example.nikhil.devopedia.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.nikhil.devopedia.Requests.UserAuthRequest;

import java.util.HashMap;

/**
 * custom class to call main user authentication class ( UserAuthRequest )
 */
public class CustomLoaderAuth extends AsyncTaskLoader<String> {

    private String urls;
    private HashMap<String,String> credentials;

    public CustomLoaderAuth(Context context, String urls, HashMap<String,String> credentials){
        super(context);
        this.urls = urls;
        this.credentials = credentials;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (urls == null) {
            return null;
        }

        String result;
        result = UserAuthRequest.fetchData(urls, credentials);

        return result;
    }
}