/**
 * Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 * Author: Savino Dambra
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Private attributes
    private Toolbar toolbar;
    private Button videoListButton;
    private Button videoUrlButton;
    private Button updateButton;
    private String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private int permsRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Since API 23 we need RUNTIME permission to Access InternalStorage. Checking permissions
        if (!checkPermission()) requestPermission();

        //If the application is run the first time, load preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Retrieving all the buttons
        videoListButton = (Button) findViewById(R.id.videoList);
        videoUrlButton = (Button) findViewById(R.id.videoUrl);
        updateButton = (Button) findViewById(R.id.update);

        //Listener for the videoList Button: it launches VideoListActivity
        videoListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoListActivity.class);
                startActivity(intent);
            }
        });

        //Listener for the videoUrl Button: it launches VideoUrlActivity
        videoUrlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoUrlActivity.class);
                startActivity(intent);
            }
        });

        //Listener for the update Button: it downloads the video xml file from a remote source
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                //Is the phone connected to the Internet?
                if(UpdateXml.isNetworkAvailable(context))
                    //Yes: Download the file
                    UpdateXml.update(context);
                else
                    //Error Message
                    Toast.makeText(context, getResources().getString(R.string.ERROR_notOnline), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Button within toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Checking permission function
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, perms[0]);
        if (result == PackageManager.PERMISSION_GRANTED) return true; else return false;
    }

    //Asking permission function
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, perms, permsRequestCode);
        }
    }
}
