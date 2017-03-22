/**
 * Copyright 2017 Laboratoire I3S, CNRS, Université côte d'azur
 * Author: Savino Dambra
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

/**
 * A video object encapsulates all the parameters related to a video in the XML file.
 * Each video object is built when parsing the video xml file and used to fill a list with all the video-items that can be played.
 */
public class Video {

    //Private attributes
    private String name;
    private String link;
    private String bitrate;
    private String standard;
    private String others;

    //Default constructor
    public Video() {
        this.name = this.link = this.bitrate = this.standard = this.others = "";
    }

    //Get function
    public String getLink() {
        return link;
    }

    //Set functions used when parsing the XML file
    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    //Formatting the output to show it in the list
    @Override
    public String toString() {
        return this.name + "\n" + this.link + "\n" + this.bitrate + " - " + this.standard + " - " + this.others;
    }
}
