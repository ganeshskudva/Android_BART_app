package com.gkudva.bart.Models;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by gkudva on 11/7/15.
 */
public class BartSugarCRMModel extends SugarRecord<BartSugarCRMModel> {
    String origin;
    String destination;
    String identifier;

    public BartSugarCRMModel()
    {

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


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public BartSugarCRMModel(String origin, String destination) {
        this.origin = origin;
        this.identifier = Long.toString((new Date()).getTime());
        this.destination = destination;

    }
}
