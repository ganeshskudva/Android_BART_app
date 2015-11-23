package com.gkudva.bart.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gkudva on 10/25/15.
 */
public class BartAdvisoryModel {
    private String advisory;
    private final static String KEY_DESC = "description";
    private final static String KEY_TRAINCOUNT = "traincount";
    private final static int CDATA_NODE = 0;
    private final static int TEXT_NODE = 1;

    public BartAdvisoryModel()
    {
        this.advisory = null;
    }

    public BartAdvisoryModel(String advisory) {
        this.advisory = advisory;
    }

    public String getAdvisory() {

        return advisory;
    }

    public void setAdvisory(String advisory) {
        this.advisory = advisory;
    }

    public Document setupDOMParser(String xmlResponse)
    {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlResponse));

            doc = db.parse(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return doc;
    }

    public String advisoryFromResponse (String xmlResponse)
    {
        String advisory = "";
        try {

            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_DESC);
            advisory = getElementValue(nodes.item(0), CDATA_NODE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return advisory;
    }

    public String trainCountFromResponse (String xmlResponse)
    {
        String trainCount = "";
        try {

            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_TRAINCOUNT);
            trainCount = getElementValue(nodes.item(0), TEXT_NODE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return trainCount;
    }

    public final String getElementValue( Node elem, int nodeType ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    switch (nodeType) {
                        case CDATA_NODE:
                            if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                                return child.getNodeValue();
                            }
                            break;
                        case TEXT_NODE:
                            if (child.getNodeType() == Node.TEXT_NODE) {
                                return child.getNodeValue();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return "";
    }
}
