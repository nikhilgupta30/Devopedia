package com.example.nikhil.devopedia.Requests;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * main class for user authentication
 */
public class UserAuthRequest {

    private static final String LOG_TAG = UserAuthRequest.class.getSimpleName();

    private UserAuthRequest(){

    }

    /**
     * main driver function of class
     */
    public static String fetchData(String requestUrl) {     // change string to your data object

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        if( jsonResponse == null ){
            jsonResponse = "false";
        }
        Log.v(LOG_TAG,jsonResponse);
        return jsonResponse;

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
     * function fot http request
     */
    private static String makeHttpRequest(URL url) throws IOException {

        // hash map for storing user entered email and password
        JSONObject postDataParams = new JSONObject();

        try {
            postDataParams.put("email", "nikhilgupta311@gmail.com");
            postDataParams.put("password", "123456789");
        }
        catch (Exception e){
            Log.e(LOG_TAG,"exception occured : " + e);
        }

        String jsonResponse = null;

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // make http request
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            try {
                writer.write(getPostDataString(postDataParams));
            }
            catch (Exception e){
                Log.e(LOG_TAG,"exception occurred : " + e);
            }

            writer.flush();
            writer.close();
            os.close();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.v(LOG_TAG,"http is ok");

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }

        }catch (IOException e){

            Log.e(LOG_TAG, "Problem in fetching the data", e);

        }finally {

            if (urlConnection != null) {
                // disconnect
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

    /**
     * helper function for parameters of api request
     */

    private static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
