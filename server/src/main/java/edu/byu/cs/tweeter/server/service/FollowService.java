package edu.byu.cs.tweeter.server.service;

import java.util.List;

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
import edu.byu.cs.tweeter.util.Pair;

public class FollowService {
    private final DAOFactory daoFactory;

    public FollowService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authtoken");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new FollowingResponse("Session expired, please log out and log in again");
        }


        Pair<List<User>, Boolean> followees = daoFactory.getFollowDAO().getFollowees(request);
        return new FollowingResponse(followees.getFirst(), followees.getSecond());
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new FollowersResponse("Session expired, please log out and log in again");
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
        if (!validAuthtoken) {
            return new UnfollowResponse("Session expired, please log out and log in again");
        }


        String currentUserAlias = daoFactory.getAuthtokenDAO().getAliasFromAuthToken(request.getAuthtoken());
        String userToFollowAlias = request.getUser().getAlias();

        return daoFactory.getFollowDAO().unfollow(currentUserAlias, userToFollowAlias);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow");
        } else if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new FollowResponse("Session expired, please log out and log in again");
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
        if (!validAuthtoken) {
            return new IsFollowerResponse("Session expired, please log out and log in again");
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
        if (!validAuthtoken) {
            return new GetFollowingCountResponse("Session expired, please log out and log in again");
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
        if (!validAuthtoken) {
            return new GetFollowersCountResponse("Session expired, please log out and log in again");
        }

        int numOfFollowers = daoFactory.getFollowDAO().getFollowersCount(request.getUser().getAlias());
        return new GetFollowersCountResponse(numOfFollowers);
    }
}
