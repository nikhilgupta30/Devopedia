package com.example.nikhil.devopedia.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikhil.devopedia.Items.CatalogItem;
import com.example.nikhil.devopedia.R;

import java.io.InputStream;
import java.util.ArrayList;

public class CatalogAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CatalogItem> catalogItems;

    public CatalogAdapter(Context c, ArrayList<CatalogItem> catalogItems) {
        mContext = c;
        this.catalogItems = catalogItems;
    }

    public int getCount() {
        return catalogItems.size();
    }

    public CatalogItem getItem(int position) {
        return catalogItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        catalogItems.clear();
    }

    // create a new View for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridItemView = convertView;

        if(gridItemView == null) {
            gridItemView = LayoutInflater.from(mContext).inflate(
                    R.layout.catalog_item, parent, false);
        }

        final CatalogItem currCourse = getItem(position);

        ImageView img = (ImageView) gridItemView.findViewById(R.id.img);
        new CatalogAdapter.DownloadImageTask(img).execute(currCourse.getImgUrl());

        TextView name = (TextView)gridItemView.findViewById(R.id.course_title);
        name.setText(currCourse.getTitle());

        return gridItemView;

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
