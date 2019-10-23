package com.example.journal3;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class homeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>
        , MediaStoreAdapter.OnClickThumbListener{
    private Toolbar myToolbar;
    private Button mSignIn;
    private BottomNavigationView mBtmView;

    //add
    private Uri videouri;
    private static final int REQUEST_CODE = 101;
    private ProgressDialog progressDialog;
    private StorageReference videoref;
    private DatabaseReference mDatabaseRef;
    private StorageReference storageRef;
    private String strDate;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private long timeStamp;
    private static final int PERMISSION_CODE = 22;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView emailView;
    private FirebaseFirestore db;
    private Uri downloadUri;
    //location
    private LocationManager lm;
    LocationListener locationListener;
    String currentlocation = "carlton";
    private double new_longitude;
    private double new_latitude;
    private final static int MEDIASTORE_LOADER_ID = 0;
    private RecyclerView mThumbnailRecyclerView;
    private MediaStoreAdapter mMediaStoreAdapter;


    public static final int MAX_SIZE = 100;
    private static final String TAG = "Upload Video";

    private List<String> recordList = new ArrayList<String>(MAX_SIZE);

    //aaaaaaaaaaaaaaaaaaaadd
    RecyclerView recyclerView;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        //aaaaaaaaaaaaaaaaaaaaaadd



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
        mAuth = FirebaseAuth.getInstance();
        myToolbar = findViewById(R.id.my_toolbar);
        mBtmView = findViewById(R.id.bot_nav);
        mBtmView.setOnNavigationItemSelectedListener(this);

        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);


        // signIn button if user not exist, show email address if user exists
        mSignIn = headerview.findViewById(R.id.nav_sign_in_btn);
        emailView = headerview.findViewById(R.id.emailView);

        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            mSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(homeActivity.this, LogInActivity.class);
                    startActivity(i);
                }
            });
        } else {
            mSignIn.setVisibility(View.GONE);
            emailView.setVisibility(View.VISIBLE);
            String email = currentUser.getEmail();
            emailView.setText(email);
        }

        // Add select item listener for navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.signOut: {
                                System.out.println("select signout");
                                mSignIn.setVisibility(View.VISIBLE);
                                emailView.setVisibility(View.GONE);
                                mAuth.signOut();
                                System.out.println("Sign out complete");
                                Intent i = new Intent(homeActivity.this,
                                        homeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                homeActivity.this.finish();
                            }
                            break;
                        }
                        return true;
                    }
                });


        //add


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        storageRef = FirebaseStorage.getInstance().getReference();


