/*
 Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 Author: Savino Dambra
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
    private String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean permissionStatus = false;
    private final int permsRequestCode = 1;
    private Button updateButton;
    private Button videoListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Defining Attributes
        Toolbar toolbar;
        Button videoUrlButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //If the application is run the first time, load preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Retrieving all the buttons
        videoListButton = (Button) findViewById(R.id.videoList);
        videoUrlButton = (Button) findViewById(R.id.videoUrl);
        updateButton = (Button) findViewById(R.id.update);

        //Since API 23 we need RUNTIME permission to Access InternalStorage. Checking permissions
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission(permsRequestCode);
                permissionStatus = false;
                videoListButton.setAlpha((float) .3);
                updateButton.setAlpha((float) .3);
            } else {
                permissionStatus = true;
            }
        } else {
            permissionStatus = true;
        }

        //Listener for the videoList Button: it launches VideoListActivity
        videoListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(permissionStatus) {
                    Intent intent = new Intent(getApplicationContext(), VideoListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ERROR_permissionNotGranted), Toast.LENGTH_SHORT).show();
                }
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
                if(permissionStatus) {
                    Context context = getApplicationContext();
                    //Is the phone connected to the Internet?
                    if(UpdateXml.isNetworkAvailable(context))
                        //Yes: Download the file
                        UpdateXml.update(context);
                    else
                        //Error Message
                        Toast.makeText(context, getResources().getString(R.string.ERROR_notOnline), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ERROR_permissionNotGranted), Toast.LENGTH_SHORT).show();
                }
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
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    //Asking permission function
    private void requestPermission(int permsRequestCode) {
        ActivityCompat.requestPermissions(MainActivity.this, perms, permsRequestCode);
    }

    //Call back method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case permsRequestCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionStatus = true;
                    videoListButton.setAlpha(1);
                    updateButton.setAlpha(1);
                }
            }
        }
    }
}
