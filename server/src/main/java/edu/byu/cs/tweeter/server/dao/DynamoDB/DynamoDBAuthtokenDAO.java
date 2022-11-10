package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

        System.err.println("timeStamp: " + timeStamp);

        String token = UUID.randomUUID().toString();

        DynamoDBAuthtoken dynamoDBAuthtoken = new DynamoDBAuthtoken(token, timeStamp, username);

        try {
            table.putItem(dynamoDBAuthtoken);
        } catch (Exception e) {
            System.err.println("Unable to create auth token");
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
//            Period period = Period.between(Timestamp.valueOf(token.getDatetime()).toLocalDateTime().toLocalDate()
//                    , new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate());

            LocalDate start_date = LocalDate.now();
            LocalDate end_date = LocalDate.parse(token.getTimeStamp());

            // find the period between
            // the start and end date
            Period diff
                    = Period
                    .between(start_date,
                            end_date);

            return diff.getDays() >= -1;
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

    private int timeBetweenHours(Date date1, Date date2) {
        int diff = (int) (date2.getTime() - date1.getTime());
        return (diff / (1000 * 60 * 60)) % 24;
    }

    private Long getHoursBetweenDates(String datetime) {
        Long currentTime = new Timestamp(System.currentTimeMillis()).getTime();
        Long tokenTime = Timestamp.valueOf(datetime).getTime();

        return (currentTime - tokenTime) / 1000 / 60 / 60;
    }

    public DynamoDBAuthtoken getAuthToken(Authtoken authToken) {
        System.err.println(authToken.getToken());

        String token = authToken.getToken();
        Key key = Key.builder().partitionValue(token).build();

        return table.getItem(key);

//        return table.getItem(
//                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
    }

    public Authtoken dynamoAuthToAuth(DynamoDBAuthtoken dynamoAuth) {
        return new Authtoken(dynamoAuth.getToken(), dynamoAuth.getTimeStamp());
    }
}
