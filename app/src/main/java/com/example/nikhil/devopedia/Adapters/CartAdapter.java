package com.example.nikhil.devopedia.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

    View rootView;

    public CartAdapter(Activity context, ArrayList<CartItem> courses,
                       LoaderManager loaderManager, View rootView){
        super(context,0,courses);
        this.cartItems = courses;

        this.context = context;
        this.loaderManager = loaderManager;

        this.rootView = rootView;
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

        TextView price = (TextView)listItemView.findViewById(R.id.price);
        price.setText("₹ " + currCourse.getPrice());

        // setting remove button
        final ImageButton remove = (ImageButton) listItemView.findViewById(R.id.remove);
        remove.setTag(position);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = (int)remove.getTag();
                REQUEST_URL_DEVOPEDIA = Constants.URL_REMOVE_COURSE;
                REQUEST_URL_DEVOPEDIA = REQUEST_URL_DEVOPEDIA + cartItems.get(pos).getCartId();
                LOADER_ID = 7 + pos;

                promptDeleteConfirm();

            }
        });

        return listItemView;

    }

    /**
     * prompt for confirming delete
     */
    protected void promptDeleteConfirm() {

        AlertDialog alertbox = new AlertDialog.Builder(context)
                .setMessage("Confirm delete ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        // initiating loader for api
                        ConnectivityManager connMgr = (ConnectivityManager)
                                context.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        if (networkInfo != null && networkInfo.isConnected()) {

                            loaderManager.restartLoader(LOADER_ID,null,CartApi);

                        }
                        else{
                            Toast.makeText(context,"check your internet connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

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
        TextView total = (TextView)rootView.findViewById(R.id.total_amount);
        String value = total.getText().toString();
        value = value.substring(10);
        int total_amount = Integer.parseInt(value);

        total_amount -= cartItems.get(pos).getPrice();
        total.setText("Total : ₹ " + total_amount);

        cartItems.remove(pos);
        CartAdapter.this.notifyDataSetChanged();
        Toast.makeText(context, "Course Successfully Removed", Toast.LENGTH_SHORT).show();

        if(cartItems.size() == 0) {
            TextView mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("Your Cart is Empty");
        }
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
