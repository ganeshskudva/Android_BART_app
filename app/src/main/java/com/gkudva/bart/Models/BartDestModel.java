package com.gkudva.bart.Models;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkudva on 10/24/15.
 */
public class BartDestModel {

    private String Dest;
    private String limited;
    private List<BartDestEstimate> bartDestEstimateList;
    private static final String KEY_DEST = "destination";
    private static final String KEY_LIMITED = "limited";
    private static final String KEY_ESTIMATE = "estimate";

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public BartDestModel(String dest, String limited) {
        Dest = dest;
        this.limited = limited;
    }

    public BartDestModel() {
        this.Dest = null;
        this.limited = null;
        bartDestEstimateList = new ArrayList<BartDestEstimate>();
    }

    public String getLimited() {

        return limited;
    }

    public void setLimited(String limited) {
        this.limited = limited;
    }

    public List<BartDestEstimate> getBartDestEstimateList() {
        return bartDestEstimateList;
    }

    public void setBartDestEstimateList(List<BartDestEstimate> bartDestEstimateList) {
        this.bartDestEstimateList = bartDestEstimateList;
    }

    public void fromXML(Element element)
    {
        NodeList name;
        NodeList nodes;
        Element line;

        setDest(getValuefromElement(element, KEY_DEST));
        setLimited(getValuefromElement(element, KEY_LIMITED));


        nodes = element.getElementsByTagName(KEY_ESTIMATE);

        for (int i = 0; i < nodes.getLength(); i++)
        {
            BartDestEstimate bartDestEstimate = new BartDestEstimate();
            bartDestEstimate.fromXML((Element) nodes.item(i));
            bartDestEstimateList.add(bartDestEstimate);
        }


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
