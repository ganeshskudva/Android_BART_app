package com.gkudva.bart.Models;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gkudva on 10/23/15.
 */
public class BartModel {
    private String Src;
    private String Dest;
    private String SrcTime;
    private String DestTime;
    private Document doc;
    private List<BartDestModel> DestList;
    private static final String KEY_ETD = "etd";

    public BartModel() {
        this.Src = null;
        this.SrcTime = null;
        this.Dest = null;
        this.DestTime = null;
        DestList = new ArrayList<BartDestModel>();
    }

    public BartModel(String src, String dest, String srcTime, String destTime) {

        Src = src;
        Dest = dest;
        SrcTime = srcTime;
        DestTime = destTime;
    }

    public String getSrc() {

        return Src;
    }

    public void setSrc(String src) {
        Src = src;
    }

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getSrcTime() {
        return SrcTime;
    }

    public void setSrcTime(String srcTime) {
        SrcTime = srcTime;
    }

    public String getDestTime() {
        return DestTime;
    }

    public List<BartDestModel> getDestList() {
        return DestList;
    }

    public void setDestList(List<BartDestModel> destList) {
        DestList = destList;
    }

    public void setDestTime(String destTime) {
        DestTime = destTime;
    }

    public void fromXML(String xmlResponse)
    {
        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlResponse));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(KEY_ETD);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                BartDestModel destModel = new BartDestModel();

                destModel.fromXML((Element) nodes.item(i));

                DestList.add(destModel);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
