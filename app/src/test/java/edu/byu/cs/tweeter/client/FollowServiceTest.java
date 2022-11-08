package edu.byu.cs.tweeter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;

public class FollowServiceTest {
    private ServerFacade serverFacade;


    @Test
    public void testGetFollowersSuccess() throws IOException, TweeterRemoteException {
        FollowersRequest request = new FollowersRequest(new AuthToken("Temp"), "@Manny", 10, "@Dallin");
        FollowersResponse response = getServerFacade().getFollowers(request, "/getfollowers");

        assertTrue(response.isSuccess());
        assertNotNull(response.getFollowers());
        assertTrue(response.getHasMorePages());
        assertEquals(10, response.getFollowers().size());
    }

    @Test
    public void testGetFollowingCount() throws IOException, TweeterRemoteException {
        GetFollowersCountRequest request = new GetFollowersCountRequest(new AuthToken("Temp"),
                new User("Manny", "Manny", "@Manny", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
        GetFollowersCountResponse response = getServerFacade().getFollowersCount(request, "/getfollowerscount");

        assertTrue(response.isSuccess());
        assertEquals(20, response.getCount());
    }


    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return new ServerFacade();
    }
}
