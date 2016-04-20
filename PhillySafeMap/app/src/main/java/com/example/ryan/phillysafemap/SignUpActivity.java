package com.example.ryan.phillysafemap;
 import android.app.Activity;
 import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
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
        import android.content.Context;
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
        import java.util.Map;
        import java.util.Timer;
        import java.util.TimerTask;
        import android.content.Intent;

public class SignUpActivity extends Activity {
    EditText muserId1, mpassword1, mconfirmpassword;
    Button msignup1;
    String clientId = "858652444720-a41uv9hf9pgn0d0mp5r1o57d2sc9sme7.apps.googleusercontent.com";
    Firebase mRef;
    private static final int RC_SIGN_IN = 9001;
    final Context context  = this;
//    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase.setAndroidContext(this);

        muserId1 = (EditText) findViewById(R.id.userId1);
        mpassword1 = (EditText) findViewById(R.id.password1);
        mconfirmpassword = (EditText) findViewById(R.id.confirmpassword);
        msignup1 = (Button) findViewById(R.id.signup2);
        mRef = new Firebase("https://phillysafemap.firebaseio.com");

        //Handle the signup button
        msignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String muserIdText = muserId1.getText().toString();
                String mpasswordText = mpassword1.getText().toString();
                String mconfirmpasswordText = mconfirmpassword.getText().toString();

                if (!(mpasswordText.equals(mconfirmpasswordText))) {
                    Toast.makeText(SignUpActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    System.out.println(mconfirmpasswordText);
                    System.out.println(mpasswordText);
                } else {
                    mRef.createUser(muserIdText, mpasswordText, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
                            Toast.makeText(SignUpActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Back to the login page", Toast.LENGTH_SHORT).show();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(context, LogInActivity.class);
                                    startActivity(intent);
                                }
                            }, 2000);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            String error = firebaseError.getMessage();
                            Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
}
