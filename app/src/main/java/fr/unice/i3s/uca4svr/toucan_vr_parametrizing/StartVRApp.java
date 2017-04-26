/*
 * Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
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
 * Author: Savino Dambra
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
     * @param videoName Name of the video used for the logging system
     * @param videoLink Video URL to be played in VR
     * @param W Number of rows in the tiling grid
     * @param H Number of columns in the tiling grid
     * @param tiling Grid composition: quartets of values formatted as x,y,w,h
     * @param preferences If preferences have already been recovered, parameters are retrieved from them
     */
    static void startVR (Context applicationContext, String videoName, String videoLink, String W, String H, String tiling, SharedPreferences preferences) {
        //Retrieving preferences
        if(preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Retrieving the VR application Name from preferences
        Intent launchIntent = applicationContext.getPackageManager().getLaunchIntentForPackage(preferences.getString("vrApplicationPackage", null));
        //If package has been found start the activity
        if (launchIntent != null) {
            //Putting extra parameters
            launchIntent.putExtra("videoLink", videoLink);
            launchIntent.putExtra("videoName", videoName);
            launchIntent.putExtra("bufferForPlayback", Integer.parseInt(preferences.getString("bufferForPlayback", null)));
            launchIntent.putExtra("bufferForPlaybackAR", Integer.parseInt(preferences.getString("bufferForPlaybackAR", null)));
            launchIntent.putExtra("minBufferSize", Integer.parseInt(preferences.getString("minBufferSize", null)));
            launchIntent.putExtra("maxBufferSize", Integer.parseInt(preferences.getString("maxBufferSize", null)));
            launchIntent.putExtra("headMotionLogging", preferences.getBoolean("headMotionLogging", true));
            launchIntent.putExtra("bandwidthLogging", preferences.getBoolean("bandwidthLogging", true));
            launchIntent.putExtra("W", Integer.parseInt(W));
            launchIntent.putExtra("H", Integer.parseInt(H));
            launchIntent.putExtra("tilesCSV", tiling);
            applicationContext.startActivity(launchIntent);
        } else {
            //Package not found: Retrieving and showing the error message
            String notFoundPackage = applicationContext.getResources().getString(R.string.ERROR_notFoundPackage);
            Toast.makeText(applicationContext, notFoundPackage, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * When the user has inserted the URL of the video to play, this function starts the VR application.
     * The VR-application package-name is recovered from the app preferences and it is customizable within the app.
     * Before launching the VR application, Exoplayer parameters are recovered and submitted to the VR app together with the video URL
     * @param applicationContext Activity context used to show toasts and to build intent
     * @param videoLink Video URL to be played in VR
     */
    static void startVR (Context applicationContext, String videoName, String videoLink) {
        //Retrieving preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Retrieving the VR application Name from preferences
        Intent launchIntent = applicationContext.getPackageManager().getLaunchIntentForPackage(preferences.getString("vrApplicationPackage", null));
        //If package has been found start the activity
        if (launchIntent != null) {
            //Putting extra parameters
            launchIntent.putExtra("videoLink", videoLink);
            launchIntent.putExtra("videoName", videoName);
            launchIntent.putExtra("bufferForPlayback", Integer.parseInt(preferences.getString("bufferForPlayback", null)));
            launchIntent.putExtra("bufferForPlaybackAR", Integer.parseInt(preferences.getString("bufferForPlaybackAR", null)));
            launchIntent.putExtra("minBufferSize", Integer.parseInt(preferences.getString("minBufferSize", null)));
            launchIntent.putExtra("maxBufferSize", Integer.parseInt(preferences.getString("maxBufferSize", null)));
            launchIntent.putExtra("headMotionLogging", preferences.getBoolean("headMotionLogging", true));
            launchIntent.putExtra("bandwidthLogging", preferences.getBoolean("bandwidthLogging", true));
            launchIntent.putExtra("W", Integer.parseInt(preferences.getString("W", null)));
            launchIntent.putExtra("H", Integer.parseInt(preferences.getString("H", null)));
            launchIntent.putExtra("tilesCSV", preferences.getString("tilesCSV", null));
            applicationContext.startActivity(launchIntent);
        } else {
            //Package not found: Retrieving and showing the error message
            String notFoundPackage = applicationContext.getResources().getString(R.string.ERROR_notFoundPackage);
            Toast.makeText(applicationContext, notFoundPackage, Toast.LENGTH_SHORT).show();
        }
    }
}
