package com.example.nikhil.devopedia.Constants;

public class Constants {

    public static final String DEVELOPER_KEY = "AIzaSyA_xMITwwtU4628qvMoa5wmd_Jgru3dY-Y";
    public static final String URL_CART = "http://devopedia.herokuapp.com/api/student/cart";
    public static final String URL_CATALOG = "http://devopedia.herokuapp.com/api/student/courses";
    public static final String URL_MY_COURSES = "http://devopedia.herokuapp.com/api/student/my-courses";
    public static final String URL_MENTORS = "http://devopedia.herokuapp.com/api/student/my-courses";
    public static final String URL_USER_AUTH = "http://devopedia.herokuapp.com/api/student/authenticate";
    public static final String URL_MY_COURSES_ITEM = "http://devopedia.herokuapp.com/api/student/course/";
    public static final String URL_BUY_COURSE = "http://devopedia.herokuapp.com/api/student/buy/";
    public static final String VIDEO_EMBED = "https://www.youtube.com/embed/";
    public static final String URL_REMOVE_COURSE = "http://devopedia.herokuapp.com/api/student/cart/";
    public static final String URl_WEBSITE = "https://devopedia.herokuapp.com/student";

    private static String Token;

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token){
        Token = token;
    }
}