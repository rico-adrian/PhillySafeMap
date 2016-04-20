package com.example.ryan.phillysafemap;

import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.firebase.client.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGPSTracker;
    private double mLatitude, mLongitude;
    private static final String TAG = AsyncTaskCrime.class.getSimpleName();
    List<Crime> currentCrimeList = new ArrayList<Crime>();
    List<Crime> masterCrimeList = new ArrayList<Crime>();
    ImageButton mImageButton;
    protected boolean[] isChecked = {
            true,
            true,
            true,
            true,
            true,
            true,
            true
    };
    Map<String, Boolean> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if(getIntent().getExtras() != null){
            isChecked = getIntent().getExtras().getBooleanArray("yourBool");
            for(int i=0; i<isChecked.length; i++){
                Log.i(TAG, isChecked[i]?"MapsActivity true":"MapsActivity false");
            }
        }

        map.put("100", isChecked[0]);
        map.put("200", isChecked[1]);
        map.put("300", isChecked[2]);
        map.put("400", isChecked[3]);
        map.put("500", isChecked[4]);
        map.put("600", isChecked[5]);
        map.put("700", isChecked[6]);

        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key+" : "+value);
        }

        // initiate the map
        if (initMap()) {
            mGPSTracker = new GPSTracker(this);
            if (mGPSTracker.canGetLocation()) {
                mLatitude = mGPSTracker.getLatitude();
                mLongitude = mGPSTracker.getLongitude();

                /*
                Drexel lat: 38.479459, long: -94.608566
                Toast.makeText(
                        getApplicationContext(),
                        "Your Location is -\nLat: " + mLatitude + "\nLong: "
                                + mLongitude, Toast.LENGTH_LONG).show();
                                */

                LatLng latLng = new LatLng(mLatitude, mLongitude);
                MarkerOptions options = new MarkerOptions()
                        .title("My Location")
                        .position(latLng);
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                /*
                 * verify if last call is recent - in the past 24 hours
                 * if it is, instead of making network call to PHL API
                 * the app will get the data from FireBase database
                 */
                recentlyQueried();
                // if last call was more than 24 hours ago, get data from API
                if(!LastAccess.getIsRecent()) {
                    Toast.makeText(getApplicationContext(), "Making network call!",
                            Toast.LENGTH_LONG).show();

                    int UCR;
                    String crimesURL;
                    for (UCR = 100; UCR < 800; UCR = UCR + 100) {
                        crimesURL = "http://gis.phila.gov/ArcGIS/rest/services/PhilaGov/Police_Incidents_Last30/MapServer/0/query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&where=SECTOR%3D%273%27+AND+UCR_GENERAL%3D%27" + UCR + "%27&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry=true&maxAllowableOffset=&outSR=&outFields=*&f=pjson";

                        // execute network call in AsyncTaskCrime (extends AsyncTask) and transfer those data back to MapsActivity
                        AsyncTaskCrime feedTask = new AsyncTaskCrime(crimesURL);
                        try {
                            currentCrimeList = feedTask.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        masterCrimeList.addAll(currentCrimeList);
                    }

                    // populate map with Crime markers
                    if(masterCrimeList != null) {
                        for (Crime crime : masterCrimeList) {
                            createMarker(crime);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error: No Crimes to show",
                                Toast.LENGTH_LONG).show();
                    }

                    // upload data to Firebase database
                    uploadToDatabase(masterCrimeList);
                }
                else {
                    // if last call was less than 24h, get data from database instead of making network call to API
                    getFromDatabase();
                }
            } else {
                mGPSTracker.showSettingsAlert();
            }
        } else {
            Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    // get Crimes data back from Firebase Database
    private void getFromDatabase() {
        Firebase.setAndroidContext(this);
        Firebase crimeRef = new Firebase("https://blistering-inferno-7336.firebaseio.com/");

        // upload a dummy instance of Crime to trigger onDataChange()
        Firebase crimeListRef = crimeRef.child("list");
        Map<String, String> entry = new HashMap<String, String>();
        entry.put("date", "dummy");
        entry.put("time", "dummy");
        entry.put("location", "dummy");
        entry.put("description", "dummy");
        entry.put("longitude", "0");
        entry.put("latitude", "0");
        entry.put("ucr", "100");
        crimeListRef.push().setValue(entry);

        crimeListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(), "Database Loaded!",
                        Toast.LENGTH_LONG).show();

                Log.v("myApp", "There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Crime crime = postSnapshot.getValue(Crime.class);
                    createMarker(crime);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    // verify if the last call is made recently (in the last 24 hours)
    private void recentlyQueried() {
        final boolean[] isRecent = new boolean[1];

        Firebase.setAndroidContext(this);
        Firebase crimeRef = new Firebase("https://blistering-inferno-7336.firebaseio.com/");
        // send 1 instance of Crime to trigger onDataChange()
        Firebase crimeListRef = crimeRef.child("list");
        Map<String, String> entry = new HashMap<String, String>();
        entry.put("date", "dummy");
        entry.put("time", "dummy");
        entry.put("location", "dummy");
        entry.put("description", "dummy");
        entry.put("longitude", "0");
        entry.put("latitude", "0");
        entry.put("ucr", "100");
        crimeListRef.push().setValue(entry);

        crimeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.v("myApp", "" + snapshot.getChildrenCount());

                Date last = snapshot.child("lastQueried").child("date").getValue(Date.class);
                //Log.v("myApp", last.toString());
                Date now = new Date();

                long diff = Math.abs(now.getTime() - last.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays < 1)
                    isRecent[0] = true;
                else
                    isRecent[0] = false;

                Log.v("myApp", "" + isRecent[0]);
                LastAccess.setIsRecent(isRecent[0]);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        Log.v("myApp1", "" + isRecent[0]);
    }

    private void uploadToDatabase(List<Crime> crimeList) {
        Firebase.setAndroidContext(this);
        Firebase crimeRef = new Firebase("https://blistering-inferno-7336.firebaseio.com/");
        Firebase crimeListRef = crimeRef.child("list");
        // reset database before adding
        crimeListRef.removeValue();
        Firebase lastRef = crimeRef.child("lastQueried").child("date");

        Map<String, String> entry = new HashMap<String, String>();
        for (Crime crime : crimeList)
        {
            entry.put("date", crime.getDate());
            entry.put("time", crime.getTime());
            entry.put("location", crime.getLocation());
            entry.put("description", crime.getDescription());
            entry.put("longitude", crime.getLongitude());
            entry.put("latitude", crime.getLatitude());
            entry.put("ucr", crime.getUCR());

            crimeListRef.push().setValue(entry);
        }

        Date lastQueried = new Date();
        lastRef.setValue(lastQueried);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //setting button
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < isChecked.length; i++) {
                    Log.i(TAG, isChecked[i] ? "MapsActivity true" : "MapsActivity false");
                }
                //Toast.makeText(MapsActivity.this, "MapsActivity Settings", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(getApplicationContext(), SortActivity.class);
                mIntent.putExtra("yourBool", isChecked);
                startActivity(mIntent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Add menu handling code
        switch (id) {
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 14.0f));
    }

    //Getting a reference to the map object
    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();

            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                        TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                        tvLocality.setText(marker.getTitle());
                        tvSnippet.setText(marker.getSnippet());

                        return v;
                    }
                });
            }
        }
        return (mMap != null);
    }

    private void createMarker(Crime crime) {
        LatLng latLng = new LatLng(Double.parseDouble(crime.getLatitude()), Double.parseDouble(crime.getLongitude()));
        MarkerOptions options = new MarkerOptions();
        int badge = 0;
        String ucr = crime.getUCR();
        if(map.get(ucr) == true){
            switch (ucr) {
                case "100":
                    badge = R.drawable.small_1;
                    break;
                case "200":
                    badge = R.drawable.small_2;
                    break;
                case "300":
                    badge = R.drawable.small_3;
                    break;
                case "400":
                    badge = R.drawable.small_4;
                    break;
                case "500":
                    badge = R.drawable.small_5;
                    break;
                case "600":
                    badge = R.drawable.small_6;
                    break;
                case "700":
                    badge = R.drawable.small_7;
                    break;
            }
            options
                    .title(crime.getDescription())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(badge))
                    .snippet("Location: "+crime.getLocation()+"\nDate: "+crime.getDate()+"\nTime: "+crime.getTime());
            mMap.addMarker(options);
        }else {
            switch (ucr) {
                case "100":
                    badge = R.drawable.small_1;
                    break;
                case "200":
                    badge = R.drawable.small_2;
                    break;
                case "300":
                    badge = R.drawable.small_3;
                    break;
                case "400":
                    badge = R.drawable.small_4;
                    break;
                case "500":
                    badge = R.drawable.small_5;
                    break;
                case "600":
                    badge = R.drawable.small_6;
                    break;
                case "700":
                    badge = R.drawable.small_7;
                    break;
            }
            options
                    .title(crime.getDescription())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(badge))
                    .visible(false)
                    .snippet("Location: " + crime.getLocation() + "\nDate: " + crime.getDate() + "\nTime: " + crime.getTime());
            mMap.addMarker(options);
        }
    }

    public void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    //Implement geoLocate here...
    public void geoLocate(View v) throws IOException {

        hideSoftKeyboard(v);

        TextView tv = (TextView) findViewById(R.id.editText1);
        String searchString = tv.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            Address add = list.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found: " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, 15);
        }

    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // Mahmuda's Part

    /*
     * Cloudmine Parsing the crime objects. If ever needed to use
     * There would be a boolean that keeps track of timer. if more than 24 hour do the json parsing and populate cloudmine
     * else don't parse jason, get data from cloudmine.
     * This snippet of code should be inside onCreate I think.
     * */
    /*
     * if(...){
        for (Crime crime : mCrimeList) {
            createMarker(Double.parseDouble(crime.getLatitude()), Double.parseDouble(crime.getLongitude()), crime.getDescription());

            // initialize CloudMine library
            CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
            for (Crime crime : mCrimeList) {
                crimeFromCloud.add(temp1);
                crimeFromCloud.add(temp2);
                CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
                String searchString = SearchQuery.filter("__class__").equal("Crime").searchQuery();
                LocallySavableCMObject.searchObjects(this, searchString, new Response.Listener<CMObjectResponse>() {
                @Override
                public void onResponse(CMObjectResponse objectResponse) {
                List<Crime> crimes = objectResponse.getObjects(Crime.class);
                for (int i = 0; i < crimes.size(); i++) {
                    crimeFromCloud.add(new Crime(
                            crimes.get(i).date,
                            crimes.get(i).time,
                            crimes.get(i).location,
                            crimes.get(i).description,
                            crimes.get(i).longitude,
                            crimes.get(i).latitude));
                }
                }});
            }
        }
    }
     */
}
