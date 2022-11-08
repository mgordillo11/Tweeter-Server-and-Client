package edu.byu.cs.tweeter.server.dao.DynamoDB;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IUserDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;


public class DynamoDBUserDAO extends DynamoDBMainDAO implements IUserDAO {
    private static final String TABLE_NAME = "users";
    private final DynamoDbTable<DynamoDBUser> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoDBUser.class));

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public void register(String username, String password, String firstName, String lastName, String imageURL) {

    }

    @Override
    public User getUser(String username) {
        DynamoDBUser user = getCompleteUser(username);
        return new User(user.getAlias(), user.getFirstName(), user.getLastName(), user.getImageUrl());
    }


    public DynamoDBUser getCompleteUser(String username) {
        Key key = Key.builder().partitionValue(username).build();

        return table.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
    }

    @Override
    public String getHashedPassword(String username) {
        return getCompleteUser(username).getPassword();
    }
}
