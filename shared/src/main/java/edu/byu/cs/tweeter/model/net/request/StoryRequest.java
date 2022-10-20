package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StoryRequest {
    private AuthToken authtoken;
    private String alias;
    private int limit;
    private Status lastStatus;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StoryRequest() {
    }

    public StoryRequest(AuthToken authToken, String alias, int limit, Status lastStatus) {
        this.authtoken = authToken;
        this.alias = alias;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public AuthToken getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(AuthToken authtoken) {
        this.authtoken = authtoken;
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

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
