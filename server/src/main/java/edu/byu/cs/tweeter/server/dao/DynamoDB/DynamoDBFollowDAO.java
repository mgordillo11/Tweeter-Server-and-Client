package edu.byu.cs.tweeter.server.dao.DynamoDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBFollows;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoDBFollowDAO extends DynamoDBMainDAO implements IFollowDAO {
    private final String tableName = "follows";
    private final String indexName = "follows_index";

    private static final String followerAttr = "follower_handle";
    private static final String followeeAttr = "followee_handle";

    private final DynamoDBUserDAO userDAO = new DynamoDBUserDAO();

    private final DynamoDbTable<DynamoDBFollows> followsTable
            = enhancedClient.table(tableName, TableSchema.fromBean(DynamoDBFollows.class));

    private final DynamoDbIndex<DynamoDBFollows> index
            = enhancedClient.table(tableName, TableSchema.fromBean(DynamoDBFollows.class)).index(indexName);

    private String lastFollowee = null;
    private String lastFollower = null;

    @Override
    public Pair<List<User>, Boolean> getFollowees(FollowingRequest request) {
        List<DynamoDBFollows> followees = getFolloweesAlias(request);

        if (followees == null || followees.size() == 0) {
            return new Pair<>(null, false);
        }

        lastFollowee = followees.get(followees.size() - 1).getFollowee_handle();
        List<User> followeesUsers = followsToUser(followees);

        return new Pair<>(followeesUsers, true);
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(FollowersRequest request) {
        List<DynamoDBFollows> followers = getFollowersAlias(request);

        if (followers == null || followers.size() == 0) {
            return new Pair<>(null, false);
        }

        lastFollower = followers.get(followers.size() - 1).getFollower_handle();
        List<User> followersUsers = followsToUser(followers);

        return new Pair<>(followersUsers, true);
    }

    @Override
    public FollowResponse follow(String follower, String followee) {
        Key key = Key.builder()
                .partitionValue(follower)
                .sortValue(followee)
                .build();


        DynamoDBFollows userToFollow = followsTable.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        if (userToFollow != null) {
            return new FollowResponse("Already following");
        }

        //followsTable.putItem(key);
        return new FollowResponse();
    }

    @Override
    public UnfollowResponse unfollow(String follower, String followee) {
        Key key = Key.builder()
                .partitionValue(follower)
                .sortValue(followee)
                .build();


        DynamoDBFollows userToUnfollow = followsTable.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        if (userToUnfollow == null) {
            return new UnfollowResponse("User not found");
        }

        followsTable.deleteItem(userToUnfollow);
        return new UnfollowResponse();
    }

    @Override
    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        return null;
    }

    public List<DynamoDBFollows> getFollowersAlias(FollowersRequest request) {
        int limit = request.getLimit();
        Key key = Key.builder().partitionValue(request.getLastFollowerAlias()).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(false);

        if (isNonEmptyString(lastFollower)) {
            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put(followerAttr, AttributeValue.builder().s(lastFollower).build());
            startKey.put(followeeAttr, AttributeValue.builder().s(request.getLastFollowerAlias()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();

        List<DynamoDBFollows> follows = new ArrayList<>();

        SdkIterable<Page<DynamoDBFollows>> results = index.query(queryEnhancedRequest);
        PageIterable<DynamoDBFollows> pages = PageIterable.create(results);

        // limit 1 page, with pageSize items
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> follows.addAll(visitsPage.items()));

        return follows;
    }

    public List<DynamoDBFollows> getFolloweesAlias(FollowingRequest request) {
        int limit = request.getLimit();

        Key key = Key.builder()
                .partitionValue(request.getFollowerAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(true);

        if (isNonEmptyString(lastFollowee)) {
            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put(followerAttr, AttributeValue.builder().s(request.getFollowerAlias()).build());
            startKey.put(followeeAttr, AttributeValue.builder().s(lastFollowee).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        return followsTable.query(queryRequest)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<User> followsToUser(List<DynamoDBFollows> followees) {
        List<User> followeesUsers = new ArrayList<>();
        for (DynamoDBFollows followee : followees) {
            followeesUsers.add(userDAO.getUser(followee.getFollowee_handle()));
        }

        return followeesUsers;
    }

    private boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
