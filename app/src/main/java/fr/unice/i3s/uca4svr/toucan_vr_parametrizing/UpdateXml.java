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

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.webkit.URLUtil;
import android.widget.Toast;

abstract class UpdateXml {

    /**
     * Download from Internet a video XML-file containing a list of video that can be played.
     * The downloaded file is saved in the same folder of the local file.
     * @param applicationContext Activity context used to show toasts and to use the Download Manager
     */
    static void update (Context applicationContext) {

        //Retrieving preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        //Validate remote URL
        String remoteUrl = preferences.getString("remoteVideoXMLFile", null);
        if(URLUtil.isValidUrl(remoteUrl)) {
            //Download and local URI
            Uri remoteUri = Uri.parse(remoteUrl);
            Uri localUri = Uri.parse(preferences.getString("localVideoXMLFile", null));

            //Building the filename
            String localPath = localUri.getPath().substring(0, localUri.getPath().length() - localUri.getLastPathSegment().length());
            String filename = remoteUri.getLastPathSegment();

            // Create request for android download manager
            DownloadManager downloadManager = (DownloadManager) applicationContext.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(remoteUri);

            //Setting title of request
            request.setTitle("Video XML Download");
            //Setting description of request
            request.setDescription("Update Video list with remote xml File");

            //Set the local destination for the downloaded file
            request.setDestinationInExternalPublicDir(localPath, filename);
            //Start download
            downloadManager.enqueue(request);
        } else {
            //Retrieving and showing the error message
            Toast.makeText(applicationContext, applicationContext.getResources().getString(R.string.ERROR_notWellFormedUrl), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if the network is available
     * @param applicationContext Activity context used to build a ConnectivityManager instance
     * @return True if the network is available, false otherwise
     */
    static boolean isNetworkAvailable(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}