package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.util.Pair;

public interface IFollowDAO {
    Pair<List<String>, Boolean> getFollowees(FollowingRequest request);

    Pair<List<String>, Boolean> getFollowers(FollowersRequest request);

    FollowResponse follow(String follower, String followee);

    UnfollowResponse unfollow(String follower, String followee);

    IsFollowerResponse isFollowing(IsFollowerRequest request);


}
