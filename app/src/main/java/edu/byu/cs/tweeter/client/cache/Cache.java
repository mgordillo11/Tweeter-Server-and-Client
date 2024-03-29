package edu.byu.cs.tweeter.client.cache;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The Cache class stores globally accessible data.
 */
public class Cache {
    private static final Cache instance = new Cache();

    public static Cache getInstance() {
        return instance;
    }

    /**
     * The currently logged-in user.
     */
    private User currUser;
    /**
     * The auth token for the current user session.
     */
    private Authtoken currUserAuthToken;

    private Cache() {
        initialize();
    }

    private void initialize() {
        currUser = new User(null, null, null);
        currUserAuthToken = null;
    }

    public void clearCache() {
        initialize();
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public Authtoken getCurrUserAuthToken() {
        return currUserAuthToken;
    }

    public void setCurrUserAuthToken(Authtoken currUserAuthToken) {
        this.currUserAuthToken = currUserAuthToken;
    }
}
