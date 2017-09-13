package com.kirja.xxx.selfiepuzzle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Intent takePictureIntent;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = (ImageView) findViewById(R.id.iw);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        Button button = new Button(this);
        rl.addView(button);
        button.setOnClickListener(this);
        button.setTextSize(30);
        button.setText("Take a picture");
        if (checkCameraHardware(getApplicationContext())) button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Log.i("stage", "klikkaus");
        permissionCheck();
        //dispatchTakePictureIntent();
        //Intent intent = new Intent(this, PuzzleView.class);
        //startActivity(intent);
    }

    private void dispatchTakePictureIntent() {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Log.i("stage", "dispatchTakePictureIntent");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.xxx.android.fileprovider",
                        photoFile);
                Log.i("stage", photoFile.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("stage", Integer.toString(resultCode));
        /*
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            //result = data.toURI();
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PuzzleView.class);
            intent.putExtra("FILEPATH", mCurrentPhotoPath);
            startActivity(intent);
        }
        */
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, PuzzleView.class);
            intent.putExtra("FILEPATH", mCurrentPhotoPath);
            startActivity(intent);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
/*
        File image = File.createTempFile(
                imageFileName,  /* prefix */
  //              ".jpg",         /* suffix */
        //           storageDir      /* directory */
        //);

        File image = File.createTempFile(
                "image",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("stage i", mCurrentPhotoPath);
        return image;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            int cameraId = -1;
            // Search for the front facing camera
            int numberOfCameras = Camera.getNumberOfCameras();
            Log.i("kamera", Integer.toString(numberOfCameras));
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId < 0) {
                Log.i("kamera", Integer.toString(cameraId));
                Toast.makeText(getApplicationContext(),
                        "Ohjelmaa ei voi käyttää, koska laitteessa ei ole selfie-kameraa", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } else {
            Toast.makeText(getApplicationContext(),
                    "Ohjelmaa ei voi käyttää, koska laitteessa ei ole kameraa", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void permissionCheck() {
        //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //showExplanation("Permission Needed", "Use device's camera", Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_IMAGE_CAPTURE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_IMAGE_CAPTURE);
            }
        }
        else dispatchTakePictureIntent();
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults){
        Log.i("stage", "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("stage", "permission granted success");
                    dispatchTakePictureIntent();
                } else {
                    //TODO: TOAST "you cannot use this functionality"
                    Log.i("stage", "permission denied");
                }
                return;
            }
        }
    }

}