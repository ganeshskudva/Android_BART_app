package com.gkudva.bart.Models;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gkudva on 10/31/15.
 */
public class BartStationInfoModel implements Serializable{
    private String parking;

    public String getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(String parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    private String bikeParking;
    private String bikeStation;
    private String locker;
    private String parkingInfo;
    private String parkingFillTime;
    private String transitInfo;
    private static String KEY_STATION = "stations";
    private static String KEY_PARKING_FLAG = "parking_flag";
    private static String KEY_BIKE_FLAG = "bike_flag";
    private static String KEY_BIKE_STAION_FLAG = "bike_station_flag";
    private static String KEY_LOCKER_FLAG = "locker_flag";
    private static String KEY_PARKING_FILL_TIME = "fill_time";
    private static String KEY_TRANSIT_INFO = "transit_info";
    private static String KEY_PARKING = "parking";
    private static String DEBUG_TAG = "Ganesh";

    public BartStationInfoModel() {
        this.parking = null;
        this.bikeParking = null;
        this.bikeStation = null;
        this.locker = null;
        this.parkingFillTime = null;
        this.transitInfo = null;
        this.parkingInfo = null;
    }

    public String getParking() {

        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getBikeParking() {
        return bikeParking;
    }

    public void setBikeParking(String bikeParking) {
        this.bikeParking = bikeParking;
    }

    public String getBikeStation() {
        return bikeStation;
    }

    public void setBikeStation(String bikeStation) {
        this.bikeStation = bikeStation;
    }

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public String getParkingFillTime() {
        return parkingFillTime;
    }

    public void setParkingFillTime(String parkingFillTime) {
        this.parkingFillTime = parkingFillTime;
    }

    public String getTransitInfo() {
        return transitInfo;
    }

    public void setTransitInfo(String transitInfo) {
        this.transitInfo = transitInfo;
    }

    public static BartStationInfoModel fromXML(String xmlResponse)
    {
        BartStationInfoModel model = new BartStationInfoModel();
        try
        {
            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_STATION);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            model.setParking(getValuefromElement(childNodeList.item(0), KEY_PARKING_FLAG));
            model.setBikeParking(getValuefromElement(childNodeList.item(0), KEY_BIKE_FLAG));
            model.setBikeStation(getValuefromElement(childNodeList.item(0), KEY_BIKE_STAION_FLAG));
            model.setLocker(getValuefromElement(childNodeList.item(0), KEY_LOCKER_FLAG));

            NodeList tmpNodeList = childNodeList.item(0).getChildNodes();
            for (int i =0; i < tmpNodeList.getLength(); i++)
            {
                Element elem = (Element) tmpNodeList.item(i);
                if (elem.getTagName().equals(KEY_PARKING_FILL_TIME))
                {
                    model.setParkingFillTime(getElementValue(tmpNodeList.item(i)));
                }
                else if (elem.getTagName().equals(KEY_TRANSIT_INFO))
                {
                    model.setTransitInfo(getElementValue(tmpNodeList.item(i)));
                }
                else if (elem.getTagName().equals(KEY_PARKING))
                {
                    model.setParkingInfo(getElementValue(tmpNodeList.item(i)));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
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
        Log.d(DEBUG_TAG, "getElementValue");
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public static String getValuefromElement(Node node, String tag)
    {
        return node.getAttributes().getNamedItem(tag).getNodeValue();
    }
}
