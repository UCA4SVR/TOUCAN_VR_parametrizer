# TOUCAN_VR_parametrizer

This application has three main purposes:  
1. Tune network parameters and logging features of TOUCAN_VR
2. Choose the content to be played
3. Start TOUCAN_VR

## Build and install

The repository provides an Android Studio project. Clone or download it, import the project in Android Studio, let Gradle check for dependencies and install the application in your favourite device.

## Tuning parameters
The parameter tuning screen is accessible by clicking the icon in the right upper corner of the app first-screen.

<img src="https://preview.ibb.co/iFHUBF/1.jpg" alt="Settings" width="300">    

It is possible to tune the following parameters:    
1. VR application package name
> Once chosen a video, the TOUCAN_VR_parametrizer application calls the VR application: the latter is searched based on the package name inserted in this field.
2. Bandwidth logging
> Choose whether to log the bandwidth consumption or not
3. Head motion logging
> Choose whether to log the head motion or not
4. Local video XML file
> Path of a xml file containing all possible videos the user can play. The path must be a relative path from "/storage/emulated/0/" device directory. The structure of the XML file is reported below.
5. Remote video XML file
> The application is able to download a remote xml file containing the list of videos. It will be downloaded in the same directory currently specified in the previous preference (e.g., if the remote link is http://www.example.org/remotevideo.xml and the local video xml file is Toucan/video.xml the new file will be Toucan/remotevideo.xml)
6. Startup buffer Size
> This parameter will be transmitted to the VR application to set in EXOPlayer the required portion of video (in milliseconds) that must be in the buffer for starting the video playback
7. Startup buffer Size after a re-buffering event
> This parameter will be transmitted to the VR application to set in EXOPlayer the required portion of video (in milliseconds) that must be in the buffer for starting the video playback after a stall event     
8. Min Buffer size     
> The default minimum duration of media (in milliseconds) that the player will attempt to ensure is buffered at all times.     
9. Max Buffer size      
> The default maximum duration of media (in milliseconds) that the player will attempt to buffer. 
10. W      
> Grid Weight of the full video (only when URL is manually inserted)
11. H     
> Grid Height of the full video (only when URL is manually inserted)
12. CSV for tiles     
> A string representing all the tiles each one in the format: x,y,w,h (only when URL is manually inserted)
13. XML for dynamic editing      
> A string representing the file relative path starting from storage/emulated/0/ (only when URL is manually inserted)

## XML video file    
The XML file must contain the following line as first one    
```xml
<?xml version="1.0" encoding="UTF-8"?>
```
The big tag container is &lt;videos&gt;&lt;/videos&gt; and each video entry is enclosed within the tags &lt;video&gt;&lt;/video&gt;. It is possible to specify five (5) video properties including them in the tag "video" and surrounding them with a specific tag as shown below:     
1. &lt;name&gt;:
> Title of the video
2. &lt;link&gt;:
> Full link of the video
3. &lt;bitrate&gt;:
> Video bitrate
4. &lt;standard&gt;:
> Video standard
5. &lt;others&gt;:
> Other video details
6. &lt;W&gt;:
> Grid width of the video
7. &lt;H&gt;:
> Grid height of the video
8. &lt;tiling&gt;:
> Tiling string
9. &lt;dynamic&gt;:     
> Dynamic editing video path

**For our purposes, the only mandatory tag is "link"**    
A full example is now provided:   
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<videos>
    <video>
        <name>Roller Coaster</name>
        <link>http://www.unice.fr</link>
        <bitrate>64k</bitrate>
        <standard>Dash</standard>
        <W>3</W>
        <H>3</H>
        <tiling>0,0,1,1,1,0,1,1,2,0,1,1,0,1,1,1,1,1,1,1,2,1,1,1,0,2,1,1,1,2,1,1,2,2,1,1</tiling>
        <dynamic>toucan/rollerDE.xml</dynamic>
    </video>
    <video>
        <name>Shark</name>
        <link>http://www.unice.fr</link>
        <bitrate>128k</bitrate>
        <standard>Dash-SRD</standard>
        <others>Long video</others>
        <W>3</W>
        <H>3</H>
        <tiling>0,0,1,1,1,0,1,1,2,0,1,1,0,1,1,1,1,1,1,1,2,1,1,1,0,2,1,1,1,2,1,1,2,2,1,1</tiling>
        <dynamic>toucan/SharkDE.xml</dynamic>
    </video>
</videos>
```
  
<img src="https://preview.ibb.co/hptmrF/Screenshot_1490180190.png" alt="main" width="300">    

## Show a list of videos    
By clicking on the button "GO TO THE LIST" a list of videos that can be played is shown. The list is retrieved from the preference #2 so the file must exist and must be formatted properly as shown above. Once chosen an item, the link together with Exoplayer parameters is passed to the VR application.

##  Insert an URL of the video
By clicking on the button "INSERT URL" the user can insert the URL of a video for playing it. Once inserted, the link together with Exoplayer parameters is passed to the VR application.

## Update XML list
By clicking on the button "UPDATE", the remote file specified in the preference #3 is retrieved and saved.    
**Please notice that if it has to be used to fill the list of videos, its path and name must be inserted in the preference #2**    
**Please also notice that if the remote file already exists in the folder (or a file with the same name) it won't be overwritten but the version number will be added before the extension** (e.g., if the file Toucan/video.xml exists a new file with the same name will be saved as Toucan/video-1.xml)
