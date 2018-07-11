package com.example.nikhil.devopedia;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * custom class to call main user authentication class ( UserAuthRequest )
 */
public class CustomLoaderAuth extends AsyncTaskLoader<String> {

    private String urls;

    CustomLoaderAuth(Context context, String urls){
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