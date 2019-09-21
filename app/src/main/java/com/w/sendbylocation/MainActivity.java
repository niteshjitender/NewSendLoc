package com.w.sendbylocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int prmission_chk_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ;
        if(prmission_chk_sms == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "HEYYYYYYYYYYYYYYYYYYY", Toast.LENGTH_SHORT).show();
            Log.wtf("a","Permission granted") ;
            startActivity(new Intent(MainActivity.this,MessageActive.class));
        }
        else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
    }

}
