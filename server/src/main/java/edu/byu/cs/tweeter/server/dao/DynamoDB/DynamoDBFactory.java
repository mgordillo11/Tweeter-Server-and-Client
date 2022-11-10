package edu.byu.cs.tweeter.server.dao.DynamoDB;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IImageDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IStoryDAO;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IUserDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.S3.S3DAO;

public class DynamoDBFactory implements DAOFactory {
    @Override
    public IUserDAO getUserDAO() {
        return new DynamoDBUserDAO();
    }

    @Override
    public IAuthtokenDAO getAuthtokenDAO() {
        return new DynamoDBAuthtokenDAO();
    }

    @Override
    public IFollowDAO getFollowDAO() {
        return new DynamoDBFollowDAO();
    }

    @Override
    public IFeedDAO getFeedDAO() {
        return new DynamoDBFeedDAO();
    }

    @Override
    public IStoryDAO getStoryDAO() {
        return new DynamoDBStoryDAO();
    }

    @Override
    public IImageDAO getImageDAO() {
        return new S3DAO();
    }
}
