package com.example.nikhil.devopedia;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * custom class to call main data retrieving class ( UserDataRequest )
 */
public class CustomLoaderData extends AsyncTaskLoader<String> {

    private String urls;

    CustomLoaderData(Context context, String urls){
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
        result = UserDataRequest.fetchData(urls);

        return result;
    }

}
