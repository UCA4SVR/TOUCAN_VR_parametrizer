/**
 * Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 * Author: Savino Dambra
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class VideoUrlActivity extends AppCompatActivity {

    //Private attributes
    private Button playVideoButton;
    private EditText videoUrlEditText;
    private String url;
    private String notWellFormedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_url);

        //Retrieving the playVideo button
        playVideoButton = (Button) findViewById(R.id.playVideo);
        //Retrieving the videoUrl EditText
        videoUrlEditText = (EditText) findViewById(R.id.videoUrl);

        //Listener for the videoList Button: it launches the VR app if the URL is correct
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = videoUrlEditText.getText().toString();
                //Check URL syntax
                if(URLUtil.isValidUrl(url)) {
                    // Start VR activity
                    StartVRApp.startVR(getApplicationContext(),url,null);
                } else {
                    //Retrieving and showing the error message
                    notWellFormedUrl = getResources().getString(R.string.ERROR_notWellFormedUrl);
                    Toast.makeText(getApplicationContext(), notWellFormedUrl, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
