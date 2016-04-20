package com.example.ryan.phillysafemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;import com.example.ryan.phillysafemap.MapsActivity;import com.example.ryan.phillysafemap.R;import java.lang.Override;

public class WebsiteActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://phlapi.com/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*
        //save setting button
        ImageButton mImageButton1 = (ImageButton) findViewById(R.id.imageButton8);
        mImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WebsiteActivity.this, "Saved Settings", Toast.LENGTH_SHORT).show();
            }
        });
        */

        //return to map button
        ImageButton mImageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WebsiteActivity.this, "Go to Map", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mIntent);
            }
        });

        //date selection/picker
        ImageButton mImageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WebsiteActivity.this, "Go to Date", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), PhoneActivity.class);
                startActivity(mIntent);
            }
        });

        //sign up/email
        ImageButton mImageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WebsiteActivity.this, "Go to Email", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivity(mIntent);
            }
        });

        //go to source(website)
        ImageButton mImageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WebsiteActivity.this, "Go to Website", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), WebsiteActivity.class);
                startActivity(mIntent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
