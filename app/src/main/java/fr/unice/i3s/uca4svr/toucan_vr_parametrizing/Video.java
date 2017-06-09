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
 */
package fr.unice.i3s.uca4svr.toucan_vr_parametrizing;

/**
 * A video object encapsulates all the parameters related to a video in the XML file.
 * Each video object is built when parsing the video xml file and used to fill a list with all the video-items that can be played.
 */
class Video {

    //Private attributes
    private String name;
    private String link;
    private String bitrate;
    private String standard;
    private String others;
    private String W;
    private String H;
    private String tiling;
    private String dynamicEditingFN;

    //Default constructor
    Video() {
        this.name = this.link = this.bitrate = this.standard = this.others = this.W = this.H = this.tiling = this.dynamicEditingFN = "";
    }

    //Get functions
    String getLink() {
        return link;
    }

    String getName() {
        return name;
    }

    String getW() { return W; }

    String getH() { return H; }

    String getTiling() { return tiling; }

    String getDynamicEditingFN() { return dynamicEditingFN; }

    //Set functions used when parsing the XML file
    void setName(String name) {
        this.name = name;
    }

    void setLink(String link) {
        this.link = link;
    }

    void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    void setStandard(String standard) {
        this.standard = standard;
    }

    void setOthers(String others) {
        this.others = others;
    }

    void setW(String W) { this.W = W; }

    void setH(String H) { this.H = H; }

    void setTiling(String tiling) { this.tiling = tiling; }

    void setDynamicEditingFN(String dynamicEditingFN) { this.dynamicEditingFN = dynamicEditingFN; }

    //Formatting the output to show it in the list
    @Override
    public String toString() {
        return this.name + "\n" +
                this.link + "\n" +
                this.bitrate + " - " + this.standard + " - " + this.others+ "\n" +
                "Tiling Grid: " + this.W + " x " + this.H+ "\n" +
                "Dynamic Editing: "+this.dynamicEditingFN;
    }
}
