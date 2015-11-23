package com.gkudva.bart.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gkudva on 10/31/15.
 */
public class BartStationModel implements Serializable{
    private String abbreviation;
    private String latitude;
    private String longitude;
    private String address;
    private String city;
    private String zip;
    private static final String KEY_ABBR = "abbr";
    private static final String KEY_LAT = "gtfs_latitude";
    private static final String KEY_LONG = "gtfs_longitude";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_ZIP = "zipcode";
    private static final String KEY_STATION = "stations";

    public BartStationModel() {
        this.abbreviation = null;
        this.latitude = null;
        this.longitude = null;
        this.address = null;
        this.city = null;
        this.zip = null;
    }

    public String getAbbreviation() {

        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public static BartStationModel modelFromResponse (Element element)
    {
        BartStationModel model = new BartStationModel();
        try
        {
            model.setAbbreviation(getElementValue(element.getElementsByTagName(KEY_ABBR).item(0)));
            model.setLatitude(getElementValue(element.getElementsByTagName(KEY_LAT).item(0)));
            model.setLongitude(getElementValue(element.getElementsByTagName(KEY_LONG).item(0)));
            model.setAddress(getElementValue(element.getElementsByTagName(KEY_ADDRESS).item(0)));
            model.setCity(getElementValue(element.getElementsByTagName(KEY_CITY).item(0)));
            model.setZip(getElementValue(element.getElementsByTagName(KEY_ZIP).item(0)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<BartStationModel> stationsFromResponse(String xmlResponse)
    {
        ArrayList<BartStationModel> stationModel = new ArrayList<>();
        try
        {
            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_STATION);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            for (int i = 0; i < childNodeList.getLength(); i++) {
                Element element = (Element) childNodeList.item(i);
                BartStationModel model = modelFromResponse(element);
                stationModel.add(model);

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return stationModel;
    }


    public static Document setupDOMParser(String xmlResponse)
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

    public static final String getElementValue( Node elem) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}
