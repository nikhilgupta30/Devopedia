package com.example.nikhil.devopedia.Items;

public class CartItem {

    private String courseId;
    private String cartId;
    private String title;
    private String intro;
    private String imgUrl;
    private String videoUrl;
    private int price;

    public CartItem(String courseId,String cartId, String title, String intro,
                    String imgUrl, String videoUrl, int price){
        this.courseId = courseId;
        this.title = title;
        this.intro = intro;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.cartId = cartId;
        this.price = price;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCartId() {
        return cartId;
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
