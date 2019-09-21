package com.w.sendbylocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.location.Location;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MessageActive extends AppCompatActivity {

    private AudioManager aud ;
    private Button button ;
    private Context mContext ;
    private Resources mResources ;
    private Ringtone ringtone ;
    private Uri uri ;
    private static final int CAMERA_REQUEST = 50;
    private static final String TAG = MessageActive.class.getSimpleName();
    private String myString = "101010101010101010100000010101010101010101010111110101011010101010101010101000000101010101010101010101111101010110101010101010101010000001010101010101010101011111010101" ;
    private String lati = "0", longi = "0" ;
    private static DecimalFormat df2 = new DecimalFormat("#.###");
    public MessageActive() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_act2);
        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        mContext = getApplicationContext();
        mResources = getResources();
        aud = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(mContext,uri) ;
        requestLocationUpdates();
        ActivityCompat.requestPermissions(MessageActive.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }
    //Initiate the request to track the device's location//
    private void requestLocationUpdates() {

        LocationRequest request = new LocationRequest();
        //Specify how often your app should request the device’s location//
        request.setInterval(20000);
        ringtone.stop();
        //Get the most accurate location data available//
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        //final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //If the app currently has access to the location permission...//
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //...then request location updates//
            client.requestLocationUpdates(request, new LocationCallback() {
                @SuppressLint("NewApi")
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    String latitude = "", longitude = "" ;
                    Location location = locationResult.getLastLocation();
                    if(location != null){
                        Toast.makeText(MessageActive.this, lati+" "+longi, Toast.LENGTH_SHORT).show();
                        latitude = df2.format(location.getLatitude());
                        longitude = df2.format(location.getLongitude());
                        if( latitude.equals(lati) &&   longitude.equals(longi)){
                            String phNo = "9728072620" ;
                            String msg = "https://www.google.com/maps/search/?api=1&query="+location.getLatitude()+","+location.getLongitude() ;
                            SmsManager smsManager = SmsManager.getDefault();
                            for(int i = 0 ; i < 15 ; i++)
                                aud.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.STREAM_RING);
                            smsManager.sendTextMessage(phNo, null, msg, null, null);
                            Toast.makeText(MessageActive.this, "Message Sent"+longi+lati, Toast.LENGTH_SHORT).show();
                            long blinkDelay = 50; //Delay in ms
                            //ringtone.setLooping(true);
                            ringtone.play();
                            for (int i = 0; i < myString.length(); i++) {
                                if (myString.charAt(i) == '0') {
                                    flashLightOn();
                                } else {
                                    flashLightOff();
                                }
                                try {
                                    Thread.sleep(blinkDelay);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            ringtone.stop();
                            Log.wtf("ab","test GIthub") ;
                            Toast.makeText(MessageActive.this, "message not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lati = latitude;
                    longi =  longitude ;
                }
            }, null);
        }
        else Toast.makeText(MessageActive.this,"Location Error!",Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("NewApi")
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);

        }
        catch (CameraAccessException e) {
        }
    }

    @SuppressLint("NewApi")
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }
}
