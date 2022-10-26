package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.PageNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

        public void loadMoreStories(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PageNotificationObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus, new PageNotificationHandler<>(getStoryObserver));

        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void loadMoreFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PageNotificationObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new PageNotificationHandler<>(getFeedObserver));

        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void postStatus(AuthToken currUserAuthToken, User currUser, String post, String formattedDateTime, List<String> parseURLs, List<String> parseMentions, SimpleNotificationObserver postStatusObserver) {
        Status newStatus = new Status(post, currUser, formattedDateTime, parseURLs, parseMentions);

        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new SimpleNotificationHandler(postStatusObserver));

        BackgroundTaskUtils.runTask(statusTask);
    }
}
