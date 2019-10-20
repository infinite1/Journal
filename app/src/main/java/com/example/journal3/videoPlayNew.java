package com.example.journal3;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;


public class videoPlayNew extends AppCompatActivity{
    //    private StorageReference mStorageRef;

    private VideoView mainVideoView;
    private Uri videoUri;

    //add
    private static final int REQUEST_CODE  = 1000;
    TextView txt_location;

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


    }



}


