package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.security.PBKDF2WithHmacSHA1Hashing;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {
    private final DAOFactory daoFactory;
    private final PBKDF2WithHmacSHA1Hashing hashing;

    public UserService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.hashing = new PBKDF2WithHmacSHA1Hashing();
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // check if user does not exist
        boolean userExists = daoFactory.getUserDAO().getUser(request.getUsername()) != null;
        if (!userExists) {
            return new LoginResponse("User does not exist, based on the provided username");
        }

        // check if password is correct
        String hashedPassword = daoFactory.getUserDAO().getHashedPassword(request.getUsername());
        boolean passwordCorrect;

        try {
            passwordCorrect = hashing.validatePassword(request.getPassword(), hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[Server Error] Error validating password");
        }

        if (!passwordCorrect) {
            return new LoginResponse("Invalid credentials");
        }

        User user = daoFactory.getUserDAO().login(request.getUsername());
        AuthToken authToken = daoFactory.getAuthtokenDAO().createAuthToken(request.getUsername());

        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImageUrl() == null) {
            throw new RuntimeException("[Bad Request] Missing an image URL");
        }

        // check if username is already taken
        boolean userExists = daoFactory.getUserDAO().getUser(request.getUsername()) != null;
        if (userExists) {
            return new RegisterResponse("Username has already been taken");
        }

        String hashedPassword;

        // hash password, and then request password to the hashed password
        try {
            hashedPassword = hashing.generateStrongPasswordHash(request.getPassword());
            request.setPassword(hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[Server Error] Unable to hash password");
        }

        // upload image to S3, and get the URL
        String imageURL = daoFactory.getImageDAO().uploadImage(request.getImageUrl(), request.getUsername());

        // register user
        daoFactory.getUserDAO().register(request.getUsername(), request.getPassword(), request.getFirstName(), request.getLastName(), imageURL);

        // Create an auth token for the new user's current session and retrieve the user's profile
        AuthToken authtoken = daoFactory.getAuthtokenDAO().createAuthToken(request.getUsername());

        //User user = daoFactory.getUserDAO().getUser(request.getUsername());
        User user = new User(request.getFirstName(), request.getLastName(),
                          request.getUsername(), imageURL);

        /**
         * Or do  User user = new User(request.getFirstName(), request.getLastName(),
         *                 request.getUsername(), imageURL);
         *
         * Not too sure if we need to get the user from the database or not, but I think
         * it'll be faster to just create a new user object instead of getting it from the
         * database.
         */

        return new RegisterResponse(user, authtoken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an auth token");
        }

        boolean activeUser = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthToken());
        if (!activeUser) {
            return new GetUserResponse("Authtoken is invalid, and User is no longer active");
        }

        User user = daoFactory.getUserDAO().getUser(request.getAlias());
        return new GetUserResponse(user);
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] Missing an auth token");
        }

        return daoFactory.getAuthtokenDAO().logout(request.getAuthtoken());
    }
}
