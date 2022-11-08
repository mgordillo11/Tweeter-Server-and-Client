package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public interface IAuthtokenDAO {
    AuthToken createAuthToken(String username);

    LogoutResponse logout(AuthToken authToken);

    boolean isValidAuthToken(AuthToken authToken);
}
