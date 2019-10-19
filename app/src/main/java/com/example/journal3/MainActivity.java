package com.example.journal3;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_TAKE_GALLERY_VIDEO1=100;
    private int REQUEST_TAKE_GALLERY_VIDEO2=101;
    private int PERMISSION_CODE=99;
    Dialog demo_dialog;
    Button mash;
    private Uri videoUri = null;
    private Uri videoUri2 = null;
    private String videoUriPath;
    private String videoUri2Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), PERMISSION_CODE);
        } else {
            Toast.makeText(this, "ALL Permissions granted", Toast.LENGTH_LONG).show();
        }



        setContentView(R.layout.demo_window);
        demo_dialog = new Dialog(this);

        mash = findViewById(R.id.save_share);
        mash.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaveShare.class);
                Log.v("TEST:",videoUriPath);
                Log.v("TEST:",videoUri2Path);
                intent.putExtra("videoUri",videoUriPath);
                intent.putExtra("videoUri2",videoUri2Path);

                startActivity(intent);
                }
            });
    }


        public void ShowPopup_order(View v) {
            TextView txtclose;
            demo_dialog.setContentView(R.layout.pop_up);
            txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo_dialog.dismiss();
                }
            });
            demo_dialog.show();
        }

        public void ShowPopup_music(View v) {
            TextView txtclose;
            demo_dialog.setContentView(R.layout.pop_up_music);
            txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo_dialog.dismiss();
                }
            });
            demo_dialog.show();
        }

        public void ShowPopup_calendar(View v) {
            TextView txtclose;
            demo_dialog.setContentView(R.layout.pop_up_calendar);
            txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo_dialog.dismiss();
                }
            });
            demo_dialog.show();
        }

        public void save_share(View v)
        {
            TextView txtclose;
            demo_dialog.setContentView(R.layout.pop_up_calendar);
            txtclose=(TextView)demo_dialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo_dialog.dismiss();
                }
            });
            demo_dialog.show();
        }

        public void upload_video(View v) {
            TextView txtclose;
            demo_dialog.setContentView(R.layout.pop_upload_video);
            txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo_dialog.dismiss();
                }
            });
            demo_dialog.show();
        }

        public void video1(View v) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            if(intent.resolveActivity(getPackageManager()) != null )
            {
                startActivityForResult(intent,REQUEST_TAKE_GALLERY_VIDEO1);
            }
        }

        public void video2(View v) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            if(intent.resolveActivity(getPackageManager()) != null )
            {
                startActivityForResult(intent,REQUEST_TAKE_GALLERY_VIDEO2);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO1 && resultCode == RESULT_OK) {
                videoUri = data.getData();
                videoUriPath = convertMediaUriToPath(videoUri);

            }
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO2 && resultCode == RESULT_OK) {
                videoUri2 = data.getData();
                videoUri2Path = convertMediaUriToPath(videoUri2);


            }
    }

    public String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }


}
