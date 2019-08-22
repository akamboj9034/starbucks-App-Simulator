package com.nihalkonda.starbuckapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class GmailActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail);

        if(gmail()){
            setContentView(R.layout.activity_contacting_server);
            return;
        }

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut();

        startActivity(new Intent(this,GmailActivity.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task.getResult());
        }
    }

    private void handleSignInResult(GoogleSignInAccount gmailAccount) {
        Log.i("Gmail_ID",gmailAccount.getId());
        Log.i("Gmail_Email",gmailAccount.getEmail());
        Log.i("Gmail_GivenName",gmailAccount.getGivenName());

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        GodClass.getInstance().loadUser(GmailActivity.this,gmailAccount.getEmail());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        signOut();
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
                builder
                .setMessage("Do you want to continue as "+gmailAccount.getEmail()+"?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();


    }

    private boolean gmail(){

        try {

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            handleSignInResult(account);

            return true;

        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this,"Sorry, This Feature Isn't Available Right Now.",Toast.LENGTH_SHORT).show();
    }


}
