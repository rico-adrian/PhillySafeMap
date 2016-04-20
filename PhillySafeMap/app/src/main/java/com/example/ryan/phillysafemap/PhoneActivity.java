package com.example.ryan.phillysafemap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;import com.example.ryan.phillysafemap.MapsActivity;import com.example.ryan.phillysafemap.R;import com.example.ryan.phillysafemap.WebsiteActivity;import java.lang.Override;import java.lang.String;

public class PhoneActivity extends Activity {

    final Context context = this;
    private Button buttonCall, buttonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonText = (Button) findViewById(R.id.buttonText);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

        // add buttonCall listener
        buttonCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                callIntent.setData(Uri.parse("tel:2673073323"));
                startActivity(callIntent);
//                finish();

            }

        });

        buttonText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:2673073323"));
                intent.putExtra("sms_body", "HELP ME! THIS IS AN EMERGENCY");
                startActivity(intent);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //save setting button
        ImageButton mImageButton1 = (ImageButton) findViewById(R.id.imageButton8);
        mImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PhoneActivity.this, "Saved Settings", Toast.LENGTH_SHORT).show();
            }
        });

        //return to map button
        ImageButton mImageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PhoneActivity.this, "Go to Map", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mIntent);
            }
        });

        //date selection/picker
        ImageButton mImageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PhoneActivity.this, "Go to Date", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), PhoneActivity.class);
                startActivity(mIntent);
            }
        });

        //sign up/email
        ImageButton mImageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PhoneActivity.this, "Go to Email", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivity(mIntent);
            }
        });

        //go to source(website)
        ImageButton mImageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PhoneActivity.this, "Go to Website", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), WebsiteActivity.class);
                startActivity(mIntent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                    Intent k = new Intent(getApplicationContext(), PhoneActivity.class);
                    startActivity(k);

                    isPhoneCalling = false;
                }

            }
        }
    }

}


