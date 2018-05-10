package com.tyler.dogfetcher;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//File to delegate reading the key
public class Keys {

    //Returns a key, or null if file not found or can't be read
    protected static String getKeyFromRawResource(Context context, int rawResource) {

        //Create a stream reader for this raw resource
        InputStream keyStream = context.getResources().openRawResource(rawResource);
        //And a BuggeredReader to read file into lines of text
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try {
            //Read in one line of text
            String key = keyStreamReader.readLine();
            return key;
        } catch (IOException ioe) {
            return null;
        }

    }

}
