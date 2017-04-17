package com.gkudva.bart.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.Serializable;
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
public class BartTripModel implements Serializable{
    private String origin;
    private String destination;
    private String fare;
    private String originTime;
    private String destTime;
    private String travelDuration;
    private String originTimeDate;
    private String destTimeDate;
    private static String scheduleRequestedTime;
    private static String scheduleRequestedDate;
    private String arriveTime;
    private boolean transferInvolved;
    private List<BartTripModel> transferStationList;

    private final static String KEY_REQUEST = "request";
    private final static String KEY_ORIGIN = "origin";
    private final static String KEY_DEST = "destination";
    private final static String KEY_FARE = "fare";
    private final static String KEY_ORIGINTIME = "origTimeMin";
    private final static String KEY_DESTTIME = "destTimeMin";
    private final static String KEY_TRIP = "trip";
    private final static String KEY_ORGDATE = "origTimeDate";
    private final static String KEY_EMISSIONS = "co2_emissions";
    private final static String KEY_TIME = "time";
    private final static String KEY_DATE = "date";

    public List<BartTripModel> getTransferStationList() {
        return transferStationList;
    }

    public void setTransferStationList(List<BartTripModel> transferStationList) {
        this.transferStationList = transferStationList;
    }

    public boolean isTransferInvolved() {
        return transferInvolved;
    }

    public void setTransferInvolved(boolean transferInvolved) {
        this.transferInvolved = transferInvolved;
    }

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


    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public static String getScheduleRequestedTime() {
        return scheduleRequestedTime;
    }

    public void setScheduleRequestedTime(String scheduleRequestedTime) {
        this.scheduleRequestedTime = scheduleRequestedTime;
    }

    public static BartTripModel ModelfromXML(Node node)
    {
        BartTripModel model = new BartTripModel();
        ArrayList<BartTransferModel> stationList = new ArrayList<>();
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
                    model.getDestTime(),
                    false));
            model.setArriveTime(timeDiff(scheduleRequestedDate,
                                         scheduleRequestedTime,
                                         model.getOriginTimeDate(),
                                         model.getOriginTime(),
                                         true));

            NodeList childNodeList = node.getChildNodes();
            /*First node only contains fare details. Start iterating from second node*/
            for (int i = 1; i < childNodeList.getLength(); i++)
            {
                BartTransferModel modelTrnsfs = BartTransferModel.ModelfromXML(childNodeList.item(i));
                stationList.add(modelTrnsfs);
            }
            model.setTransferStationList((List)stationList);
            if (stationList.size() > 1)
            {
                model.setTransferInvolved(true);
            }
            else
            {
                model.setTransferInvolved(false);
            }

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

            Document doc = setupDOMParser(xmlResponse);
            NodeList timeNode = doc.getElementsByTagName(KEY_TIME);
            scheduleRequestedTime = getElementValue(timeNode.item(0));
            NodeList dateNode = doc.getElementsByTagName(KEY_DATE);
            scheduleRequestedDate = convertDate(getElementValue(dateNode.item(0)));
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
                                   String destTime,
                                   Boolean arrivalTime)
    {
        long diff;
        double diffInHours = 0l;
        double diffInMin = 0l;
        String timeDiffStr = "";
        try
        {
            String format = "MM/dd/yyyy hh:mm a";
            DecimalFormat decimalFormat = new DecimalFormat("#");

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(orgDate + " " + orgTime);
            Date dateObj2 = sdf.parse(destDate + " " + destTime);

            diff = dateObj2.getTime() - dateObj1.getTime();
            if ((diff < 0) && arrivalTime)
            {
                /*This happens around midnight. If user wants schedule at around 11:58PM, Bart api returns schedule prior to 11;58Pm instead of next day's (AM) schedule's*/
                if (!isDateAfter(orgDate, destDate)) {
                    timeDiffStr = "Departed";
                    return timeDiffStr;
                }
            }
            if (diff < 0)
            {
                /*Train reaches destination the next day*/
                diff = dateObj1.getTime() - dateObj2.getTime();
            }
            diffInHours = diff / (double)((1000 * 60 * 60));
            if (diffInHours < 1)
            {
                /*Difference in time is less than an hour*/
                diffInHours = diff / ((double) 1000 * 60);
                if (diffInHours == 0 )
                {
                    timeDiffStr = "Arriving";
                }
                else {
                    /*Add 1 min to Arrival & Travel Duration times as a buffer*/
                    timeDiffStr = decimalFormat.format(diffInHours + 1.0) + " min";
                }
            }
            else
            {
                /*Add 1 min to Arrival & Travel Duration times as a buffer*/
                timeDiffStr = "1 hr "+decimalFormat.format(((diffInHours - 1) * 60) + 1.0) + " min";
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return timeDiffStr;
    }

    private static Boolean isDateAfter(String scheduleRequestedDate,
                                               String trainArrivalDate)
    {
        Boolean isAfter = false;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date schdReqDate = sdf.parse(scheduleRequestedDate);
            Date trnArrDate = sdf.parse(trainArrivalDate);

            if (trnArrDate.after(schdReqDate))
            {
                isAfter = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return isAfter;

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

    public static String getEmissions(String xmlResponse)
    {
        String emissions = "";
        try
        {
            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_EMISSIONS);

            emissions = getElementValue(nodes.item(0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return emissions;
    }

    /*
    * Convert date from Nov 22, 2015 to 11/22/2015 format
    */
    public static String convertDate(String date)
    {
        String convertedDate = "";

        try
        {
            String OLD_FORMAT = "MMM dd, yyyy";
            String NEW_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date oldDate = sdf.parse(date);
            sdf.applyPattern(NEW_FORMAT);
            convertedDate = sdf.format(oldDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertedDate;
    }
}
