package com.sensei374121.amey.memoryappfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Activity_NewMemory extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    Firebase ref;
    private static final int PLACE_PICKER_REQUEST = 10;
    private static final int SELECT_PHOTO = 20;
    private String spinner_value, mem_place_street_addr,mem_lat,mem_long, mem_place_name,UID;
    private View rootview;
    private GoogleApiClient googleApiClient;
    private TextView display,display_image_count;
    private EditText description,memory_name;
    private TransferUtility transferUtility;
    private String PERMISSIONS[]={WRITE_EXTERNAL_STORAGE};
    private List<String> uploadFilePaths;
    private List<String> awsPicPaths;
    private Firebase.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory);

        //GETTING THE ROOTVIEW FOR SNACKBAR MESSAGES
        rootview = findViewById(R.id.newmemory);

        //setup the spinner
        setupSpinner();

        //setting up the placepicker
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this,this)
                .build();

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:c73692c8-4f28-4cdc-b96a-b60c04b9c72c", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Initializing the Amazon S3 credentials and tranferUtility class
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, getApplicationContext());

        //Setting up firebase reference and listeners
        ref = new Firebase(FIREBASE_URL);

        //setting up the UID from the homepage
        UID = Activity_Login.UID;
        //Toast.makeText(Activity_NewMemory.this, "UID is:"+UID, Toast.LENGTH_SHORT).show();

        //setting reference to textview
        display = (TextView) findViewById(R.id.display_addr);
        display_image_count = (TextView) findViewById(R.id.display_pictures_count);

        //setting refeence to edittext
        description = (EditText) findViewById(R.id.description);
        memory_name = (EditText) findViewById(R.id.memory_name);

        //initilizing our lists
        uploadFilePaths = new ArrayList<>();
        awsPicPaths = new ArrayList<>();

    }

    private void setupSpinner(){
        //CONFIGURING THE SPINNER
        Spinner spinner = (Spinner) findViewById(R.id.share);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.share_choice, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //start the listener on the spinner
        spinner.setOnItemSelectedListener(this);
    }

    //this function is used when any item from the spinner is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //to select the item selected by the spinner
        String spinnertmp_value=parent.getItemAtPosition(position).toString();
        if (spinnertmp_value.equalsIgnoreCase("Friends")){
            spinner_value = spinnertmp_value + "-Public";
        }else{
            spinner_value = spinnertmp_value;
        }
    }

    //this function is used when nothing from the spinner is selected. For default, settings.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinner_value = "public";
    }

    //this is used if Google failed to initialize the GOogleAPIClient
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GOOGLEAPICLIENT","Google API Client failed to start");
        Snackbar.make(rootview,"Google API Client failed to start",Snackbar.LENGTH_SHORT).show();
    }

    //this is the OnClickListener for the loc picker Button
    public void startLocPicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //this function is the onClickListener for upload Picture Button
    public void uploadPic(View view) {
        //this is the permissions module for android marshmallow.
        //WE are asking permissions so that the upload works
        if(ContextCompat.checkSelfPermission(Activity_NewMemory.this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(Activity_NewMemory.this, WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Need Permissions")
                        .setMessage("We need permissions to write the photos to your device. Any damage caused to your" +
                                "device due to our writing process. We claim responsibility !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Activity_NewMemory.this, new String[] { WRITE_EXTERNAL_STORAGE }, 0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(Activity_NewMemory.this, PERMISSIONS, 0);
            }
        }

        //We execute only when permissions are granted.
        if (ContextCompat.checkSelfPermission(Activity_NewMemory.this,WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //these lines of code open up the gallery and let's us choose an image
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }else{
            Snackbar.make(rootview,"You need to grant permissions for this functionality to work",Snackbar.LENGTH_SHORT).show();
        }

    }

    //this executed when above initial permission check is needed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != 0) {
            return;
        }
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            //you can add code if needed
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK){
                    final Uri imageUri = data.getData();
                    String path = getRealPathFromURI(imageUri);
                    uploadFilePaths.add(path);
                    String disp_msg = "You uploaded "+uploadFilePaths.size()+" pictures";
                    display_image_count.setText(disp_msg);
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK){
                    Place place = PlacePicker.getPlace(getApplicationContext(), data);
                    mem_place_name = (String) place.getName();
                    mem_place_street_addr = (String) place.getAddress();
                    mem_lat = String.valueOf(place.getLatLng().latitude);   //remember this value is double
                    mem_long = String.valueOf(place.getLatLng().longitude); //remember this value is double
                    String disp_msg = String.format("Place: %s\nAddr: %s\nLat:%s\nLong:%s\n", place.getName(),place.getAddress(),place.getLatLng().latitude,place.getLatLng().longitude);
                    display.setText(disp_msg);
                    display.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Snackbar.make(rootview,"Something is wrong in onActivvityReturn function",Snackbar.LENGTH_SHORT).show();
        }

    }

    //this piece of code is required to get the exact path from the uri.
    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void uploadMemory(View view) {
        File file;
        TransferObserver transferObserver;

        if (UID != null){
            String bucketName = "memory-saves/"+UID;
            String path = "https://s3.amazonaws.com/"+bucketName;
            String finalpath;

            //this piece of code uploads the pictures to Amazon AWS
            for (int i = 0; i < uploadFilePaths.size(); i++) {
                file = new File(uploadFilePaths.get(i));
                transferObserver = transferUtility.upload(bucketName, file.getName(), file);
                //set up the code here to listen to this observer
                finalpath=path+"/"+file.getName();
                awsPicPaths.add(finalpath);
                //you can download the file using the url given above.
                //the url formed will be https://s3.amazonaws.com/+bucket_name+filename
            }

            /*this piece of code will send the code to firebase*/
            //this is to set all variables befor uploading to firebase
            if (description!=null && memory_name != null){
                String description_str = String.valueOf(description.getText());
                if (description_str.length()==0){
                    Toast.makeText(Activity_NewMemory.this, "Please enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
                String memory_name_str = String.valueOf(memory_name.getText());
                if (memory_name_str.length()==0){
                    Toast.makeText(Activity_NewMemory.this, "Please enter memory name", Toast.LENGTH_SHORT).show();
                    return;
                }
                POJO_MemoryDetail memoryDetail = new POJO_MemoryDetail(memory_name_str, mem_place_name, mem_place_street_addr, mem_lat, mem_long,description_str,spinner_value,awsPicPaths);
                HashMap sendMap = memoryDetail.createHash();
                Firebase memoryref = ref.child("USERS").child(UID).child("MEMORIES");
                memoryref.push().updateChildren(sendMap);

                Snackbar.make(rootview,"your memory was uploaded",Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_NewMemory.this,Activity_Homepage.class));
            }

        }else{
            Log.d("ACTIVITY_NEWMEMORY", "UID not assigned");
            return;
        }
    }

}
