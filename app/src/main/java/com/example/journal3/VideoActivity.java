package com.example.journal3;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class VideoActivity extends AppCompatActivity {
    private int REQUEST_TAKE_GALLERY_VIDEO1=100;
    private int REQUEST_TAKE_GALLERY_VIDEO2=101;
    private int PERMISSION_CODE=99;
    Dialog demo_dialog;
    CircularProgressButton mash;
    private Uri videoUri;
    private Uri videoUri2;
    private String videoUriPath;
    private String videoUri2Path;
    private TextView mDisplayDate1;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetLinstenner;
    private DatePickerDialog.OnDateSetListener mDateSetLinstenner2;
    private String start_date;
    private String end_date;
    private java.util.Timer timer;

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


        if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.demo_window_horizontal);
            Log.i("info", "landscape");
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.demo_window);
            Log.i("info", "portrait");

        }

        if (savedInstanceState != null){
            videoUriPath = savedInstanceState.getString("videoUriPath");
            videoUri2Path = savedInstanceState.getString("videoUri2Path");
        }

        demo_dialog = new Dialog(this);

        mash = (CircularProgressButton) findViewById(R.id.save_share);
        mash.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mash.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoActivity.this," Mash finished",Toast.LENGTH_SHORT).show();
                        mash.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                        Intent intent = new Intent(VideoActivity.this, SaveShare.class);
                        Log.v("TEST:",videoUriPath);
                        Log.v("TEST:",videoUri2Path);
                        intent.putExtra("videoUri",videoUriPath);
                        intent.putExtra("videoUri2",videoUri2Path);

                        startActivity(intent);
                    }
                },3000);

            }
        });
    }


    public void ShowPopup_order(View v) {
        ImageView txtclose;
        demo_dialog.setContentView(R.layout.pop_up);
        txtclose = (ImageView) demo_dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo_dialog.dismiss();
            }
        });
        demo_dialog.show();
    }

    public void ShowPopup_music(View v) {
        ImageView txtclose;
        demo_dialog.setContentView(R.layout.pop_up_music);
        txtclose = (ImageView) demo_dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo_dialog.dismiss();
            }
        });
        demo_dialog.show();
    }

    public void ShowPopup_calendar(View v) {
        ImageView txtclose;
        demo_dialog.setContentView(R.layout.pop_up_calendar);
        txtclose = (ImageView) demo_dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo_dialog.dismiss();
            }
        });
        
        demo_dialog.show();

        mDisplayDate1 = (TextView) demo_dialog.findViewById(R.id.start_date);
        mDisplayDate2 = (TextView) demo_dialog.findViewById(R.id.end_date);

        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(VideoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetLinstenner, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(VideoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetLinstenner2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetLinstenner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date_start = month + "/" + day + "/" + year;
                start_date = date_start;
                String ResourceIdAsString = view.getResources().getResourceName(view.getId());
                Log.v("testId", String.valueOf(ResourceIdAsString));
                mDisplayDate1.setText(date_start);
            }
        };

        mDateSetLinstenner2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date_end = month + "/" + day + "/" + year;
                end_date = date_end;
                String ResourceIdAsString = view.getResources().getResourceName(view.getId());
                Log.v("testId", String.valueOf(ResourceIdAsString));
                mDisplayDate2.setText(date_end);
            }
        };
    }

    public void save_share(View v)
    {
        ImageView txtclose;
        demo_dialog.setContentView(R.layout.pop_up_calendar);
        txtclose=(ImageView)demo_dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo_dialog.dismiss();
            }
        });
        demo_dialog.show();
    }

    public void upload_video(View v) {
        ImageView txtclose;
        demo_dialog.setContentView(R.layout.pop_upload_video);
        txtclose = (ImageView) demo_dialog.findViewById(R.id.txtclose);
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

    public void select_last_month(View v) {

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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("videoUriPath", videoUriPath);
        outState.putString("videoUri2Path", videoUri2Path);
    }


}

