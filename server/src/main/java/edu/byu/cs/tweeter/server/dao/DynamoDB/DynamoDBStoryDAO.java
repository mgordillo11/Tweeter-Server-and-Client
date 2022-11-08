package edu.byu.cs.tweeter.server.dao.DynamoDB;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IStoryDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoDBStoryDAO extends DynamoDBMainDAO implements IStoryDAO {
    private static final String TABLE_NAME = "story";
    private final DynamoDbTable<Status> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Status.class));


    @Override
    public StoryResponse getStory(StoryRequest request) {
        return null;
    }

    @Override
    public boolean postStatus(Status status) {
        return false;
    }

}
