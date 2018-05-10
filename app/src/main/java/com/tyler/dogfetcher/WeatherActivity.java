package com.tyler.dogfetcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "Weather Activity";
    private static final String EXTRA_GOOGLE_ACCOUNT =
            "com.tyler.weatheractivity";

    TextView mCurrentTempTV;
    ImageView mCurrentRadarTV;
    ProgressBar mLoadingProgress;

    String key;

    public static Intent newIntent(Context packageContext, GoogleSignInAccount account) {
        Intent intent = new Intent(packageContext, WeatherActivity.class);
        intent.putExtra(EXTRA_GOOGLE_ACCOUNT, account);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Get references to View components
        mCurrentTempTV = (TextView) findViewById(R.id.temp_tv);
        mCurrentRadarTV = (ImageView) findViewById(R.id.radar_image);
        mLoadingProgress = (ProgressBar) findViewById(R.id.fetching_data_progress);

        //Get key from raw resource. /res/raw/keys.txt w/ key
        key = Keys.getKeyFromRawResource(this, R.raw.keys);

        if (key != null) {
            getMinneapolisTemp();
            getMinneapolisRadar();
        } else {
            Log.e(TAG, "Key can't be read from raw resource");
        }
    }

    private void getMinneapolisTemp() {

        //Create the URL. Note %s to insert your key String
        String baseTempURL = "http://api.wunderground.com/api/%s/conditions/q/MN/Minneapolis.json";
        String url = String.format(baseTempURL, key);

        RequestMinneapolisCurrentTemp tempTask = new RequestMinneapolisCurrentTemp();
        tempTask.execute(url);

        //Show progress bar - spinning wheel - to indicate app is working
        mLoadingProgress.setVisibility(ProgressBar.VISIBLE);
    }

    private void getMinneapolisRadar() {

        //Url to request radar for Minneapolis. Specify height and weight of image returned
        //The newmaps parameter is 1 for include basemap, 0 for just radar on transparent background
        String baseRadarURL = "http://api.wunderground.com/api/%s/radar/q/MN/Minneapolis.png?width-200&height=200&newmaps=1";
        String url = String.format(baseRadarURL, key);

        /* Save some code! Can replace these two lines with one
            RequestMinneapolisCurrentTemp tempTask = new RequestMinneapolisCurrentTemp();
            tempTask.execute(url);
         */

        new RequestMinneapolisRadarMap().execute(url);

        mLoadingProgress.setVisibility(ProgressBar.VISIBLE);
    }

    //Async inner class to request current MPLS conditions
    private class RequestMinneapolisCurrentTemp extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = connection.getInputStream();

                //Wrap in InputStreamReader, and then wrap that in a BufferedReader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream));

                //Read stream into String. Use StringBuilder to put mult lines together
                //Read lines in a loop until end of stream
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //Log.d(TAG, "line = " + line);
                    builder.append(line);
                }

                //Turn the StringBuilder into a String
                String responseString = builder.toString();

                Log.d(TAG, responseString);

                //Then parse this String into a JSON object
                //Return this JSONObject, and it will be delivered
                //to the onPostExecute method. onPostExecute method is
                //called automatically
                JSONObject json = new JSONObject(responseString);

                return json;

            } catch (Exception e) {
                //TODO handle with more granularity. At least 3 different exceptions could be thrown here
                Log.e(TAG, "Error fetching temperature data", e);
                return null;
            }

        }

        protected void onPostExecute(JSONObject json) {

            if (json != null) {

                //Hide ProgressBar now task is done
                mLoadingProgress.setVisibility(ProgressBar.INVISIBLE);

                try {
                    //If we make an invalid request, WU may return JSON describing our error. Check for that
                    if (json.getJSONObject("response").has("error") ) {
                        Log.e(TAG, "Error in response from WU " + json.getJSONObject("response")
                                .getJSONObject("error")
                                .getString("description"));
                        return;
                    }

                    //Hopefully we have JSON and it's not reporting an error. Try and read desired data
                    String temp_f = json.getJSONObject("current_observation").getString("temp_f");

                    //Update the TextView with the data
                    mCurrentTempTV.setText("The current temperature in Minneapolis is " + temp_f);

                } catch (JSONException je) {
                    Log.e(TAG, "JSON parsing error", je);
                }
            }

        }

    } //End of RequestMinneapolisCurrentTemp inner class

    //Async inner class for fetching weather radar
    private class RequestMinneapolisRadarMap extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = connection.getInputStream();
                Bitmap radar = BitmapFactory.decodeStream(responseStream);
                return radar;
            } catch (Exception e) {
                Log.e(TAG, "Request Minneapolis Radar Map error: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap radarBitmap) {
            if (radarBitmap == null) {
                Log.e(TAG, "Bitmap is null, check for errors from doInBackground");
            } else {
                mLoadingProgress.setVisibility(ProgressBar.INVISIBLE);
                mCurrentRadarTV.setImageBitmap(radarBitmap);
            }
        }
    } //End of RequestMinneapolisWeatherMap inner class

}
