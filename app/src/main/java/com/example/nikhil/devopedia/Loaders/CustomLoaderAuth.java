package com.example.nikhil.devopedia.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.nikhil.devopedia.Requests.UserAuthRequest;

/**
 * custom class to call main user authentication class ( UserAuthRequest )
 */
public class CustomLoaderAuth extends AsyncTaskLoader<String> {

    private String urls;

    public CustomLoaderAuth(Context context, String urls){
        super(context);
        this.urls = urls;
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
        result = UserAuthRequest.fetchData(urls);

        return result;
    }
}