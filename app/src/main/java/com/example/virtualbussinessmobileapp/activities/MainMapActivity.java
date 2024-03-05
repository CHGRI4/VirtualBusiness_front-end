package com.example.virtualbussinessmobileapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.virtualbussinessmobileapp.R;
import com.example.virtualbussinessmobileapp.networking.config_files.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.opencsv.CSVReader;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.w3c.dom.Text;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/** @noinspection ALL*/
public class MainMapActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        setContentView(R.layout.main_page);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx)); //changed line ??!!

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView progressBar_msg = findViewById(R.id.progressBar_message);
        Button settings_but = findViewById(R.id.settingsButton);

        progressBar_msg.setText("Loading map...");
        progressBar.setVisibility(0);
        progressBar_msg.setVisibility(0);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressBar.setProgress(30);
        MapView map = (MapView) findViewById(R.id.MainMapView);
        IMapController mapController = map.getController();
        progressBar.setProgress(50);
        getLocPerms();
        progressBar.setProgress(60);
        getCurLoc(fusedLocationClient);
        progressBar.setProgress(70);
        mapInit(map);
        progressBar.setProgress(80);

        progressBar_msg.setVisibility(4);
        progressBar.setVisibility(4);

        addMarker(map, new GeoPoint(38.089355, 23.785459));
        settings_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMapActivity.this,SettingsActivity.class));
            }
        });
        requestMonumentList();
    }


    @Override
    protected void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }


    private void mapInit(MapView map){
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMapOrientation(0);
        Overlay locOverlay = new MyLocationNewOverlay(map); locOverlay.setEnabled(true);
        Overlay scaleBarOverlay = new ScaleBarOverlay(map); scaleBarOverlay.setEnabled(true);
        scaleBarOverlay.setEnabled(true);

        map.getOverlays().add(scaleBarOverlay);
        map.getOverlays().add(locOverlay);
    }
    private void getCurLoc(FusedLocationProviderClient fusedLocationClient2) {
        fusedLocationClient2.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        new CancellationToken() {
                            @NonNull
                            @Override
                            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                return null;
                            }

                            @Override
                            public boolean isCancellationRequested() {
                                return false;
                            }
                        })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Toast.makeText(getApplicationContext(), "Cannot get location.", Toast.LENGTH_SHORT).show();
                        } else {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            updateMap(new GeoPoint(lat, lon));
                        }
                    }
                });
    }

    private void updateMap(GeoPoint startPoint) {
        MapView map = findViewById(R.id.MainMapView);
        IMapController mapController = map.getController();
        mapController.setZoom(14.0);
        mapController.setCenter(startPoint);
    }

    public void getLocPerms() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }
    public void addMarker(MapView mapView, GeoPoint geoPoint){
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);
    }
    private void requestMonumentList(){
        System.out.println("ENTERED FUN");
        Call<CSVReader> Monuments = ApiClient.getUserService().getExcelFile();
        Monuments.enqueue(new Callback<CSVReader>() {
            @Override
            public void onResponse(Call<CSVReader> call, Response<CSVReader> response) {
                if(response.isSuccessful()){
                    assert(Monuments!=null);
                    try{
                        CSVReader reader = (CSVReader) Monuments;
                        String [] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            // nextLine[] is an array of values from the line
                            System.out.println(nextLine[0] + nextLine[1] + "etc...");
                            Log.d("VariableTag", nextLine[0]);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "The specified file was not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CSVReader> call, Throwable t) {

            }
        });
    }
    private void loadMonuments(MapView mapView, MapController mapController){

    }
}