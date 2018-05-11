package com.tyler.fetcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;


public class EntryFormActivity extends SingleFragmentActivity {

    private static final String EXTRA_DOGPARK_ID = "dog_park_Id";

    public static Intent newIntent(Context packageContext,  UUID dogParkId) {
        Intent intent = new Intent(packageContext, EntryFormActivity.class);
        intent.putExtra(EXTRA_DOGPARK_ID, dogParkId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        UUID dog_park_Id = (UUID) getIntent().getSerializableExtra(EXTRA_DOGPARK_ID);
        return EntryFormFragment.newInstance(dog_park_Id);
    }
}