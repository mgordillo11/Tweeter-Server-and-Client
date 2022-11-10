package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {
    private Authtoken authToken;
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {
    }

    public PostStatusRequest(Authtoken authToken, Status status) {
        this.authToken = authToken;
        this.status = status;
    }

    public Authtoken getAuthToken() {
        return authToken;
    }

    public Status getStatus() {
        return status;
    }

    public void setAuthToken(Authtoken authToken) {
        this.authToken = authToken;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
