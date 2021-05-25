package com.example.booklisting;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

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
import java.util.List;

public class QueryUtils {
    //Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    //returns list of books from the json response
    public static List<Books> extractBookDetails(String jsonResponse) {
        Log.v(LOG_TAG, "Extracting Book Details");
        ArrayList<Books> books = new ArrayList<Books>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray itemsArray = root.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject iItem = itemsArray.getJSONObject(i);//Accessing i'th element of JSONArray items
                JSONObject volumeInfo = iItem.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                String authorsNames = "";
                for (int j = 0; j < authorsArray.length(); j++) {
                    String authorsName = authorsArray.getString(j);
                    authorsNames = authorsNames + " | " + authorsName;
                }
                Log.v(LOG_TAG, "authors= " + authorsNames);
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnailUrl = imageLinks.getString("smallThumbnail");
                Drawable thumbnail = LoadImageFromWeb(thumbnailUrl);

                JSONObject accessInfo = iItem.getJSONObject("accessInfo");
                String webReaderLink=accessInfo.getString("webReaderLink");

                Books iBooks = new Books(title, authorsNames, thumbnail,webReaderLink);
                books.add(iBooks);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "srcName");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Books> fetchBooksDetails(String requestUrl) {
        Log.v(LOG_TAG, "I'm fetching book details");
        URL url = createUrl(requestUrl);//converting String requestUrl to URL
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);//make HTTP request at the url
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractBookDetails(jsonResponse);
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        Log.v(LOG_TAG, "I'm making HTTP Request");
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();//open connection at url
            urlConnection.setRequestMethod("GET");//set the request method-GET,PUT or DELETE
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();//connect to the server
            int responseCode = urlConnection.getResponseCode();//store the received response code from the server
            //if connection is successful then response code is 200
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();//server sends data as InputStream
                jsonResponse = readFromStream(inputStream);//convert received InputStream to String jsonResponse
            } else {
                Log.e(LOG_TAG, "Status Code is " + responseCode);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while making HTTP request ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static URL createUrl(String stringUrl) {
        Log.v(LOG_TAG, "I'm creating URL");
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.v(LOG_TAG, "I'm in reading from stream");
        StringBuilder stringBuilder = new StringBuilder();//Better than String

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();//reading the first line of the inputStream
            while (line != null) {
                stringBuilder.append(line);//adding the line(of the inputStream to the StringBuilder)
                line = reader.readLine();//reading the next line of inputStream
            }
        }
        return stringBuilder.toString();//converting StringBuilder output to String(The jsonResponse code)
    }
}

