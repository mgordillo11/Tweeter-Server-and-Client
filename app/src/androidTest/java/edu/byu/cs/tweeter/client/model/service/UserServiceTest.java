package edu.byu.cs.tweeter.client.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class UserServiceTest {
    private User currentUser;
    private Authtoken currentAuthToken;

    private Status lastStatus;
    private int limit;
    private List<String> urls;
    private List<String> mentions;

    private MainPresenter presenter;
    private MainPresenter.PostStatusObserver observer;
    private MainActivity mainActivity;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        currentUser = new User("Manuel", "Gordillo", "@ryth", "https://manuel-gordillo-tweeter-bucket.s3.amazonaws.com/%40ryth");
        currentAuthToken = new Authtoken();

        limit = 10;
        urls = new ArrayList<>();
        mentions = new ArrayList<>();
        lastStatus = null;

        mainActivity = Mockito.mock(MainActivity.class);
        presenter = Mockito.spy(new MainPresenter(mainActivity));
        observer = Mockito.spy(presenter.new PostStatusObserver());

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void test_Login_PostStatus_And_GetStory_Successful() throws IOException, TweeterRemoteException, InterruptedException {
        LoginRequest loginRequest = new LoginRequest("@ryth", "1");
        LoginResponse loginResponse = getServerFacade().login(loginRequest, "/login");

        currentAuthToken = loginResponse.getAuthToken();
        currentUser = loginResponse.getUser();

        assertTrue(loginResponse.isSuccess());
        assertNotNull(currentUser);
        assertNotNull(currentAuthToken);
        assertEquals(loginRequest.getUsername(), loginResponse.getUser().getAlias());

        Cache.getInstance().setCurrUser(currentUser);
        Cache.getInstance().setCurrUserAuthToken(currentAuthToken);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = String.valueOf(timestamp.getTime());

        Mockito.when(presenter.getFormattedDateTime()).thenReturn(time);
        Mockito.when(presenter.getPostStatusObserver()).thenReturn(observer);

        Answer<Void> answer = invocation -> {
            countDownLatch.countDown();
            return null;
        };

        Mockito.doAnswer(answer).when(mainActivity).displayMessage("Successfully Posted!");

        presenter.statusPost("This is a test status 7");
        awaitCountDownLatch();

        Mockito.verify(mainActivity, Mockito.times(1)).displayMessage("Successfully Posted!");
        Mockito.verify(observer, Mockito.times(1)).handleSuccess();

        String statusTime = new Timestamp(Long.parseLong(time)).toString();
        Status testStatus = new Status("This is a test status 7", currentUser, statusTime, urls, mentions);

        StoryRequest storyRequest = new StoryRequest(currentAuthToken, currentUser.getAlias(), limit, lastStatus);
        StoryResponse storyResponse = getServerFacade().getStory(storyRequest, "/getstory");

        assertTrue(storyResponse.isSuccess());
        assertNotNull(storyResponse.getStatuses());
        assertTrue(storyResponse.getStatuses().size() > 0);

        List<Status> statuses = storyResponse.getStatuses();
        Status latestStatus = statuses.get(0); // Recent status should be at the top of the list

        assertEquals(testStatus.getPost(), latestStatus.getPost());
        assertEquals(testStatus.getUser(), latestStatus.getUser());
        assertEquals(testStatus.getUrls(), latestStatus.getUrls());
        assertEquals(testStatus.getMentions(), latestStatus.getMentions());
        // assertEquals(parseTime, timestamp2); We can't compare the timestamps because
        // they are different because of the time zone of where the computer is located when the lambda is called
    }

    private ServerFacade getServerFacade() {
        return ServerFacade.getInstance();
    }
}
