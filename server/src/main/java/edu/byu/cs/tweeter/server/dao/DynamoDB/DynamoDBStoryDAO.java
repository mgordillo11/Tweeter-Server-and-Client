package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IStoryDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoDBStoryDAO extends DynamoDBMainDAO implements IStoryDAO {
    private static final String TABLE_NAME = "story";
    private final DynamoDbTable<DynamoDBStatus> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoDBStatus.class));

    private final DynamoDBUserDAO userDAO = new DynamoDBUserDAO();

    private static Long lastTimestamp = null;

    @Override
    public StoryResponse getStory(StoryRequest request) {
        int limit = request.getLimit();
        Long currentTime = new Timestamp(System.currentTimeMillis()).getTime();

        Key key = Key.builder()
                .partitionValue(request.getAlias())
                .sortValue(currentTime)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        if (isNonEmptyLong(lastTimestamp)) {
            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put("alias", AttributeValue.builder().s(request.getAlias()).build());
            startKey.put("datetime", AttributeValue.builder().n(lastTimestamp.toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        List<DynamoDBStatus> results = table.query(queryRequest)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());

        if (results.size() == 0) {
            return new StoryResponse(new ArrayList<>(), false);
        }

        lastTimestamp = results.get(results.size() - 1).getDate();

        return new StoryResponse(dynamoStatusesToStatuses(results), true);
    }

    @Override
    public boolean postStatus(Status status) {
        Long datetime = Timestamp.valueOf(status.getDate()).getTime();

        try {
            DynamoDBStatus dynamoDBStatus = new DynamoDBStatus(status.getPost(), status.getUser().getAlias(), datetime, status.getUrls(), status.getMentions());
            table.putItem(dynamoDBStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Status> dynamoStatusesToStatuses(List<DynamoDBStatus> dynamoStatuses) {
        List<Status> statuses = new ArrayList<>();

        // Don't know if this is the proper way to convert back time to string
        for (DynamoDBStatus currentStatus : dynamoStatuses) {
            User user = userDAO.getUser(currentStatus.getUserAlias());
            String statusTime = new Timestamp(currentStatus.getDate()).toString();

            Status status = new Status(currentStatus.getPost(), user, statusTime, currentStatus.getUrls(), currentStatus.getMentions());
            statuses.add(status);
        }

        return statuses;
    }

    private boolean isNonEmptyLong(Long value) {
        return (value != null) && (value > 0);
    }
}
