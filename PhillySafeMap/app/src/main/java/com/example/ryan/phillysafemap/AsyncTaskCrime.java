package com.example.ryan.phillysafemap;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncTaskCrime extends AsyncTask<Void, Void, List<Crime>> {

    String crimesURL;
    private static final String TAG = AsyncTaskCrime.class.getSimpleName();

    // Find this in your developer console
    private static final String APP_ID = "0dfe9797f15da547b66830f30a6b58c0";
    // Find this in your developer console
    private static final String API_KEY = "e49a29df1d2e442985d841f23d9e412e";

    public AsyncTaskCrime(String crimesURL) {
        this.crimesURL = crimesURL;
    }

    @Override
    protected List<Crime> doInBackground(Void... params) {
        List<Crime> crimesInPhilly = new ArrayList<Crime>();

        //1. create okHttp Client object
        OkHttpClient client = new OkHttpClient();

        //2. Define reqest being sent to the server
        RequestBody postData = new FormBody.Builder()
                .add("type", "json")
                .build();

        Request request = new Request.Builder()
                .url(crimesURL)
                .post(postData)
                .build();

        //3. Transport the request and wait for response to process next
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseCrimes(result, crimesInPhilly);
        return crimesInPhilly;
    }

    public static void parseCrimes(String jsonLine, List<Crime> crimes)
    {
        String date, time, location, description, UCR;
        String longitude, latitude;

        // initialize JSON parser to parse the response String
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(jsonLine);

        JsonArray crimesList = root.getAsJsonObject().get("features").getAsJsonArray();

        for(int i = 0; i < crimesList.size(); i++)
        {
            JsonObject current = crimesList.get(i).getAsJsonObject().get("attributes").getAsJsonObject();

            date = current.get("DISPATCH_DATE").getAsString();
            time = current.get("DISPATCH_TIME").getAsString();
            location = formatLocation(current.get("LOCATION_BLOCK").getAsString());
            description = current.get("TEXT_GENERAL_CODE").getAsString();
            longitude = current.get("POINT_X").getAsString();
            latitude = current.get("POINT_Y").getAsString();
            UCR = current.get("UCR_GENERAL").getAsString();

            if(happenedRecently(date))
                crimes.add(new Crime(date, time, location, description, longitude, latitude, UCR));

//            Log.i(TAG, crimes.get(i).toString());
        }
    }

    private static boolean happenedRecently(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date crimeDate = new Date();
        try {
            crimeDate = fmt.parse(dateString);
            //Log.d("Crime", crimeDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date currentDate = new Date();
        //Log.d("Current", currentDate.toString());

        long diff = Math.abs(currentDate.getTime() - crimeDate.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);
        //Log.d("Diff Days", ""+diffDays);

        if(diffDays < 15)
            return true;

        return false;
    }

    public static String formatLocation(String location)
    {
        String formattedLocation = "";

        if(location.contains("BLOCK")) {
            String[] tokens = location.split(" BLOCK");
            for(int i = 0; i < tokens.length; i++)
                formattedLocation += tokens[i];
        }
        else
            formattedLocation = location.trim();

        return formattedLocation;
    }
}