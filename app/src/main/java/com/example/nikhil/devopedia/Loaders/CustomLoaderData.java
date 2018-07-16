package com.example.nikhil.devopedia.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.nikhil.devopedia.Requests.UserDataRequest;

/**
 * custom class to call main data retrieving class ( UserDataRequest )
 */
public class CustomLoaderData extends AsyncTaskLoader<String> {

    private String urls;
    private int requestType;

    public CustomLoaderData(Context context, String urls){
        super(context);
        this.urls = urls;
        requestType = 0;
    }

    public CustomLoaderData(Context context, String urls,int requestType){
        super(context);
        this.urls = urls;
        this.requestType = requestType;
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
        UserDataRequest userDataRequest = new UserDataRequest(urls,requestType);
        result = userDataRequest.fetchData();

        return result;
    }

}
