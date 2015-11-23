package com.gkudva.bart.Models;

import org.w3c.dom.Node;

import java.io.Serializable;

/**
 * Created by gkudva on 11/7/15.
 */
public class BartTransferModel implements Serializable {
    private String order;
    private String origin;
    private String destination;
    private String originTime;
    private String destinationTime;
    private String trainHeadStation;
    private String bikeFlag;

    private static final String KEY_ORDER = "order";
    private static final String KEY_ORIGIN = "origin";
    private final static String KEY_DEST = "destination";
    private final static String KEY_ORIGINTIME = "origTimeMin";
    private final static String KEY_DESTTIME = "destTimeMin";
    private final static String KEY_TRAIN_HEAD = "trainHeadStation";
    private final static String KEY_BIKE_FLAG = "bikeflag";

    public BartTransferModel() {
        this.order = null;
        this.origin = null;
        this.destination = null;
        this.originTime = null;
        this.destinationTime = null;
    }

    public String getOrder() {

        return order;
    }

    public void setOrder(String order) {
        this.order = order;
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

    public String getOriginTime() {
        return originTime;
    }

    public void setOriginTime(String originTime) {
        this.originTime = originTime;
    }

    public String getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(String destinationTime) {
        this.destinationTime = destinationTime;
    }

    public String getTrainHeadStation() {
        return trainHeadStation;
    }

    public void setTrainHeadStation(String trainHeadStation) {
        this.trainHeadStation = trainHeadStation;
    }

    public String getBikeFlag() {
        return bikeFlag;
    }

    public void setBikeFlag(String bikeFlag) {
        this.bikeFlag = bikeFlag;
    }

    public static BartTransferModel ModelfromXML(Node node)
    {
        BartTransferModel model = new BartTransferModel();
        try
        {
            model.setOrder(getValuefromElement(node, KEY_ORDER));
            model.setOrigin(getValuefromElement(node, KEY_ORIGIN));
            model.setDestination(getValuefromElement(node, KEY_DEST));
            model.setOriginTime(getValuefromElement(node, KEY_ORIGINTIME));
            model.setDestinationTime(getValuefromElement(node, KEY_DESTTIME));
            model.setTrainHeadStation(getValuefromElement(node, KEY_TRAIN_HEAD));
            model.setBikeFlag(getValuefromElement(node, KEY_BIKE_FLAG));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static String getValuefromElement(Node node, String tag)
    {
        return node.getAttributes().getNamedItem(tag).getNodeValue();
    }
}
