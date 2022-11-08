package edu.byu.cs.tweeter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class AuthenticateTest {
    private ServerFacade serverFacade;

    @Test
    public void testRegisterSuccess() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("FirstName", "LastName", "@Alias", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        RegisterResponse response = getServerFacade().register(request, "/register");

        assertTrue(response.isSuccess());
        assertNotNull(response.getUser());
        assertNotNull(response.getAuthToken());

        // Verify that the user has the expected values, but because the server end is still using dummy data it will always return the same user which is Allen Anderson
        assertEquals("Allen", response.getUser().getFirstName());
        assertEquals("Anderson", response.getUser().getLastName());
        assertEquals("@allen", response.getUser().getAlias());
        assertEquals("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png", response.getUser().getImageUrl());
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
