package com.example.journal3;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


// location
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class videoPlayNew extends AppCompatActivity{
    //    private StorageReference mStorageRef;

    private VideoView mainVideoView;
    private Uri videoUri;

    // Location
    private static final int REQUEST_CODE  = 1000;
    TextView txt_location;
    Button btn_start,btn_stop;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_video);

        MediaController mediaController =new MediaController(this);

        mainVideoView = (VideoView) findViewById(R.id.vid);

        mainVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mainVideoView);
        Bundle data = getIntent().getExtras();
        String videourl = data.getString("videourl");

        videoUri = Uri.parse(videourl);
        mainVideoView.setVideoURI(videoUri);
//        mainVideoView.requestFocus();
         mainVideoView.start();

        // location
        txt_location = (TextView)findViewById(R.id.editLocation);

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
        else {
            // if permission is granted
            buildLocationRequest();
            buildLocationCallBack();

            // create Fused
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    // location
    public void buildLocationCallBack() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult){
                for (Location location:locationResult.getLocations())
                    txt_location.setText(String.valueOf(location.getLatitude())
                            +"/"
                            +String.valueOf(location.getLongitude()));

            }
        };
    }

    public void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }


}

