package com.example.journal3;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.TextView;


public class videoPlayNew extends AppCompatActivity{
    //    private StorageReference mStorageRef;

    private VideoView mainVideoView;
    private Uri videoUri;

    //add
    private static final int REQUEST_CODE  = 1000;
    TextView locatioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_video);

        MediaController mediaController =new MediaController(this);

        mainVideoView = (VideoView) findViewById(R.id.vid);
        locatioView = (TextView) findViewById(R.id.locationView);

        mainVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mainVideoView);
        Bundle data = getIntent().getExtras();
        String videourl = data.getString("videourl");
        String location = data.getString("location");
        System.out.println("location from calendar is "+location);

        locatioView.setText(location);
        videoUri = Uri.parse(videourl);
        mainVideoView.setVideoURI(videoUri);
//        mainVideoView.requestFocus();
        mainVideoView.start();


    }



}


