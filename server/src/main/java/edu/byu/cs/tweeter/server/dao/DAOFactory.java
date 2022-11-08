package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IImageDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IStoryDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IUserDAO;

public interface DAOFactory {
    IUserDAO getUserDAO();

    IAuthtokenDAO getAuthtokenDAO();

    IFollowDAO getFollowDAO();

    IFeedDAO getFeedDAO();

    IStoryDAO getStoryDAO();

    IImageDAO getImageDAO();
}
