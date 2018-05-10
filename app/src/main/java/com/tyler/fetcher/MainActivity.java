package com.tyler.fetcher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private static final String EXTRA_GOOGLE_ACCOUNT =
            "com.tyler.mainactivity.google_account";
    private static final String EXTRA_DOGPARK_ID =
            "com.tyler.mainactivity.dogpark_id";

    private DogPark mDogPark;
    private EditText mNameET;
    private EditText mLocationET;
    private EditText mNoteET;
    private RatingBar mRating;

    public static Intent newIntent(Context packageContext, GoogleSignInAccount account, UUID dogParkId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_GOOGLE_ACCOUNT, account);
        intent.putExtra(EXTRA_DOGPARK_ID, dogParkId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UUID parkId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DOGPARK_ID);
        GoogleSignInAccount account = (GoogleSignInAccount) getIntent()
                .getSerializableExtra(EXTRA_GOOGLE_ACCOUNT);

        mDogPark = DogHouse.get(getApplicationContext()).getInspiration(parkId);

        //Get references to View components
        mNameET = (EditText) findViewById(R.id.name_et);
        mNameET.setText(mDogPark.getName());
        mNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDogPark.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationET = (EditText) findViewById(R.id.location_et);
        mLocationET.setText(mDogPark.getLocation());
        mLocationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDogPark.setLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNoteET = (EditText) findViewById(R.id.note_et);
        mNoteET.setText(mDogPark.getNote());
        mNoteET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDogPark.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRating = (RatingBar) findViewById(R.id.dog_friendly_rating_bar);
        mRating.setRating(mDogPark.getRating());
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mDogPark.setRating(rating);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_fetch_spot:
                //TODO
                break;
            case R.id.search:
                //TODO
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Dog Park Note: " + mDogPark.getNote());
        DogHouse.get(getApplicationContext()).updateDogPark(mDogPark);

    }

}
