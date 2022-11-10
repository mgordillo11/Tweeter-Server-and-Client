package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import java.text.ParseException;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public interface IAuthtokenDAO {
    Authtoken createAuthToken(String username) ;

    LogoutResponse logout(Authtoken authToken);

    boolean isValidAuthToken(Authtoken authToken);

    String getAliasFromAuthToken(Authtoken authtoken);
}
