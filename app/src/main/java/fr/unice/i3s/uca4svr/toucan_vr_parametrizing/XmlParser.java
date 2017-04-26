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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for parsing the video XML file. It uses the recommended Android XML parser XmlPullParser
 */
class XmlParser {

    //private attributes
    private List<Video> videos;
    private Video video;
    private String text;

    //Main constructor
    XmlParser() {
        videos = new ArrayList<>();
    }

    //Main parse method
    List<Video> parse(File file) {
        XmlPullParserFactory factory;
        XmlPullParser parser;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(new FileInputStream(file),"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("video")) {
                            video = new Video();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("video")) {
                            // add video object to list
                            videos.add(video);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            if (video!=null) video.setName(text);
                        } else if (tagname.equalsIgnoreCase("link")) {
                            if (video!=null) video.setLink(text);
                        } else if (tagname.equalsIgnoreCase("bitrate")) {
                            if (video!=null) video.setBitrate(text);
                        } else if (tagname.equalsIgnoreCase("standard")) {
                            if (video!=null) video.setStandard(text);
                        } else if (tagname.equalsIgnoreCase("others")) {
                            if (video!=null) video.setOthers(text);
                        } else if (tagname.equalsIgnoreCase("W")) {
                            if (video!=null) video.setW(text);
                        } else if (tagname.equalsIgnoreCase("H")) {
                            if (video!=null) video.setH(text);
                        } else if (tagname.equalsIgnoreCase("tiling")) {
                            if (video!=null) video.setTiling(text);
                        }


                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return videos;
    }

}