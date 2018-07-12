package com.example.nikhil.devopedia;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.youtube.player.YouTubeThumbnailView;

public class YoutubeViewHolder  extends RecyclerView.ViewHolder{
    public YouTubeThumbnailView videoThumbnailImageView;
    public CardView youtubeCardView;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        youtubeCardView = itemView.findViewById(R.id.youtube_row_card_view);

    }
}