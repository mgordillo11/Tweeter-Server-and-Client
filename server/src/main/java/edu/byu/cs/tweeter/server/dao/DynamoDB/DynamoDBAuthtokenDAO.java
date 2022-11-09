package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.sql.Timestamp;
import java.time.Period;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBAuthtoken;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

public class DynamoDBAuthtokenDAO extends DynamoDBMainDAO implements IAuthtokenDAO {
    private final String tableName = "authtoken";
    private final DynamoDbTable<DynamoDBAuthtoken> table = enhancedClient.table(tableName, TableSchema.fromBean(DynamoDBAuthtoken.class));

    @Override
    public Authtoken createAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        String timeStamp = new Timestamp(System.currentTimeMillis()).toString();

        DynamoDBAuthtoken dynamoDBAuthtoken = new DynamoDBAuthtoken(token, timeStamp, username);

        try {
            table.putItem(dynamoDBAuthtoken);
        } catch (Exception e) {
            System.err.println("Unable to create auth token");
            System.err.println(e.getMessage());
            return null;
        }

        return dynamoAuthToAuth(dynamoDBAuthtoken);
    }

    @Override
    public LogoutResponse logout(Authtoken authToken) {
        try {
            Key key = Key.builder().partitionValue(authToken.getToken()).build();
            table.deleteItem(key);
        } catch (Exception e) {
            System.err.println("Unable to delete auth token");
            System.err.println(e.getMessage());
            return new LogoutResponse(e.getMessage());
        }

        return new LogoutResponse();
    }

    @Override
    public boolean isValidAuthToken(Authtoken authToken) {
        try {
            DynamoDBAuthtoken token = getAuthToken(authToken);

            // Validate if authToken is active (24 hours or less old)
            Period period = Period.between(Timestamp.valueOf(token.getDatetime()).toLocalDateTime().toLocalDate()
                    , new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate());

            return period.getDays() < 1;
        } catch (Exception e) {
            System.err.println("Unable to get auth token");
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public String getAliasFromAuthToken(Authtoken authtoken) {
        return getAuthToken(authtoken).getAlias();
    }

    public DynamoDBAuthtoken getAuthToken(Authtoken authToken) {
        Key key = Key.builder().partitionValue(authToken.getToken()).build();

        return table.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
    }

    public Authtoken dynamoAuthToAuth(DynamoDBAuthtoken dynamoAuth) {
        return new Authtoken(dynamoAuth.getToken(), dynamoAuth.getDatetime());
    }
}
