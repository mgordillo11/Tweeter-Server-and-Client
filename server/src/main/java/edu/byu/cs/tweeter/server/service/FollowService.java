package edu.byu.cs.tweeter.server.service;

import java.util.Random;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return getFollowingDAO().getFollowees(request);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to unfollow");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        return new UnfollowResponse();
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        return new FollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }

        return new IsFollowerResponse(new Random().nextInt() > 0);
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }

        return new GetFollowingCountResponse(20);
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }

        return new GetFollowersCountResponse(20);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
