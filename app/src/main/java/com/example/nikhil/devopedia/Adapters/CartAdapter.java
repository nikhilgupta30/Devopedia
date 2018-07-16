package com.example.nikhil.devopedia.Adapters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.devopedia.Constants.Constants;
import com.example.nikhil.devopedia.Items.CartItem;
import com.example.nikhil.devopedia.Loaders.CustomLoaderData;
import com.example.nikhil.devopedia.R;

import java.io.InputStream;
import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {

    ArrayList<CartItem> cartItems;
    Context context;
    int pos;
    LoaderManager loaderManager;
    String REQUEST_URL_DEVOPEDIA;
    private int LOADER_ID;

    public CartAdapter(Activity context, ArrayList<CartItem> courses, LoaderManager loaderManager){
        super(context,0,courses);
        this.cartItems = courses;
        this.context = context;
        this.loaderManager = loaderManager;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cart_item, parent, false);
        }

        final CartItem currCourse = getItem(position);

        ImageView img = (ImageView) listItemView.findViewById(R.id.img);
        new CartAdapter.DownloadImageTask(img).execute(currCourse.getImgUrl());

        TextView name = (TextView)listItemView.findViewById(R.id.course_title);
        name.setText(currCourse.getTitle());

        TextView category = (TextView)listItemView.findViewById(R.id.course_intro);
        category.setText(currCourse.getIntro());

        // setting remove button
        final Button remove = (Button)listItemView.findViewById(R.id.remove);
        remove.setTag(position);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = (int)remove.getTag();
                REQUEST_URL_DEVOPEDIA = Constants.URL_REMOVE_COURSE;
                REQUEST_URL_DEVOPEDIA = REQUEST_URL_DEVOPEDIA + cartItems.get(pos).getCartId();
                LOADER_ID = 7 + pos;

                // initiating loader for api
                ConnectivityManager connMgr = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    loaderManager.restartLoader(LOADER_ID,null,CartApi);

                }
                else{

                    Toast.makeText(context,"check your internet connection",Toast.LENGTH_SHORT).show();

                }

            }
        });

        return listItemView;

    }

    /**
     *  loader for retrieving data
     */
    private LoaderManager.LoaderCallbacks<String> CartApi
            = new LoaderManager.LoaderCallbacks<String>(){

        public Loader<String> onCreateLoader(int i, Bundle bundle) {
            return new CustomLoaderData(context,REQUEST_URL_DEVOPEDIA,2);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if(data.equals("")){
                Toast.makeText(context, "problem removing the course", Toast.LENGTH_SHORT).show();
            }
            else{
                handleUi();
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) { }
    };

    private void handleUi(){
        cartItems.remove(pos);
        CartAdapter.this.notifyDataSetChanged();
    }

    /**
     * helper class to download image and set it
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
