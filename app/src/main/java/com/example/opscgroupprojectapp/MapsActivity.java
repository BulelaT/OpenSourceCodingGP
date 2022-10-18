package com.example.opscgroupprojectapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList markerPoints = new ArrayList();
    private LatLng destination = new LatLng(MainActivity.lati, MainActivity.longi);
    public static String distance ="";
    public static String duration ="";

    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;

    //(Mehta, Kanani and Lande, 2019)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //Get users current location Code
        //https://www.youtube.com/watch?v=p0PoKEPI65o
        //====================================================================================
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission is granted call get current location method
            //getCurrentLocation();
        } else {
            //When permission is denied request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //====================================================================================

    }



    //Get users current location
    //https://www.youtube.com/watch?v=p0PoKEPI65o
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When permission granted call the get current location method
                //getCurrentLocation();
            }
        }
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

    //(Sari, and Riyansah, 2021)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                destination = marker.getPosition();

                List<LatLng> path = new ArrayList();


                //Execute Directions API request
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey("AIzaSyCLsSnRSj3Wp4ODEHNh-frX__F__ZSpmYw")
                        .build();
                DirectionsApiRequest req = DirectionsApi.getDirections(context, MainActivity.lati + "," +MainActivity.longi, destination.latitude+","+destination.longitude);
                try {
                    DirectionsResult res = req.await();

                    //Loop through legs and steps to get encoded polylines of each step
                    if (res.routes != null && res.routes.length > 0) {
                        DirectionsRoute route = res.routes[0];

                        if (route.legs !=null) {
                            for(int i=0; i<route.legs.length; i++) {
                                DirectionsLeg leg = route.legs[i];
                                MapsActivity.duration = leg.duration.humanReadable;
                                MapsActivity.distance = leg.distance.humanReadable;

                                Toast.makeText(MapsActivity.this, "Duration: "+ duration +"\n" + "Distance: " +distance, Toast.LENGTH_LONG).show();

                                if (leg.steps != null) {
                                    for (int j=0; j<leg.steps.length;j++){
                                        DirectionsStep step = leg.steps[j];
                                        if (step.steps != null && step.steps.length >0) {
                                            for (int k=0; k<step.steps.length;k++){
                                                DirectionsStep step1 = step.steps[k];
                                                EncodedPolyline points1 = step1.polyline;
                                                if (points1 != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                    for (com.google.maps.model.LatLng coord1 : coords1) {
                                                        path.add(new LatLng(coord1.lat, coord1.lng));
                                                    }
                                                }
                                            }
                                        } else {
                                            EncodedPolyline points = step.polyline;
                                            if (points != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                for (com.google.maps.model.LatLng coord : coords) {
                                                    path.add(new LatLng(coord.lat, coord.lng));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                //Draw the polyline
                if (path.size() > 0) {
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    mMap.addPolyline(opts);
                }

                mMap.getUiSettings().setZoomControlsEnabled(true);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(MainActivity.lati, MainActivity.longi));
                LatLngBounds myB = builder.build();
                CameraUpdate zoomin = CameraUpdateFactory.newLatLngBounds(myB,20);
                mMap.animateCamera(zoomin);

                return false;
            }
        });

        mMap.addMarker(new MarkerOptions().position(new LatLng(MainActivity.lati, MainActivity.longi)));
        PlacesSearchResult[] placesSearchResults = run().results;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.lati, MainActivity.longi), 14));
        Log.d("Check running", ""+placesSearchResults.length);
        for (PlacesSearchResult place: placesSearchResults) {

            Log.d("locationF", "onMapReady: " + place.name);
            mMap.addMarker(new MarkerOptions().position(new LatLng(place.geometry.location.lat, place.geometry.location.lng)).title(place.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                com.example.opscgroupprojectapp.DirectionsJSONParser parser = new com.example.opscgroupprojectapp.DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //(Evan, 2020)
    //https://stackoverflow.com/questions/59922561/how-to-find-nearby-places-using-new-places-sdk-for-android
    public PlacesSearchResponse run(){
        Log.d("runM", "run: method is running");
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCLsSnRSj3Wp4ODEHNh-frX__F__ZSpmYw")
                .build();
        com.google.maps.model.LatLng location = new com.google.maps.model.LatLng(MainActivity.lati, MainActivity.longi);

        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .rankby(RankBy.PROMINENCE)
                    .language("en")
                    .type(SettingsPage.pLandmark)
                    .await();
            return request;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;

    }
}
