package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */

public class Authtoken implements Serializable {
    /**
     * Value of the auth token.
     */
    public String token;

    /**
     * String representation of date/time at which the auth token was created.
     */
    public String datetime;

    public Authtoken() {
    }

    public Authtoken(String token) {
        this.token = token;
    }

    public Authtoken(String token, String datetime) {
        this.token = token;
        this.datetime = datetime;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatetime() {
        return datetime;
    }
}