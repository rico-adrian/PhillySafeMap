package com.example.ryan.phillysafemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SortActivity extends Activity {

    private ListView list;
    private String[] crimeTypes = {
            "Homicide",
            "Rape",
            "Robbery",
            "Agggravated Assault",
            "Burglary",
            "Theft",
            "Vehicle Theft "
    } ;

    private Integer[] imageId = {
            R.drawable.rsz_1,
            R.drawable.rsz_2,
            R.drawable.rsz_3,
            R.drawable.rsz_4,
            R.drawable.rsz_5,
            R.drawable.rsz_6,
            R.drawable.rsz_7
    };

    protected boolean[] isChecked = {
            true,
            true,
            true,
            true,
            true,
            true,
            true
    };

    boolean[] MyBooleanArray = new boolean[7];

//    private boolean[] isChecked = new boolean[7];

    private static final String TAG = SortActivity.class.getSimpleName();


    CustomList adapter = null;
//    private CheckBox mCheckBox;
//    private ImageButton mImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        if(getIntent().getExtras() != null){
            isChecked = getIntent().getExtras().getBooleanArray("yourBool");
            for(int i=0; i<isChecked.length; i++){
                Log.i(TAG, isChecked[i]?"SortActivity true":"SortActivity false");
            }
        }

        adapter = new
                CustomList(SortActivity.this, crimeTypes, imageId, isChecked);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), "You Clicked on " + crimeTypes[+position], Toast.LENGTH_SHORT).show();
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
                for(int i=0; i<isChecked.length; i++){
                    Log.i(TAG, isChecked[i]?"Save true":"Save false");
                }
                Toast.makeText(SortActivity.this, "Saved Settings", Toast.LENGTH_SHORT).show();

                Intent mIntent = new Intent(getApplicationContext(), MapsActivity.class);
                mIntent.putExtra("yourBool", isChecked);
                startActivity(mIntent);
            }
        });

        //return to map button
        ImageButton mImageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SortActivity.this, "Go to Map", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mIntent);
            }
        });

        //date selection/picker
        ImageButton mImageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SortActivity.this, "Go to Date", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), PhoneActivity.class);
                startActivity(mIntent);
            }
        });

        //sign up/email
        ImageButton mImageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SortActivity.this, "Go to Phone", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivity(mIntent);
            }
        });

        //go to source(website)
        ImageButton mImageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SortActivity.this, "Go to Website", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), WebsiteActivity.class);
                startActivity(mIntent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public class CustomList extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] crimeTypes;
        private final Integer[] imageId;
        private final boolean[] isChecked;

        public CustomList(Activity context, String[] crimeTypes, Integer[] imageId, boolean[] isChecked) {
            super(context, R.layout.custom_template, crimeTypes);
            this.context = context;
            this.crimeTypes = crimeTypes;
            this.imageId = imageId;
            this.isChecked = isChecked;
        }

        private class ViewHolder {
            ImageView mImageView;
            CheckBox mCheckBox;
            TextView mTextView;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ViewHolder holder = null;

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.custom_template, null, true);

            holder = new ViewHolder();
            holder.mImageView = (ImageView) rowView.findViewById(R.id.imageButton7);
            holder.mCheckBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            holder.mTextView = (TextView) rowView.findViewById(R.id.textView);
            rowView.setTag(holder);

            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    //Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                    //        Toast.LENGTH_SHORT).show();
                }
            });

            MyClickListener myClickListener = new MyClickListener(position, isChecked);

            holder.mCheckBox.setOnClickListener(myClickListener);

                holder.mTextView.setText(crimeTypes[position]);
                holder.mImageView.setImageResource(imageId[position]);
                holder.mCheckBox.setChecked(isChecked[position]);
                return rowView;
            }
        }
}
