package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFollowDAO;
import edu.byu.cs.tweeter.util.Pair;

public class DynamoDBFollowDAO implements IFollowDAO {

    @Override
    public Pair<List<String>, Boolean> getFollowees(FollowingRequest request) {
        return null;
    }

    @Override
    public Pair<List<String>, Boolean> getFollowers(FollowersRequest request) {
        return null;
    }

    @Override
    public FollowResponse follow(String follower, String followee) {
        return null;
    }

    @Override
    public UnfollowResponse unfollow(String follower, String followee) {
        return null;
    }

    @Override
    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        return null;
    }
}
