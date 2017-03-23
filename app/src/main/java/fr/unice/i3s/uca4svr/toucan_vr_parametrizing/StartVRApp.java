/*
 Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 Author: Savino Dambra
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

abstract class StartVRApp {
    /**
     * When the user has chosen the video to play (identified by an URL), this function starts the VR application.
     * The VR-application package-name is recovered from the app preferences and it is customizable within the app.
     * Before launching the VR application, Exoplayer parameters are recovered and submitted to the VR app together with the video URL
     * @param applicationContext Activity context used to show toasts and to build intent
     * @param videoLink Video URL to be played in VR
     * @param preferences If preferences have already been recovered, parameters are retrieved from them
     */
    static void startVR (Context applicationContext, String videoLink, SharedPreferences preferences) {
        //Retrieving preferences
        if(preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Retrieving the VR application Name from preferences
        Intent launchIntent = applicationContext.getPackageManager().getLaunchIntentForPackage(preferences.getString("vrApplicationPackage", null));
        //If package has been found start the activity
        if (launchIntent != null) {
            //Putting extra parameters
            launchIntent.putExtra("videoLink", videoLink);
            launchIntent.putExtra("startupBufferSize", preferences.getString("startupBufferSize", null));
            launchIntent.putExtra("bufferSize", preferences.getString("bufferSize", null));
            applicationContext.startActivity(launchIntent);
        } else {
            //Package not found: Retrieving and showing the error message
            String notFoundPackage = applicationContext.getResources().getString(R.string.ERROR_notFoundPackage);
            Toast.makeText(applicationContext, notFoundPackage, Toast.LENGTH_SHORT).show();
        }
    }
}
