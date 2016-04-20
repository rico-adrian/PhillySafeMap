package com.example.ryan.phillysafemap;

/**
 * Created by Tri on 3/12/16.
 */
    import android.app.Activity;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.View;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.EditText;
    import com.firebase.client.AuthData;
    import com.firebase.client.Firebase;
    import com.firebase.client.FirebaseError;
//        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//        import com.google.android.gms.auth.api.signin.SignInAccount;
//        import com.google.android.gms.common.ConnectionResult;
//        import com.google.android.gms.common.SignInButton;
//        import com.google.android.gms.common.api.Api;
//        import com.google.android.gms.common.api.GoogleApiClient;
    import android.widget.Button;
    import android.widget.Toast;
    import android.content.Intent;
    import android.content.Context;
    import java.util.Timer;

    import java.util.TimerTask;

//        import com.google.android.gms.auth.api.*;
//        import com.google.android.gms.common.api.PendingResult;
//        import com.google.android.gms.common.api.Status;
//        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//        import com.google.android.gms.auth.api.Auth;
//        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

//        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//        import com.google.android.gms.common.ConnectionResult;
//        import com.google.android.gms.common.SignInButton;
//        import com.google.android.gms.common.api.GoogleApiClient;
//        import com.google.android.gms.common.api.OptionalPendingResult;
//        import com.google.android.gms.common.api.ResultCallback;
//        import com.google.android.gms.common.api.Status;

    public class LogInActivity extends Activity {
        EditText muserId;
        EditText mpassword;
        Button mlogIn, msignup1;
        Firebase mRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            Firebase.setAndroidContext(this);

            muserId = (EditText) findViewById(R.id.userId);
            mpassword = (EditText) findViewById(R.id.password);
            mlogIn = (Button) findViewById(R.id.login);
            msignup1 = (Button) findViewById(R.id.signup1);
            final Context context = this;
            mRef = new Firebase("https://phillysafemap.firebaseio.com");

            //Handle the login button when clicked
            mlogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String muserIdText = muserId.getText().toString();
                    String mpasswordText = mpassword.getText().toString();
                    mRef.authWithPassword(muserIdText, mpasswordText, new Firebase.AuthResultHandler() {
                        @Override
                        //Log the user in
                        public void onAuthenticated(AuthData authData) {
                            Toast.makeText(LogInActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MapsActivity.class);
                            startActivity(intent);

                        }

                        //If there's error, pop up a message
                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(LogInActivity.this, "Incorrect email/password", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            });

            //Handle the sign up button
            msignup1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LogInActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, SignUpActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);
                }
            });

        }
    }
