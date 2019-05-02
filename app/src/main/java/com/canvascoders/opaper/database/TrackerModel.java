package com.canvascoders.opaper.database;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Nikhil Prajapati on 26-02-2018.
 */

public class TrackerModel extends RealmObject {

    private int id;
    private String agentID;
    private String processID;
    private String screenName;
    private String actionTaken;
    private String actionRequest;
    private String actionResponse;
    private String docID;
    private String dateNtime;
    private boolean isSync = false;

    public TrackerModel(int id, String agentID, String processID, String screenName, String actionTaken, String actionRequest, String actionResponse, String docID) {
        this.id = id;
        this.agentID = agentID;
        this.processID = processID;
        this.screenName = screenName;
        this.actionTaken = actionTaken;
        this.actionRequest = actionRequest;
        this.actionResponse = actionResponse;
        this.docID = docID;
        this.dateNtime = new Date().toString();
    }

    public TrackerModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getActionRequest() {
        return actionRequest;
    }

    public void setActionRequest(String actionRequest) {
        this.actionRequest = actionRequest;
    }

    public String getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(String actionResponse) {
        this.actionResponse = actionResponse;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDateNtime() {
        return dateNtime;
    }

    public void setDateNtime(String dateNtime) {
        this.dateNtime = dateNtime;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }
}
