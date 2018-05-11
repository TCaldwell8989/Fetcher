package com.tyler.fetcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;

public class EntryFormFragment extends Fragment {

    private static final String TAG = "Main Activity";
    private static final String ARG_DOG_PARK_ID = "dog_park_id_arg";

    private DogPark mDogPark;
    private EditText mNameET;
    private EditText mLocationET;
    private EditText mNoteET;
    private RatingBar mRating;
    private UUID mDogParkId;

    public static EntryFormFragment newInstance(UUID dog_park_Id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOG_PARK_ID, dog_park_Id);

        EntryFormFragment fragment = new EntryFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDogParkId = (UUID) getArguments().getSerializable(ARG_DOG_PARK_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        mDogPark = DogHouse.get(getActivity()).getDogPark(mDogParkId);

                //Get references to View components
        mNameET = (EditText) view.findViewById(R.id.name_et);
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

        mLocationET = (EditText) view.findViewById(R.id.location_et);
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

        mNoteET = (EditText) view.findViewById(R.id.note_et);
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

        mRating = (RatingBar) view.findViewById(R.id.dog_friendly_rating_bar);
        mRating.setRating(mDogPark.getRating());
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mDogPark.setRating(rating);
            }
        });

        return view;

    }




    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Dog Park Note: " + mDogPark.getNote());
        DogHouse.get(getActivity()).updateDogPark(mDogPark);

    }

}




