package edu.byu.cs.tweeter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterUnitTest {
    private MainPresenter.MainView mockMainView;
    private StatusService mockStatusService;

    private MainPresenter mainPresenterSpy;


    @BeforeEach
    public void setup() {
        // Create mock objects
        mockMainView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockMainView));

        Cache.getInstance().setCurrUserAuthToken(new AuthToken("token"));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
    }

    abstract static class AnswerUse implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) {

            assertTrue(invocation.getArgument(0) instanceof AuthToken);
            assertTrue(invocation.getArgument(1) instanceof User);
            assertTrue(invocation.getArgument(2) instanceof String);
            assertTrue(invocation.getArgument(3) instanceof String);

            List<String> urls = invocation.getArgument(4);
            List<String> mentions = invocation.getArgument(5);

            // The reason why their size is zero is because no URLS or mentions were found when parsed by sending a post that said "Hello World"
            assertEquals(urls.size(), 0);
            assertEquals(mentions.size(), 0);

            assertTrue(invocation.getArgument(6) instanceof SimpleNotificationObserver);

            SimpleNotificationObserver observer = invocation.getArgument(6, SimpleNotificationObserver.class);
            handleOperation(observer);
            return null;
        }

        public abstract void handleOperation(SimpleNotificationObserver observer);
    }

    public static class TestAnswer extends AnswerUse {
        @Override
        public void handleOperation(SimpleNotificationObserver observer) {
            observer.handleSuccess();
        }
    }

    public static class FailAnswer extends AnswerUse {
        @Override
        public void handleOperation(SimpleNotificationObserver observer) {
            observer.handleFailure("Fail Happened, Bad Day!");
        }
    }

    public static class ExceptionAnswer extends AnswerUse {
        @Override
        public void handleOperation(SimpleNotificationObserver observer) {
            observer.handleException(new Exception("Exception Happened, Bad Day!"));
        }
    }

    @Test
    public void testPostStatus_postStatusSuccessful() {
        runDoAnswerWhen(new TestAnswer());
        postStatus();

        Mockito.verify(mockMainView).showPostingToast(false);
        verifyMessage("Successfully Posted!");
    }

    @Test
    public void testPostStatus_postStatusFailed() {
        runDoAnswerWhen(new FailAnswer());
        postStatus();
        verifyMessage("Failed to post status: " + "Fail Happened, Bad Day!");
    }

    @Test
    public void testPostStatus_postStatusFailedWithException() {
        runDoAnswerWhen(new ExceptionAnswer());
        postStatus();
        verifyMessage("Failed to post status because of exception: " + "Exception Happened, Bad Day!");
    }

    private void runDoAnswerWhen(AnswerUse answer) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(AuthToken.class),
                Mockito.any(User.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.any(SimpleNotificationObserver.class));
    }

    private void postStatus() {
        mainPresenterSpy.statusPost("Hello World");
    }

    private void verifyMessage(String message) {
        Mockito.verify(mockMainView).displayMessage(message);
    }
}
