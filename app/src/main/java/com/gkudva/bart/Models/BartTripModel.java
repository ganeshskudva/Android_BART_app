package com.gkudva.bart.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by gkudva on 10/24/15.
 */
public class BartTripModel {
    private String origin;
    private String destination;
    private String fare;
    private String originTime;
    private String destTime;
    private String travelDuration;
    private String originTimeDate;
    private String destTimeDate;

    private final static String KEY_REQUEST = "request";
    private final static String KEY_ORIGIN = "origin";
    private final static String KEY_DEST = "destination";
    private final static String KEY_FARE = "fare";
    private final static String KEY_ORIGINTIME = "origTimeMin";
    private final static String KEY_DESTTIME = "destTimeMin";
    private final static String KEY_TRIP = "trip";
    private final static String KEY_ORGDATE = "origTimeDate";

    public String getOriginTimeDate() {
        return originTimeDate;
    }

    public void setOriginTimeDate(String originTimeDate) {
        this.originTimeDate = originTimeDate;
    }

    public String getDestTimeDate() {
        return destTimeDate;
    }

    public void setDestTimeDate(String destTimeDate) {
        this.destTimeDate = destTimeDate;
    }

    private final static String KEY_DESTDATE = "destTimeDate";
    private final static String DEBUG_KEY = "Ganesh";

    public BartTripModel() {
        this.origin = null;
        this.destination = null;
        this.fare = null;
        this.originTime = null;
        this.destTime = null;
    }

    public String getOrigin() {

        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getOriginTime() {
        return originTime;
    }

    public void setOriginTime(String originTime) {
        this.originTime = originTime;
    }

    public String getDestTime() {
        return destTime;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }

    public String getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }


    public static BartTripModel ModelfromXML(Node node)
    {
        BartTripModel model = new BartTripModel();
        try {

            model.setOrigin(getValuefromElement(node, KEY_ORIGIN));
            model.setDestination(getValuefromElement(node, KEY_DEST));
            model.setFare(getValuefromElement(node, KEY_FARE));
            model.setDestTime(getValuefromElement(node, KEY_DESTTIME));
            model.setOriginTime(getValuefromElement(node, KEY_ORIGINTIME));
            model.setOriginTimeDate(getValuefromElement(node, KEY_ORGDATE));
            model.setDestTimeDate(getValuefromElement(node, KEY_DESTDATE));
            model.setTravelDuration(timeDiff(model.getOriginTimeDate(),
                                             model.getOriginTime(),
                                             model.getDestTimeDate(),
                                             model.getDestTime()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static List<BartTripModel> fromXML(String xmlResponse)
    {

        ArrayList<BartTripModel> bartTripModelList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlResponse));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(KEY_REQUEST);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            for (int i = 0; i < childNodeList.getLength(); i++) {
                    BartTripModel model = ModelfromXML(childNodeList.item(i));
                    bartTripModelList.add(model);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return bartTripModelList;
    }

    public static String getValuefromElement(Node node, String tag)
    {
        return node.getAttributes().getNamedItem(tag).getNodeValue();
    }

    private static String timeDiff(String orgDate,
                                   String orgTime,
                                   String destDate,
                                   String destTime)
    {
        long diff;
        double diffInHours = 0l;
        String timeDiffStr = "";
        try
        {
            String format = "MM/dd/yyyy hh:mm a";
            DecimalFormat decimalFormat = new DecimalFormat("#");

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(orgDate + " " + orgTime);
            Date dateObj2 = sdf.parse(destDate + " " + destTime);

            diff = dateObj2.getTime() - dateObj1.getTime();
            diffInHours = diff / ((double) 1000 * 60 * 60);
            if (diffInHours < 1)
            {
                /*Difference in time is less than an hour*/
                diffInHours = diff / ((double) 1000 * 60);
                timeDiffStr = decimalFormat.format(diffInHours) + " min";
            }
            else
            {
                timeDiffStr = "1 hr "+decimalFormat.format((diffInHours - 1) * 100) + " min";
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return timeDiffStr;
    }
}
