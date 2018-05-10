package com.tyler.fetcher;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;

public class DogParkListActivity extends SingleFragmentActivity {

    private static final String EXTRA_GOOGLE_USERNAME = "username";
    private static final String EXTRA_GOOGLE_EMAIL = "email";

    public static Intent newIntent(Context packageContext, String username, String email) {
        Intent intent = new Intent(packageContext, DogParkListActivity.class);
        intent.putExtra(EXTRA_GOOGLE_USERNAME, username);
        intent.putExtra(EXTRA_GOOGLE_EMAIL, email);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        String username = getIntent().getStringExtra(EXTRA_GOOGLE_USERNAME);
        String email = getIntent().getStringExtra(EXTRA_GOOGLE_EMAIL);
        return DogParkListFragment.newInstance(username, email);
    }
}
