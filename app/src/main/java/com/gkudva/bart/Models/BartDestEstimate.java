package com.gkudva.bart.Models;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by gkudva on 10/24/15.
 */
public class BartDestEstimate {
    private String minutes;
    private String platform;
    private String direction;
    private String trainLen;
    private String bikeflag;
    private String color;
    private String hexColor;
    private static final String KEY_MIN = "minutes";
    private static final String KEY_PLAT = "platform";
    private static final String KEY_DIRECTION = "direction";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_COLOR = "color";
    private static final String KEY_HEXCOLOR = "hexcolor";
    private static final String KEY_BIKEFLAG = "bikeflag";

    public BartDestEstimate(String minutes, String platform, String direction, String trainLen, String bikeflag, String color, String hexColor) {
        this.minutes = minutes;
        this.platform = platform;
        this.direction = direction;
        this.trainLen = trainLen;
        this.bikeflag = bikeflag;
        this.color = color;
        this.hexColor = hexColor;
    }

    public BartDestEstimate() {
        this.minutes = null;
        this.platform = null;
        this.direction = null;
        this.trainLen = null;
        this.bikeflag = null;
        this.color = null;
        this.hexColor = null;
    }

    public String getMinutes() {

        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTrainLen() {
        return trainLen;
    }

    public void setTrainLen(String trainLen) {
        this.trainLen = trainLen;
    }

    public String getBikeflag() {
        return bikeflag;
    }

    public void setBikeflag(String bikeflag) {
        this.bikeflag = bikeflag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public void fromXML(Element element)
    {
        setMinutes(getValuefromElement(element, KEY_MIN));
        setPlatform(getValuefromElement(element, KEY_PLAT));
        setBikeflag(getValuefromElement(element, KEY_BIKEFLAG));
        setColor(getValuefromElement(element, KEY_COLOR));
        setDirection(getValuefromElement(element, KEY_DIRECTION));
        setHexColor(getValuefromElement(element, KEY_HEXCOLOR));
        setDirection(getValuefromElement(element, KEY_DIRECTION));
    }

    public String getValuefromElement(Element element, String tag)
    {
        NodeList name;
        Element line;

        name =  element.getElementsByTagName(tag);
        line = (Element) name.item(0);
        return getCharacterDataFromElement(line);
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
