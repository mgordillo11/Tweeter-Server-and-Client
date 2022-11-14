package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBFeed;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoDBFeedDAO extends DynamoDBMainDAO implements IFeedDAO {
    private static final String TABLE_NAME = "feed";
    private final DynamoDbTable<DynamoDBFeed> table =
            enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoDBFeed.class));

    private final DynamoDBStoryDAO storyDAO = new DynamoDBStoryDAO();

    private static final String aliasAttr = "receiver_alias";
    private static final String datetimeAttr = "datetime";

    @Override
    public FeedResponse getFeed(FeedRequest request) {
        int limit = request.getLimit();

        Key key = Key.builder()
                .partitionValue(request.getAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        if (request.getLastStatus() != null) {
            // convert timestamp to long
            String timestamp = request.getLastStatus().getDate();
            long convertedTime = Timestamp.valueOf(timestamp).getTime();

            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put(aliasAttr, AttributeValue.builder().s(request.getAlias()).build());
            startKey.put(datetimeAttr, AttributeValue.builder().n(Long.toString(convertedTime)).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        List<DynamoDBFeed> results;

        results = table.query(queryRequest)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());


        if (results.size() == 0) {
            return new FeedResponse(new ArrayList<>(), false);
        }

        List<DynamoDBStatus> dynamoDBStatuses = new ArrayList<>();
        for (DynamoDBFeed feed : results) {
            dynamoDBStatuses.add(feed.getStatus());
        }

        List<Status> statuses = storyDAO.dynamoStatusesToStatuses(dynamoDBStatuses);

        return new FeedResponse(statuses, true);
    }

    @Override
    public void updateFeed(String alias, PostStatusRequest request) {
        Status status = request.getStatus();
        Long convertedTime = Long.valueOf(status.getDate());

        DynamoDBStatus dynamoDBStatus = new DynamoDBStatus(status.getPost(),
                status.getUser().getAlias(), convertedTime, status.getUrls(), status.getMentions());

        DynamoDBFeed addedFeed = new DynamoDBFeed(alias, convertedTime, dynamoDBStatus);

        try {
            table.putItem(addedFeed);
        } catch (Exception e) {
            System.err.println("Unable to add feed item: " + alias + " " + convertedTime);
            throw new RuntimeException(e);
        }
    }
}
