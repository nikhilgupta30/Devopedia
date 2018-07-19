package com.example.nikhil.devopedia.Requests;

import android.util.Log;

import com.example.nikhil.devopedia.Constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * main class for retrieving user data
 */
public class UserDataRequest {

    private static final String LOG_TAG = UserDataRequest.class.getSimpleName();

    private String requestUrl;
    private static String requestType;
    private static String tokenValue;

    public UserDataRequest(String requestUrl,int reqType){
        this.requestUrl = requestUrl;

        if(reqType == 0){
            requestType = "GET";
        }
        else if(reqType == 1){
            requestType = "POST";
        }
        else{
            requestType = "DELETE";
        }
    }

    /**
     * main driver function of class
     */
    public String fetchData() {     // change string to your data object

        tokenValue = Constants.getToken();

        if(tokenValue != null) {

            // Create URL object
            URL url = createUrl(requestUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            return jsonResponse;

        }

        return null;

    }

    /**
     * function to create url from string
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();

    }

    /**
     * function to make main httpRequest
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // make http request
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            Log.v(LOG_TAG,"executed");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000 /* milliseconds */);
            urlConnection.setConnectTimeout(35000 /* milliseconds */);
            urlConnection.setRequestProperty("x-access-token",tokenValue);

            urlConnection.setRequestMethod(requestType);

            urlConnection.setDoInput(true);

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.v(LOG_TAG,"http is ok");

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.v(LOG_TAG, "json response: " + jsonResponse);
            }
            else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        }catch (IOException e){

            Log.e(LOG_TAG, "Problem in fetching the data", e);

        }finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

}
