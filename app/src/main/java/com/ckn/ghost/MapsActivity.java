package com.ckn.ghost;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Listener {

    private GoogleMap mMap;

    EasyWayLocation easyWayLocation;

    private LatLng officeLatLng = new LatLng(-6.3198178, 106.7476626);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        easyWayLocation = new EasyWayLocation(this, false, this);
        initUI();
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions officeMarker = new MarkerOptions().position(officeLatLng).title("Office Position");
        mMap.addMarker(officeMarker);
        mMap.addCircle(new CircleOptions()
                .center(officeLatLng)
                .radius(300.0)
                .strokeColor(Color.LTGRAY)
                .fillColor(getResources().getColor(R.color.colorAccentTransparent))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(officeLatLng));
        mMap.setMaxZoomPreference(16f);
        mMap.setMinZoomPreference(16f);

        for (LatLng latLng : getFriendsLatLng()) {
            showFriendMarker(latLng);
        }
    }

    @Override
    public void locationOn() {
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(final Location location) {
        showMyMarker(location);
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            easyWayLocation.onActivityResult(resultCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();

    }

    private void showMyMarker(final Location location) {
        if (mMap != null) {
            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions myMarker = new MarkerOptions().position(myLatLng).title("Your Position");
            myMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(myMarker);
        }
    }

    private void showFriendMarker(final LatLng latLng) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            MarkerOptions myMarker = new MarkerOptions().position(latLng);
            myMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addMarker(myMarker);
        }
    }

    private ArrayList<LatLng> getFriendsLatLng() {
        ArrayList<LatLng> friends = new ArrayList<>();
        friends.add(officeLatLng);
        friends.add(new LatLng(-6.320878, 106.74766));
        friends.add(new LatLng(-6.3199782, 106.747661));
        friends.add(officeLatLng);
        return friends;
    }
}