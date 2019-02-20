package com.example.android.earthquake;

import android.text.TextUtils;
import android.util.Log;
import android.util.MalformedJsonException;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
final class QueryUtils {
    private static final String LOG_TAG =  QueryUtils.class.getSimpleName();




    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */




    private QueryUtils() {
    }


    public static List<Word> fetchEarthquakeData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the Json Result",e);
        }

        List<Word> words = extractFeatureFromJson(jsonResponse);
        return words;

    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the Url",e);
        }
        return url;
    }

    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse = "";

        if(url==null){
            return jsonResponse;
        }

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
                Log.e(LOG_TAG,"Connection Error code :"+ urlConnection.getResponseCode());
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Problem retrieving the Json Result",e);
        }
        finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }

        }

        return jsonResponse;

    }

   private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null ){
                output.append(line);
                line = bufferedReader.readLine();
                }
        }
        return output.toString();
   }

   private static List<Word> extractFeatureFromJson (String earthquakeJSON){

        if (TextUtils.isEmpty(earthquakeJSON) ){
            return null;
        }

        List<Word> words = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray = rootJsonObject.getJSONArray("features");

            for (int i =0 ; i<  earthquakeArray.length();i++){
                JSONObject currentJsonObject = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentJsonObject.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Word word = new Word(magnitude,location,time,url);
                words.add(word);

            }
        }
        catch (JSONException e){
            Log.e("Query Utils","Problem parsing the earthquake Json Response",e);
        }
       return words;
   }


}