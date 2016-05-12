package com.sensei374121.amey.memoryappfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Activity_UpdateProfile extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 21;
    private String UID_str;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    private Firebase ref;
    private EditText nameButton,aboutme;
    private ImageButton profileimage,bgImage;
    private Button uploadprofile;
    private Boolean femaleGenderChecked=true;
    private View rootview;
    private String mCurrentPhotoPath,user_mail;
    private String PERMISSIONS[]={WRITE_EXTERNAL_STORAGE};
    private File profilePicFile,bgFile;
    private TransferUtility transferUtility;
    private String profilePicfinalpath,bgpicfinalPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        rootview = findViewById(R.id.act_profile_edit);

        //Setting up firebase reference and listeners
        ref = new Firebase(FIREBASE_URL);

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

        //set up UID of user
        UID_str =Activity_Login.UID;
        //Toast.makeText(Activity_UpdateProfile.this, "UID_str is:"+ UID_str, Toast.LENGTH_SHORT).show();

        //first get all the IDS so that they can be worked upon since its not a layout
        nameButton = (EditText)findViewById(R.id.input_name);
        aboutme = (EditText) findViewById(R.id.aboutme);
        profileimage = (ImageButton) findViewById(R.id.profile_image_button);
        bgImage = (ImageButton) findViewById(R.id.profile_bg_button);
        uploadprofile = (Button) findViewById(R.id.UploadProfileButton);

        Firebase newref = ref.child("USERS-LIST").child(UID_str);
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                POJO_UserBIO post = snapshot.getValue(POJO_UserBIO.class);
                user_mail = post.getEmail();
                Log.d("email", user_mail);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Snackbar.make(rootview,"event cancelled",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadProfilePicOnClick(View view) {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Snackbar.make(rootview,"Error while creating file",Snackbar.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    //this function is used to store a image file on disk that was recently clicked
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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



    public void uploadBgOnClick(View view) {
        //this is the permissions module for android marshmallow.
        //WE are asking permissions so that the upload works
        if(ContextCompat.checkSelfPermission(Activity_UpdateProfile.this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(Activity_UpdateProfile.this, WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Need Permissions")
                        .setMessage("We need permissions to write the photos to your device. Any damage caused to your" +
                                "device due to our writing process. We claim responsibility !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Activity_UpdateProfile.this, new String[] { WRITE_EXTERNAL_STORAGE }, 0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(Activity_UpdateProfile.this, PERMISSIONS, 0);
            }
        }

        //We execute only when permissions are granted.
        if (ContextCompat.checkSelfPermission(Activity_UpdateProfile.this,WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //these lines of code open up the gallery and let's us choose an image
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }

    public void updateProfileButtonOnCLick(View view) {
        //get all the values to strings
        String name_str = String.valueOf(nameButton.getText());
        String aboutme_str = String.valueOf(aboutme.getText());
        String gender = femaleGenderChecked.toString();

        //upload the pics to amazon aws
        String bucketName = "memory-saves/"+UID_str;
        String path = "https://s3.amazonaws.com/"+bucketName;
        profilePicfinalpath=path+"/"+profilePicFile.getName();
        bgpicfinalPath=path+"/"+bgFile.getName();
        TransferObserver transferObserver = transferUtility.upload(bucketName, profilePicFile.getName(), profilePicFile);
        TransferObserver transferObserver1 = transferUtility.upload(bucketName, bgFile.getName(), bgFile);

        //upload the data to firebase's current user account
        POJO_UserBIO userBIO = new POJO_UserBIO(name_str,user_mail,UID_str,profilePicfinalpath,bgpicfinalPath,aboutme_str,gender);
        HashMap sendmap = userBIO.createHashBIOHashComplete();
        Firebase tmpref = ref.child("USERS").child(UID_str).child("BIO");
        tmpref.setValue(sendmap);

        //update the users account in the userlist section
        POJO_UserBIO userBIO1 = new POJO_UserBIO(name_str, user_mail, UID_str, profilePicfinalpath);
        HashMap sendMap = userBIO1.createHashBIOHashContact();
        Firebase tmpref1 = ref.child("USERS-LIST").child(UID_str);
        tmpref1.setValue(sendMap);


        Toast.makeText(Activity_UpdateProfile.this, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),Activity_Homepage.class));
    }

    public void onRadioButtonCLicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    femaleGenderChecked = false;
                break;
            case R.id.radio_female:
                if (checked)
                    femaleGenderChecked = true;
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data == null){
                        //if the above part of code failed then we directly fetch the image from storage and store it in the imageview
                        profilePicFile = new File(mCurrentPhotoPath);
                        if (profilePicFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(profilePicFile.getAbsolutePath());
                            profileimage.setImageBitmap(myBitmap);
                        }
                    }
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK){
                    final Uri imageUri = data.getData();
                    String path = getRealPathFromURI(imageUri);
                    bgFile = new File(path);
                    Bitmap mybitmap = BitmapFactory.decodeFile(bgFile.getAbsolutePath());
                    bgImage.setImageBitmap(mybitmap);

                }
                break;
            default:
                Snackbar.make(rootview,"Default updatep profile error",Snackbar.LENGTH_SHORT).show();

        }
    }

}
