package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersCountRequest {

    private AuthToken authtoken;
    private User user;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowersCountRequest() {}

    public GetFollowersCountRequest(AuthToken authtoken, User user) {
        this.authtoken = authtoken;
        this.user = user;
    }

    public AuthToken getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(AuthToken authtoken) {
        this.authtoken = authtoken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
