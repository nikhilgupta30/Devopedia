package com.example.nikhil.devopedia;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyCourseAdapter extends ArrayAdapter<MyCourseItem> {

    public MyCourseAdapter(Activity context, ArrayList<MyCourseItem> courses){
        super(context,0,courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        // todo : change my_course_item
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.my_course_item, parent, false);
        }

        final MyCourseItem currCourse = getItem(position);

        ImageView img = (ImageView) listItemView.findViewById(R.id.img);
        new DownloadImageTask(img).execute(currCourse.getImgUrl());

        TextView name = (TextView)listItemView.findViewById(R.id.course_title);
        name.setText(currCourse.getTitle());

        TextView category = (TextView)listItemView.findViewById(R.id.course_intro);
        category.setText(currCourse.getIntro());

        return listItemView;

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
