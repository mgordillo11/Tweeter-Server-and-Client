package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        // Eventually, this will post the status to the database.
        if (request.getStatus() == null) {
            throw new RuntimeException("[BadRequest400] Status is null");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[BadRequest400] AuthToken is null");
        } else if(request.getStatus().post == null) {
            throw new RuntimeException("[BadRequest400] Status text is null");
        }

        return new PostStatusResponse();
    }
}