//        videoref =mStorageRef.child("/videos" + "/userIntro.3gp");


        getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
        mThumbnailRecyclerView = (RecyclerView) findViewById(R.id.thumbnailRecyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        mThumbnailRecyclerView.setLayoutManager(gridLayoutManager);
        mThumbnailRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mMediaStoreAdapter = new MediaStoreAdapter(this);
        mThumbnailRecyclerView.setAdapter((mMediaStoreAdapter));

    }

    // Add select item listener for bottomNavigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mesh: {
                System.out.println("select mesh");
                Intent intent = new Intent(homeActivity.this, VideoActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.calendar: {
                System.out.println("select calendar");
                Intent intent = new Intent(homeActivity.this, CalendarActivity.class);
                intent.putExtra("recordlist", (Serializable) recordList);
                startActivity(intent);

            }
            break;
        }
        return true;
    }


    //add

    public void upload(View view) {

        // Can only upload when the user sign in
        if (currentUser != null) {
            long timeStamp = System.currentTimeMillis();
            System.out.println("Time is : " + timeStamp);

            // initialize Firestore
            db = FirebaseFirestore.getInstance();
            simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy");
            date = new Date(timeStamp);
            strDate = simpleDateFormat.format(date);
            System.out.println("Date is : " + strDate);
            videoref = storageRef.child("/videos" + "/" + strDate);
            ;


            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploading file..");
            progressDialog.setProgress(0);
            progressDialog.show();
            if (videouri != null) {
                UploadTask uploadTask = videoref.putFile(videouri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(homeActivity.this,
                                "Upload failed: " + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(homeActivity.this, "Upload complete",
                                        Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                recordList.add(strDate);
                                /******************************get location*************************/
                                checkGPSSettings();

                            }
                        }).addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            updateProgress(taskSnapshot);

                                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setProgress(currentProgress);
                            }
                        }).addOnCompleteListener(
                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                videoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUri = uri;
                                        uploadRefToDatabase(currentUser, strDate);
//                                        /******************************get location*************************/
                                        try {
                                            currentlocation = getLocation(new_latitude,new_longitude);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        /******************************add metadata*************************/
                                        StorageMetadata metadata = new StorageMetadata.Builder()
                                                .setContentType("video/mp4")
                                                .setCustomMetadata("Location", currentlocation)
                                                .build();
                                        // Update metadata properties
                                        videoref.updateMetadata(metadata)
                                                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                    @Override
                                                    public void onSuccess(StorageMetadata storageMetadata) {
                                                        System.out.println("Add location successfully!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Uh-oh, an error occurred!
                                                        System.out.println("Add location Failed: " + exception.toString());

                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        System.out.println("Fail to upload uri");
                                    }
                                });
                            }
                        }
                );


            } else {
                Toast.makeText(homeActivity.this, "Nothing to upload",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(homeActivity.this, "User doesn't sign in",
                    Toast.LENGTH_LONG).show();
        }


    }

    // upload video reference to firebase
    public void uploadRefToDatabase(FirebaseUser currentUser,
                                    String strDate) {

        System.out.println("upload " + downloadUri.toString());
        // upload videos to cloud
        Map<String, Object> user = new HashMap<>();
        user.put(strDate, downloadUri.toString());

        // Add a new document with user email as id
        db.collection("users").document(Objects.requireNonNull(currentUser.getEmail()))
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);


        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        videouri = data.getData();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        videouri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else if (requestCode == 2) {
                checkGPSSettings();
                return;
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkGPSSettings() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                //location.getProvider();
                Log.d("haha", "" + location.getProvider() + " Location latitude " + latitude + "\nlongitude:" + longitude);

                new_latitude = latitude;
                new_longitude = longitude;
                System.out.println("new latitude is " + new_latitude);
                System.out.println("new longitude is " + new_longitude);
                lm.removeUpdates(locationListener);
                lm = null;

            }
        };






        // Location hardware setting enabled?
        boolean GPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        String[] permissionsArray = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (GPSEnabled) {
            // Android 6.0+
            if (Build.VERSION.SDK_INT >= 23) {
                if (!checkPermissions(this, permissionsArray)) {
                    // request code 1

                    Log.d("haha", "request");

                    ActivityCompat.requestPermissions(this, permissionsArray,
                            1);
                } else {
                    // Permission has already been granted
                    Log.d("haha", "line 52");
                    startLocalisation();
                }


            } else {
                // no runtime check
                Log.d("haha", "line 74");
                startLocalisation();
            }
        } else {
            Log.d("haha", "line 82");
            Toast.makeText(this, "GPS Not Enabled", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            // request code 2
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void startLocalisation() {

        // parameters of location service
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // localisation uses a lot of power, consider your task cycle

        //String provider = lm.getBestProvider(criteria, true);

        // can also use a specific provider

        // cellular or WIFI network can localise me
        String providerNET = LocationManager.NETWORK_PROVIDER;

        // gps signal often naive
        String providerGPS = LocationManager.GPS_PROVIDER;


        // must call this before using getLastKnownLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }



        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled) {
            Log.d("haha", " gps_enabled");

            //requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener)

            lm.requestLocationUpdates(providerGPS, 0, 0, locationListener);
            gps_loc = lm.getLastKnownLocation(providerGPS);
        }
        if (network_enabled){
            Log.d("haha", " net_enabled");
            lm.requestLocationUpdates(providerNET, 0, 0, locationListener);
            net_loc = lm.getLastKnownLocation(providerNET);
        }

        if (gps_loc != null && net_loc != null) {

            Log.d("haha", "both available location");

            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;

            // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

        } else {

            if (gps_loc != null) {
                finalLoc = gps_loc;
                Log.d("haha", "gps available location");
            } else if (net_loc != null) {
                finalLoc = net_loc;
                Log.d("haha", "net available location");
            }
        }
//        if (gps_loc != null) {
//                finalLoc = gps_loc;}

        if (finalLoc != null) {

            double latitude = finalLoc.getLatitude();

            double longitude = finalLoc.getLongitude();

            Log.d("haha", "latitude：" + latitude + "\nlongitude" + longitude);

            // if we are in melbourne, we get negative latitude.
            // it means south part of the earth.

        } else {
            Log.d("haha", "no available location");


            //startLocalisation();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // request code 1
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("haha", "line 90");
                    startLocalisation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            default:
                break;

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public String getLocation(double latitude,double longtitude) throws IOException {
        String city = "";
        String subadmin="";
        String admin = "";
        Geocoder geocoder = new Geocoder(homeActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                subadmin = addresses.get(0).getSubAdminArea();
                admin = addresses.get(0).getAdminArea();

                Log.d("address", "Complets Address: " + addresses.toString());
                Log.d("address", "Address: " + address);

            } catch (Exception e) {
                e.printStackTrace();
            }


        return (city+","+subadmin+","+admin);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return new CursorLoader(
                this,
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mMediaStoreAdapter.changeCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMediaStoreAdapter.changeCursor(null);
    }

    @Override
    public void onClickImage(Uri imageUri) {
        Toast.makeText(homeActivity.this, "ImageUri = " + imageUri.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickVideo(Uri videoUri) {
        Intent videoPlayIntent= new Intent(this, VideoPlayActivity.class);
        videoPlayIntent.setData(videoUri);
        startActivity(videoPlayIntent);
    }
}


