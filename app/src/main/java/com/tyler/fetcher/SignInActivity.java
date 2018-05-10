package com.tyler.fetcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 0;

    //Global Variables
    private static final String TAG = "SIGN_IN_ACTIVITY";
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Get references to components
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        //Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile and included in DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result containing a GoogleSignInAccount object if user signed in to google successfully
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Log.d(TAG, "Signed in with " + account.getDisplayName());
            //User already signed in. Hide sign in TextView and Button
            mSignInButton.setVisibility(View.INVISIBLE);
            String username = account.getDisplayName();
            String email = account.getEmail();
            Intent intent = DogParkListActivity.newIntent(this, username, email);
            startActivity(intent);
        } else {
            //User not signed in. Show sign in TextView and Button
            Log.d(TAG, "Not signed in");
            Toast.makeText(this, "Go Fetch", Toast.LENGTH_LONG).show();
            mSignInButton.setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = new Intent();
        signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //Signed in successfully, show authenticated UI
            updateUI(account);
        } catch (ApiException e) {
            //ApiException.getStatusCode() gives the detailed failure reason
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

}
