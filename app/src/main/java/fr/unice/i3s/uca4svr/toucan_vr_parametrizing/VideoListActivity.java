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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    //Private attributes
    private List<Video> videos;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        //Defining variables
        ListView videoListViews;

        //Retrieving preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Retrieving the listView containing the Videos with their infos
        videoListViews = (ListView) findViewById(R.id.videos);

        //Filling the listview with the XML file content
        videos = null;
        XmlParser parser = new XmlParser();
        File file = new File("/storage/emulated/0/"+preferences.getString("localVideoXMLFile", null));
        //Checking whether the file exists or not
        if(file.exists()) {
            //Creating the video list with attributes
            videos = parser.parse(file);
            ArrayAdapter<Video> adapter = new ArrayAdapter<>(this, R.layout.listviewitems, videos);
            videoListViews.setAdapter(adapter);

            //ListView listener for catching the clicked item
            videoListViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Clicked item is given by the attribute "position".
                    // Retrieving the link of the chosen video and start VR activity
                    StartVRApp.startVR(getApplicationContext(),
                            videos.get(position).getName(),
                            videos.get(position).getLink(),
                            videos.get(position).getW(),
                            videos.get(position).getH(),
                            videos.get(position).getTiling(),
                            videos.get(position).getDynamicEditingFN(),
                            preferences);
                }
            });
        } else {
            //Error Message video file doesn't exist
            String notFoundVideoXml = getApplicationContext().getResources().getString(R.string.ERROR_notFoundVideoXml);
            Toast.makeText(getApplicationContext(), notFoundVideoXml, Toast.LENGTH_SHORT).show();
        }
    }
}
