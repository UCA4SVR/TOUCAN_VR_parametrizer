/*
 Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 Author: Savino Dambra
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
                    StartVRApp.startVR(getApplicationContext(), videos.get(position).getLink(), preferences);
                }
            });
        } else {
            //Error Message video file doesn't exist
            String notFoundVideoXml = getApplicationContext().getResources().getString(R.string.ERROR_notFoundVideoXml);
            Toast.makeText(getApplicationContext(), notFoundVideoXml, Toast.LENGTH_SHORT).show();
        }
    }
}
