package com.example.nikhil.devopedia.Items;

public class LessonItem {

    private String lessonId;
    private String title;
    private String description;
    private String videoUrl;
    private String imageUrl;

    public LessonItem(String lessonId,String title,String description,
                                                    String imageUrl,String videoUrl){
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
