package pg.eti.ksg.ProjektInzynierski.ui.directions;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.Permissions;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.ui.friends.FriendsViewModel;

public class MapsActivity  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Button directionsBtn;
    private MapsViewModel mViewModel;
    private Long routeId;
    private FusedLocationProviderClient location;
    private LocationCallback locationCallback;
    private List<Points> allPoints;
    private boolean isFriendRoute;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Google map setup

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Show marker on the screen and adjust the zoom level
        if(isFriendRoute) {
            if (mDestination != null) {
                mMap.addMarker(new MarkerOptions().position(mDestination).title("Lokalizacja znajomego"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestination, 8f));
            }
        }
        else {
            if (mDestination != null && mOrigin !=null) {
                mMap.addMarker(new MarkerOptions().position(mOrigin).title("Lokalizacja początkowa"));
                mMap.addMarker(new MarkerOptions().position(mDestination).title("Lokalizacja końcowa"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestination, 8f));
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();

        if(bundle !=null) {
            isFriendRoute = bundle.getBoolean("route");
            routeId = bundle.getLong("id");
        }

        directionsBtn = findViewById(R.id.directionBtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Permissions.LocationPermission(this))
                    requestPermissions(Permissions.LOCATION_PERMISSIONS,Permissions.LOCATION_REQUEST_CODE);
        }
        if(isFriendRoute)
            setLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        mViewModel.getPoints(routeId).observe(this, new Observer<List<Points>>() {
            @Override
            public void onChanged(List<Points> points) {
                if(isFriendRoute) {
                    if (points.size() > 0) {
                        Points point = points.get(points.size() - 1);
                        mDestination = new LatLng(point.getLat(), point.getLng());
                        mMap.addMarker(new MarkerOptions().position(mDestination).title("Lokalizacja znajomego"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestination, 8f));
                    }
                }
                else
                {
                    allPoints = points;
                    mOrigin = new LatLng(points.get(0).getLat(), points.get(0).getLng());
                    mDestination = new LatLng(points.get(points.size()-1).getLat(), points.get(points.size()-1).getLng());
                    mMap.addMarker(new MarkerOptions().position(mOrigin).title("Lokalizacja początkowa"));
                    mMap.addMarker(new MarkerOptions().position(mDestination).title("Lokalizacja końcowa"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestination, 8f));
                }
            }
        });

        directionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriendRoute) {
                    if (mDestination != null && mOrigin != null) {
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", mOrigin.latitude, mOrigin.longitude, mDestination.latitude, mDestination.longitude);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                }
                else
                {
                    if (mDestination != null && mOrigin != null) {
                        String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/dir/?api=1&origin=%f,%f&destination=%f,%f&waypoints=", mOrigin.latitude, mOrigin.longitude, mDestination.latitude, mDestination.longitude);
                        for(int i=1;i<allPoints.size()-1;i++){
                            if(i!=1)
                                uri+="|";
                            uri += allPoints.get(i).getLat()+","+allPoints.get(i).getLng();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void setCallback()
    {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            }

        };
    }

    @SuppressLint("MissingPermission")
    private void setLocation()
    {
        location = LocationServices.getFusedLocationProviderClient(this);
        setCallback();
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

}