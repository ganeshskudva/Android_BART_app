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
public class BartHolidayModel implements Serializable {
    private String name;
    private String date;
    private String day;
    private final static String KEY_HOLIDAY = "holidays";
    private final static String KEY_HOLIDAY_NAME = "name";
    private final static String KEY_HOLIDAY_DATE = "date";
    private final static String KEY_HOLIDAY_SCHEDULE_TYPE = "schedule_type";

    public BartHolidayModel() {
        this.name = null;
        this.date = null;
        this.day = null;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public static BartHolidayModel modelFromResponse (Element element)
    {
        BartHolidayModel model = new BartHolidayModel();
        try
        {
            model.setName(getElementValue(element.getElementsByTagName(KEY_HOLIDAY_NAME).item(0)));
            model.setDate(getElementValue(element.getElementsByTagName(KEY_HOLIDAY_DATE).item(0)));
            model.setDay(getElementValue(element.getElementsByTagName(KEY_HOLIDAY_SCHEDULE_TYPE).item(0)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<BartHolidayModel> holidaysFromResponse(String xmlResponse)
    {
        ArrayList<BartHolidayModel> holidayModel = new ArrayList<>();
        try
        {
            Document doc = setupDOMParser(xmlResponse);
            NodeList nodes = doc.getElementsByTagName(KEY_HOLIDAY);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            for (int i = 0; i < childNodeList.getLength(); i++) {
                Element element = (Element) childNodeList.item(i);
                BartHolidayModel model = modelFromResponse(element);
                holidayModel.add(model);

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return holidayModel;
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
