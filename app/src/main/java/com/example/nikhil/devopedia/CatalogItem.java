package com.example.nikhil.devopedia;

public class CatalogItem {

    private String courseId;
    private String title;
    private String intro;
    private String imgUrl;
    private String videoUrl;
    private int price;

    public CatalogItem(String courseId, String title, String intro,
                       String imgUrl, String videoUrl, int price){
        this.courseId = courseId;
        this.title = title;
        this.intro = intro;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.price = price;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getPrice() {
        return price;
    }
}
