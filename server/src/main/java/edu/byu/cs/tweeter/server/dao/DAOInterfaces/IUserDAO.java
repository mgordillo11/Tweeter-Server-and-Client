package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import edu.byu.cs.tweeter.model.domain.User;

public interface IUserDAO {
    User login(String username);

    void register(String username, String password, String firstName, String lastName, String imageURL);

    User getUser(String username);

    String getHashedPassword(String username);
}
