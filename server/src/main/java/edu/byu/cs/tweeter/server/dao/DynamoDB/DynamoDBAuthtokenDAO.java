package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBAuthtoken;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoDBAuthtokenDAO extends DynamoDBMainDAO implements IAuthtokenDAO {
    private final String tableName = "authtoken";
    private final DynamoDbTable<DynamoDBAuthtoken> table = enhancedClient.table(tableName, TableSchema.fromBean(DynamoDBAuthtoken.class));

    @Override
    public Authtoken createAuthToken(String username) {
        LocalDate localDate = LocalDate.now();
        String timeStamp = localDate.toString();

        String token = UUID.randomUUID().toString();
        DynamoDBAuthtoken dynamoDBAuthtoken = new DynamoDBAuthtoken(token, timeStamp, username);

        table.putItem(dynamoDBAuthtoken);

        return dynamoAuthToAuth(dynamoDBAuthtoken);
    }

    @Override
    public LogoutResponse logout(Authtoken authToken) {
        try {
            Key key = Key.builder().partitionValue(authToken.getToken()).build();
            table.deleteItem(key);
        } catch (Exception e) {
            System.err.println("Unable to delete auth token");
            return new LogoutResponse(e.getMessage());
        }

        return new LogoutResponse();
    }

    @Override
    public boolean isValidAuthToken(Authtoken authToken) {
        // Makes sure the token is less than a day old
        try {
            DynamoDBAuthtoken token = getAuthToken(authToken);

            LocalDate start_date = LocalDate.now();
            LocalDate end_date = LocalDate.parse(token.getTimeStamp());

            // find the period between
            // the start and end date
            Period diff
                    = Period
                    .between(start_date,
                            end_date);

            return diff.getDays() > -1;
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
        String token = authToken.getToken();
        Key key = Key.builder().partitionValue(token).build();

        return table.getItem(key);
    }

    public Authtoken dynamoAuthToAuth(DynamoDBAuthtoken dynamoAuth) {
        return new Authtoken(dynamoAuth.getToken(), dynamoAuth.getTimeStamp());
    }
}
