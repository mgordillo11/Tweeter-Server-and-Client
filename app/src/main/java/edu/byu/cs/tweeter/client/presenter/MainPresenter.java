package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowUserStatusObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.IsFollowerNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {
    private final MainView mainActivityView;
    private StatusService statusService;

    private static final String LOG_TAG = "MainActivity";

    public interface MainView extends View {
        void setFollowersCount(int count);

        void setFollowingCount(int count);

        void setFollowButton(boolean isFollower);

        void enableFollowButton(boolean status);

        void updateFollowButton(boolean removed);

        void updateSelectedUserFollowingAndFollowees();

        void showPostingToast(boolean toastStatus);

        void logoutUser();
    }

    public MainPresenter(MainView view) {
        mainActivityView = view;
        //statusService = getStatusService();
    }

    public StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }

        return statusService;
    }

    public void statusPost(String post) {
        try {
            getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), post, getFormattedDateTime(), parseURLs(post), parseMentions(post), new PostStatusObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            mainActivityView.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public void logoutUser() {
        userService.logoutUser(Cache.getInstance().getCurrUserAuthToken(), new LogoutUserObserver());
    }

    public void followUser(User selectedUser) {
        followService.followUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowUserObserver());
    }

    public void unfollowUser(User selectedUser) {
        followService.unfollowUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new UnfollowUserObserver());
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerUserObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

//    public String getFormattedDateTime() throws ParseException {
//        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");
//        String test1 = LocalDate.now().toString();
//        String test2 = LocalTime.now().toString().substring(0, 8);
//
//        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
//    }

    public String getFormattedDateTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }
        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }
        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private class PostStatusObserver extends SimpleNotificationObserver {
        public PostStatusObserver() {
            super(mainActivityView, "Failed to post status: ", "Failed to post status because of exception: ");
        }

        @Override
        public void handleSuccess() {
            mainActivityView.showPostingToast(false);
            mainActivityView.displayMessage("Successfully Posted!");
        }
    }

    private class LogoutUserObserver extends SimpleNotificationObserver {
        public LogoutUserObserver() {
            super(mainActivityView, "Failed to logout: ", "Failed to logout because of exception: ");
        }

        @Override
        public void handleSuccess() {
            //Clear user data (cached data).
            Cache.getInstance().clearCache();
            mainActivityView.logoutUser();
        }
    }

    private class FollowUserObserver extends FollowUserStatusObserver {
        public FollowUserObserver() {
            super(mainActivityView, "Failed to follow: ", "Failed to follow because of exception: ", false);
        }
    }

    private class UnfollowUserObserver extends FollowUserStatusObserver {
        public UnfollowUserObserver() {
            super(mainActivityView, "Failed to unfollow: ", "Failed to unfollow because of exception: ", true);
        }
    }

    private class IsFollowerUserObserver extends IsFollowerNotificationObserver {
        protected IsFollowerUserObserver() {
            super(mainActivityView, "Failed to determine following relationship: ", "Failed to determine following relationship because of exception: ");
        }
    }

    private class GetFollowersCountObserver extends CountNotificationObserver {

        public GetFollowersCountObserver() {
            super(mainActivityView, "Failed to get followers count: ", "Failed to get followers count because of exception: ");
        }

        @Override
        public void handleSuccess(int count) {
            mainActivityView.setFollowersCount(count);
        }
    }

    private class GetFollowingCountObserver extends CountNotificationObserver {
        public GetFollowingCountObserver() {
            super(mainActivityView, "Failed to get following count: ", "Failed to get following count because of exception: ");
        }

        @Override
        public void handleSuccess(int count) {
            mainActivityView.setFollowingCount(count);
        }
    }
}
