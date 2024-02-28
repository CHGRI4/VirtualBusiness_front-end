package com.example.virtualbussinessmobileapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.virtualbussinessmobileapp.R;
import com.example.virtualbussinessmobileapp.global_data.LocationManaging;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.location.Location;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;

/** @noinspection ALL*/
public class MainMapActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.main_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationManaging.getLocPerms(this);
        Location curLoc = getCurLoc(fusedLocationClient);

        MapView map = (MapView) findViewById(R.id.MainMapView);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(8);
        GeoPoint startPoint = new GeoPoint(/*curLoc.getLatitude()*/23, /*curLoc.getLongitude()*/23);
        mapController.setCenter(startPoint);
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
    private Location getCurLoc(FusedLocationProviderClient fusedLocationClient2) {
        fusedLocationClient2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
    }
    public void getLocPerms() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }
}