package com.example.nikhil.devopedia.MyCourseVideo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nikhil.devopedia.R;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.w3c.dom.Text;

public class YoutubeViewHolder  extends RecyclerView.ViewHolder{
    public YouTubeThumbnailView videoThumbnailImageView;
    public CardView youtubeCardView;
    public TextView lesson_no;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        youtubeCardView = itemView.findViewById(R.id.youtube_row_card_view);
        lesson_no = itemView.findViewById(R.id.lesson_no);
    }
}