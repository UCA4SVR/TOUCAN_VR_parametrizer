/*
 * Copyright 2017 Université Nice Sophia Antipolis (member of Université Côte d'Azur), CNRS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CheckConnectionResponse {

    //Private attributes
    private String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean permissionStatus = false;
    private final int permsRequestCode = 1;
    private Button updateButton;
    private Button videoListButton;
    private Button videoUrlButton;
    private SharedPreferences preferences;
    private String remoteVideoFile;
    private String localVideoFile;
    private MainActivity currentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Defining Attributes
        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //If the application is run the first time, load preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Saving the current activity for the update button case
        currentActivity = this;

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
                    //Retrieving URLs from preferences
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    remoteVideoFile = preferences.getString("remoteVideoXMLFile", null);
                    localVideoFile = preferences.getString("localVideoXMLFile", null);
                    //Validate remote URL
                    if(URLUtil.isValidUrl(remoteVideoFile)) {
                        //Checking the connection
                        CheckConnection checkConnection = new CheckConnection(context);
                        checkConnection.response = currentActivity;
                        checkConnection.execute(remoteVideoFile);
                    } else {
                        //Url is not valid
                        //Retrieving and showing the error message
                        Toast.makeText(context, context.getResources().getString(R.string.ERROR_notWellFormedUrl), Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public void urlChecked(boolean exists) {
        if(exists) {
            downloadFile();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ERROR_notOnline), Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadFile() {
        //Download and local URI
        Uri remoteUri = Uri.parse(remoteVideoFile);
        Uri localUri = Uri.parse(localVideoFile);

        //Building the filename
        String localPath = localUri.getPath().substring(0, localUri.getPath().length() - localUri.getLastPathSegment().length());
        String filename = remoteUri.getLastPathSegment();

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(remoteUri);

        //Setting title of request
        request.setTitle("Video XML Download");
        //Setting description of request
        request.setDescription("Update Video list with remote xml File");

        //Set the local destination for the downloaded file
        request.setDestinationInExternalPublicDir(localPath, filename);
        //Start download
        downloadManager.enqueue(request);
    }
}


