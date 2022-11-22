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
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;

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

    @Override
    public void addFeedBatch(Status status, List<String> followers) {
        Long convertedTime = Long.valueOf(status.getDate());

        DynamoDBStatus dynamoDBStatus = new DynamoDBStatus(status.getPost(),
                status.getUser().getAlias(), convertedTime, status.getUrls(), status.getMentions());

        List<WriteBatch> writeBatches = new ArrayList<>();

        for (String follower : followers) {
            DynamoDBFeed addedFeed = new DynamoDBFeed(follower, convertedTime, dynamoDBStatus);

            WriteBatch currWriteBatch = WriteBatch.builder(DynamoDBFeed.class)
                    .mappedTableResource(table)
                    .addPutItem(addedFeed)
                    .build();

            writeBatches.add(currWriteBatch);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (writeBatches.size() == 25) {
                BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                        .writeBatches(writeBatches)
                        .build();

                loopBatchWrite(batchWriteItemEnhancedRequest);
                writeBatches.clear();
            }
        }

        // Left over items, that didn't make a full batch
        if (writeBatches.size() > 0) {
            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                    .writeBatches(writeBatches)
                    .build();

            loopBatchWrite(batchWriteItemEnhancedRequest);
        }
    }

    private void loopBatchWrite(BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest) {
        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            System.out.println("Batch write succeeded: " + result);
        } catch (Exception e) {
            System.err.println("Unable to add batch of feeds");
            throw new RuntimeException(e);
        }
    }
}
