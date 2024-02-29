package com.example.virtualbussinessmobileapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.virtualbussinessmobileapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.widget.Toast;

/** @noinspection ALL*/
public class MainMapActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.main_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MapView map = (MapView) findViewById(R.id.MainMapView);
        IMapController mapController = map.getController();

        getLocPerms();
        getCurLoc(fusedLocationClient);

        mapInit(map);
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
                            // Your further processing here
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
}