package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedRequest {
    private Authtoken authtoken;
    private Status lastStatus;
    private String alias;
    private int limit;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FeedRequest() {}

    public FeedRequest(Authtoken authtoken, Status lastStatus, String alias, int limit) {
        this.authtoken = authtoken;
        this.lastStatus = lastStatus;
        this.alias = alias;
        this.limit = limit;
    }

    public Authtoken getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(Authtoken authtoken) {
        this.authtoken = authtoken;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
