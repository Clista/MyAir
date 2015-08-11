package com.example.callistaohnemus.mymap;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;


public class MapDemoActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks {
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setUpMap(latitude, longitude, AirQuality, address);
        }

        private void setUpMap(double latitude, double longitude, double AirQuality, String address) {

            int v = 51;
            int w = 101;
            int x = 151;
            int y = 201;
            int z = 301;

            if ((AirQuality < v)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(address)
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
            } else if ((AirQuality >= v) && (AirQuality < w)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(address)
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );
            } else if ((AirQuality >= w) && (AirQuality < x)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
            } else if ((AirQuality >= x) && (AirQuality < y)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );
            } else if ((AirQuality >= y) && (AirQuality < z)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                );
            } else if ((AirQuality >= z)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                );
            }
        }


        /**
         * Google api callback methods
         */

        public void onConnectionFailed(ConnectionResult result) {
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());
        }


    }
    private MyReceiver receiver;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 5;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String TAG = MapDemoActivity.class.getSimpleName();
    private static final String DEBUG_TAG = "HttpExample";
    public double latitude;
    public double longitude;
    public double AirQuality;
    InputStream is = null;
    String inputStream = null;
    String contentAsString;

    public Button get_location;

    String myurl = "https://api.thingspeak.com/apps/thinghttp/send_request?api_key=8Z1EQXGCJ0693HZL";




    public Marker marker;

    public Marker getMarker(){
        return this.marker;
    }

    public GoogleMap getmMap(){
        return this.mMap;
    }

    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    String address;
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_demo);
        //    setUpMapIfNeeded();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        get_location = (Button) findViewById(R.id.get_location);

        //thingspeak = (Button) findViewById(R.id.thingspeak);


        get_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                displayLocation();

                final Runnable runnable = new Runnable() {
                    public void run() {
                        address = getAddress(latitude, longitude);

                        if (mMap == null) {
                            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                                    .getMap();
                        }

                        if (address == null) {
                            address = "null";
                        }

                        Log.v("address received", address);

                        String s = null;

                        try {
                            s = downloadUrl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("thingspeak returns this", " "+ s);
                    }
                };

            Thread background_Thread = new Thread(runnable);
            background_Thread.start();





                if(mMap!=null)

            setUpMap(latitude, longitude, 70,address);


        }


    });

    // some time when u want to run
    Date when = new Date(System.currentTimeMillis());
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_VIEW");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);

    try{
        Intent someIntent = new Intent(this ,MyReceiver.class); // intent to be launched

        // note this could be getActivity if you want to launch an activity
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0, // id, optional
                someIntent, // intent to launch
                PendingIntent.FLAG_CANCEL_CURRENT); // PendintIntent flag

        AlarmManager alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);

        alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                when.getTime(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent);

    }catch(Exception e){
        e.printStackTrace();
    }



        /* thingspeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v("Thing is clicked", "Thing is clicked");

                final Runnable runnable = new Runnable() {
                    public void run() {




                    }
                };

                Thread background_Thread = new Thread(runnable);
                background_Thread.start();
            }
        });

        */




        onStart();
    }
    public String downloadUrl2() throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500000;
        URL url = null;
        try {

            url = new URL("https://api.thingspeak.com/update?key=EZALNV6Z3VQVKLUA&field1=" + String.valueOf(latitude) + "&field2=" + String.valueOf(longitude) + "&field3=t");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        int response = 400000;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);


            Log.v("contentAsString", contentAsString);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return contentAsString;}

    public String downloadUrl() throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500000;
        URL url = null;
        try {

            //url = new URL("https://api.thingspeak.com/update?key=EZALNV6Z3VQVKLUA&field1=" + String.valueOf(latitude) + "&field2=" + String.valueOf(longitude) + "&field3=t");
            url = new URL("https://api.thingspeak.com/channels/49126/feed.json?key=EZALNV6Z3VQVKLUA");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        int response = 400000;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);

            Log.v("contentAsString", contentAsString);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Log.d("Response tag", "The response is: " + response);
        // HttpResponse response = httpRequest("https://api.thingspeak.com/update?key=EZALNV6Z3VQVKLUA&lat="+String.valueOf(latitude)+"&lon="+String.valueOf(longitude)+"field3=t");
        Log.v("Lat thingspeak", String.valueOf(latitude));

        String string = null;

        URL url2 = null;
        try {


            url2 = new URL("https://api.thingspeak.com/channels/49126/feed.json&field1=" + String.valueOf(latitude) + "&field2=" + String.valueOf(longitude) + "&field3=t");

        }
        catch(
                MalformedURLException e
                )

        {
            e.printStackTrace();
        }

        HttpURLConnection conn2 = null;
        int response2 = 400000;
        try

        {
            conn2 = (HttpURLConnection) url.openConnection();
            conn2.setReadTimeout(10000 /* milliseconds */);
            conn2.setConnectTimeout(15000 /* milliseconds */);
            conn2.setRequestMethod("GET");
            conn2.setDoInput(true);
            // Starts the query
            conn2.connect();
            response2 = conn2.getResponseCode();
        }

        catch(
                IOException e
                )

        {
            e.printStackTrace();
        }

        Log.d("Response tag","The response is: "+response2);
        // HttpResponse response = httpRequest("https://api.thingspeak.com/update?key=EZALNV6Z3VQVKLUA&lat="+String.valueOf(latitude)+"&lon="+String.valueOf(longitude)+"field3=t");
        Log.v("Lat thingspeak",String.valueOf(latitude));

        String string2 = null;

        return contentAsString;}

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

        }
    }



    private String getAddress(double latitude, double longitude) {
        String latitude_string = Double.toString(latitude);
        String longitude_string = Double.toString(longitude);
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude_string+","+longitude_string+"&location_type=ROOFTOP&result_type=street_address&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o"));
            response = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String string = null;

        try {
            string = convertStreamToString(response.getEntity().getContent());
            Log.v("convert string into",string);
        } catch (IOException e) {
            e.printStackTrace();

        }

        String[] parse_array = new String[2];
        parse_array = string.split("\"formatted_address\" : \"");
        String[] second_parse_array = new String[2];
        second_parse_array = parse_array[1].split("geometry");
        String [] fourth_parse_array = new String[2];
        fourth_parse_array = second_parse_array[0].split("\",");
        String actual_address = fourth_parse_array[0];
        Log.v("actual address", actual_address);


        return actual_address;



    }

    public void onConnected(Bundle connectionHint) {

        displayLocation();
    }

    public HttpResponse httpRequest(String uri) {
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            Log.v("HTTPCLient Error", uri);
            response = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

        public String convertStreamToString(InputStream inputStream) throws IOException {
            if (inputStream != null) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    inputStream.close();
                }
                return writer.toString();
            } else {
                return "";
            }
        }

        public void onConnectionSuspended(int arg0) {
            mGoogleApiClient.connect();


        }

        @Override
        protected void onResume() {
            super.onResume();

            checkPlayServices();
            //      setUpMapIfNeeded();
        }

        /**
         * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
         * installed) and the map has not already been instantiated.. This will ensure that we only ever
         * call {@link #setUpMap()} once when {@link #mMap} is not null.
         * <p/>
         * If it isn't installed {@link SupportMapFragment} (and
         * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
         * install/update the Google Play services APK on their device.
         * <p/>
         * A user can return to this FragmentActivity after following the prompt and correctly
         * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
         * have been completely destroyed during this process (it is likely that it would only be
         * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
         * method in {@link #onResume()} to guarantee that it will be called.
         */
  /*  private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
*/

        /**
         * This is where we can add markers or lines, add listeners or move the camera. In this case, we
         * just add a marker near Africa.
         * <p/>
         * This should only be called once and when we are sure that {@link #mMap} is not null.
         */
        private void setUpMap(double latitude, double longitude, double AirQuality, String address) {

            int v = 51;
            int w = 101;
            int x = 151;
            int y = 201;
            int z = 301;

            if ((AirQuality < v)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(address)
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
            } else if ((AirQuality >= v) && (AirQuality < w)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(address)
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );
            } else if ((AirQuality >= w) && (AirQuality < x)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
            } else if ((AirQuality >= x) && (AirQuality < y)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );
            } else if ((AirQuality >= y) && (AirQuality < z)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                );
            } else if ((AirQuality >= z)) {
                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("https://maps.googleapis.com/maps/api/geocode/json?latlng=latitude,longitude&key=AIzaSyBYTvr2xZeqKKT_KHB7bfzj1AAP71d7o3o")
                                .snippet("Population: 8.174 million (2011)")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                );
            }
        }


        /**
         * Google api callback methods
         */

        public void onConnectionFailed(ConnectionResult result) {
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());
        }


}

