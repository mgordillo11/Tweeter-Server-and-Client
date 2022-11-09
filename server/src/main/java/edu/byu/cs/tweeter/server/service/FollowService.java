package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDB.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private DAOFactory daoFactory;

    public FollowService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

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

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new FollowingResponse("Authtoken is invalid, and User is no longer active");
        }


        Pair<List<User>, Boolean> followees = daoFactory.getFollowDAO().getFollowees(request);
        return new FollowingResponse(followees.getFirst(), followees.getSecond());
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if(request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getLastFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a last follower alias");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new FollowersResponse("Authtoken is invalid, and User is no longer active");
        }

        Pair<List<User>, Boolean> followers = daoFactory.getFollowDAO().getFollowers(request);
        return new FollowersResponse(followers.getFirst(), followers.getSecond());
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to unfollow");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new UnfollowResponse("Authtoken is invalid, and User is no longer active");
        }

        return new UnfollowResponse();
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new FollowResponse("Authtoken is invalid, and User is no longer active");
        }

        String currentUserAlias = daoFactory.getAuthtokenDAO().getAliasFromAuthToken(request.getAuthtoken());
        String userToFollowAlias = request.getUser().getAlias();

        return daoFactory.getFollowDAO().follow(currentUserAlias, userToFollowAlias);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new IsFollowerResponse("Authtoken is invalid, and User is no longer active");
        }

        return daoFactory.getFollowDAO().isFollowing(request);
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new GetFollowingCountResponse("Authtoken is invalid, and User is no longer active");
        }

        int numOfFollowees = daoFactory.getFollowDAO().getFollowingCount(request.getUser().getAlias());
        return new GetFollowingCountResponse(numOfFollowees);
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if(!validAuthtoken) {
            return new GetFollowersCountResponse("Authtoken is invalid, and User is no longer active");
        }

        int numOfFollowers = daoFactory.getFollowDAO().getFollowersCount(request.getUser().getAlias());
        return new GetFollowersCountResponse(numOfFollowers);
    }
}
