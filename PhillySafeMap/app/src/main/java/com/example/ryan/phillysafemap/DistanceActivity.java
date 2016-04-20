package com.example.ryan.phillysafemap;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* This activity class opens a page after user click 'add destination' button.
* The class will provide 'source' and 'destrination' edit textview with a search button
* upon pressing which user will see the crimes that happen between the two locaition they input.
* This class uses most of the functionality from MapsActivity and gets directed from that activity after
* an specific button is clicked. Please help us finish implementing this.
*/
public class DistanceActivity extends Activity {
    MapsActivity m = new MapsActivity();
    private Bundle savedInstanceState;


    @Override
    public void onCreate(Bundle savedInstanceState){
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        EditText et_source = (EditText) findViewById(R.id.editText1);
        EditText et_dest = (EditText) findViewById(R.id.editText2);
        Button searchButton = (Button) findViewById(R.id.button2);
        sourceText = et_source.getEditableText().toString();
        destText = et_dest.getEditableText().toString();

        try {
            c1 = castToLocation(sourceText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            c2 = castToLocation(destText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the List<Crime> given two location c1 and c2
                findCrimesBetween(c1, c2, mCrimeList);

            }
        });
    }

    // given two location object, finds the crimes that occured between the two points
    public List<Crime> findCrimesBetween(Location c1, Location c2, List<Crime> crimeList){
        List<Crime> crimeListBetween = new ArrayList<Crime>();
        Crime crime = new Crime();

        double lat1 = c1.getLatitude();
        double long1 = c1.getLongitude();
        double lat2 = c2.getLatitude();
        double long2 = c2.getLongitude();

        double minLat = Math.min(lat1, lat2);
        double maxLat = Math.max(lat1, lat2);
        double minLong = Math.min(long1, long2);
        double maxLong = Math.max(long1, long2);

        for(int i = 0; i < crimeList.size(); i++) {
            crime = crimeList.get(i);
            double tempLat = Double.parseDouble(crime.getLatitude());
            if (minLat < tempLat &&
                    maxLat > tempLat) {
                double tempLong = Double.parseDouble(crime.getLongitude());
                if (minLong < tempLong &&
                        maxLong> tempLong) {
                    crimeListBetween.add(crime);
                }
            }
        }
        return crimeListBetween;
    }

    public Location castToLocation(String address) throws IOException {
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(address, 1);
        if (list.size() > 0) {
            Address add = list.get(0);
            double lat = add.getLatitude();
            double lng = add.getLongitude();
            m.gotoLocation(lat, lng, 15);
            return new Location(lat, lng);
        }
        return null;
    }

    Location c1 = null;
    Location c2 = null;
    String sourceText;
    String destText;

    List<Crime> crimeFromCloud = new ArrayList<Crime>();

    private GoogleMap mMap;
    private GPSTracker mGPSTracker;
    private double mLatitude, mLongitude;
    private static final String TAG = AsyncTaskCrime.class.getSimpleName();
    List<Crime> mCrimeList = new ArrayList<Crime>();

    /*
    * Straight copy the following functions from MapsActivity if the other alternative to
    * connecting to MapActivity and getting its method fails.
    * public void onMapReady(GoogleMap googleMap) {}
    *   private boolean initMap() {}
    *   private void createMarker(double latitude, double longitude, String locality) {}
    *   public void gotoLocation(double lat, double lng, float zoom) {}
    *   public void geoLocate(View v) throws IOException {}
    *       private void hideSoftKeyboard(View v) {}
    * */
//1
}
