package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Authtoken;

public class GetUserRequest {
    private Authtoken authToken;
    private String alias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetUserRequest() {}

    public GetUserRequest(Authtoken authToken, String alias) {
        this.authToken = authToken;
        this.alias = alias;
    }

    public Authtoken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(Authtoken authToken) {
        this.authToken = authToken;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
