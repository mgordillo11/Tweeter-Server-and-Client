package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.CountNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.FollowStatusHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.PageNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowUserStatusObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.IsFollowerNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    //This is to load more followees
    public void loadMoreFollowees(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PageNotificationObserver<User> getFolloweesUserObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PageNotificationHandler<>(getFolloweesUserObserver));

        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    //This is to load more followers
    public void loadMoreFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PageNotificationObserver<User> getFollowersUserObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PageNotificationHandler<>(getFollowersUserObserver));

        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public void followUser(AuthToken authToken, User selectedUser, FollowUserStatusObserver followUserObserver) {
        FollowTask followTask = new FollowTask(authToken,
                selectedUser, new FollowStatusHandler(followUserObserver));

        BackgroundTaskUtils.runTask(followTask);
    }

    public void unfollowUser(AuthToken authToken, User selectedUser, FollowUserStatusObserver unfollowUserObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                selectedUser, new FollowStatusHandler(unfollowUserObserver));

        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, IsFollowerNotificationObserver isFollowerNotificationObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currUser, selectedUser, new IsFollowerNotificationHandler(isFollowerNotificationObserver));

        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User selectedUser, CountNotificationObserver getFollowersCountObserver, CountNotificationObserver getFollowingCountObserver) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new CountNotificationHandler(getFollowersCountObserver));

        BackgroundTaskUtils.runTask(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                selectedUser, new CountNotificationHandler(getFollowingCountObserver));

        BackgroundTaskUtils.runTask(followingCountTask);
    }
}
