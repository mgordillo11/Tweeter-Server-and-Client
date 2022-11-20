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

    private final DynamoDbIndex<DynamoDBFollows> followsIndex
            = followsTable.index(indexName);

    @Override
    public Pair<List<User>, Boolean> getFollowees(FollowingRequest request) {
        List<DynamoDBFollows> followees = getDynamoFollowees(request);
        List<User> followeesUsers = followeesToUsers(followees);

        if (followees.size() < request.getLimit()) {
            return new Pair<>(followeesUsers, false);
        }

        //List<User> followeesUsers = followsToUsers(followees);
        return new Pair<>(followeesUsers, true);
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(FollowersRequest request) {
        List<DynamoDBFollows> followers = getDynamoFollowers(request);
        List<User> followersUsers = followersToUsers(followers);

        if (followers.size() < request.getLimit()) {
            return new Pair<>(followersUsers, false);
        }

        //List<User> followersUsers = followsToUsers(followers);
        return new Pair<>(followersUsers, true);
    }

    @Override
    public FollowResponse follow(String followerAlias, String followeeAlias) {
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();


        DynamoDBFollows userToFollow = followsTable.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        if (userToFollow != null) {
            return new FollowResponse("Already following");
        }

        DynamoDBFollows follow = new DynamoDBFollows(followerAlias, followeeAlias);

        try {
            followsTable.putItem(follow);
        } catch (Exception e) {
            return new FollowResponse("Failed to follow");
        }

        return new FollowResponse();
    }

    @Override
    public UnfollowResponse unfollow(String followerAlias, String followeAlias) {
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeAlias)
                .build();

        DynamoDBFollows userToUnfollow = followsTable.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        if (userToUnfollow == null) {
            return new UnfollowResponse("User not found");
        }

        try {
            followsTable.deleteItem(userToUnfollow);
        } catch (Exception e) {
            return new UnfollowResponse("Failed to unfollow");
        }

        return new UnfollowResponse();
    }

    @Override
    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        Key key = Key.builder().partitionValue(request.getFollower().getAlias())
                .sortValue(request.getFollowee().getAlias()).build();

        DynamoDBFollows follows = followsTable.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));

        if (follows == null) {
            System.err.println("Not following");
            return new IsFollowerResponse(false);
        } else {
            System.err.println("Following");
            return new IsFollowerResponse(true);
        }
    }

    @Override
    public int getFollowingCount(String userAlias) {
        Key key = Key.builder().partitionValue(userAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();

        return (int) followsTable.query(queryEnhancedRequest)
                .items()
                .stream().count();
    }

    @Override
    public int getFollowersCount(String userAlias) {
//        Key key = Key.builder().partitionValue(userAlias).build();
//
//        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key));
//
//        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();
//
//        SdkIterable<Page<DynamoDBFollows>> results = followsIndex.query(queryEnhancedRequest);
//        PageIterable<DynamoDBFollows> pages = PageIterable.create(results);
//
//        List<DynamoDBFollows> followers = new ArrayList<>();;
//
//        pages.stream()
//                .limit(1)
//                .forEach(visitsPage -> followers.addAll(visitsPage.items()));
//
//        return followers.size();
        return getFollowersAlias(userAlias).size();
    }

    @Override
    public List<String> getFollowersAlias(String userAlias) {
        Key key = Key.builder().partitionValue(userAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();

        List<DynamoDBFollows> followers = new ArrayList<>();

        SdkIterable<Page<DynamoDBFollows>> results = followsIndex.query(queryEnhancedRequest);
        PageIterable<DynamoDBFollows> pages = PageIterable.create(results);

        pages.stream()
                .limit(1)
                .forEach(visitsPage -> followers.addAll(visitsPage.items()));

        List<String> followersAlias = new ArrayList<>();
        for (DynamoDBFollows follower : followers) {
            followersAlias.add(follower.getFollower_handle());
        }

        return followersAlias;
    }

    public List<DynamoDBFollows> getDynamoFollowers(FollowersRequest request) {
        int limit = request.getLimit();

        Key key = Key.builder().partitionValue(request.getFollowerAlias()).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);
        //.scanIndexForward(false);

        if (request.getLastFollowerAlias() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put(followeeAttr, AttributeValue.builder().s(request.getFollowerAlias()).build());
            startKey.put(followerAttr, AttributeValue.builder().s(request.getLastFollowerAlias()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();

        List<DynamoDBFollows> follows = new ArrayList<>();

        SdkIterable<Page<DynamoDBFollows>> results = followsIndex.query(queryEnhancedRequest);
        PageIterable<DynamoDBFollows> pages = PageIterable.create(results);

        // limit 1 page, with pageSize items
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> follows.addAll(visitsPage.items()));

        System.err.println("Follows Size: " + follows.size());

        return follows;
    }

    public List<DynamoDBFollows> getDynamoFollowees(FollowingRequest request) {
        int limit = request.getLimit();

        Key key = Key.builder()
                .partitionValue(request.getFollowerAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(true);

        if (request.getLastFolloweeAlias() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();

            startKey.put(followerAttr, AttributeValue.builder().s(request.getFollowerAlias()).build());
            startKey.put(followeeAttr, AttributeValue.builder().s(request.getLastFolloweeAlias()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        return followsTable.query(queryRequest)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<User> followeesToUsers(List<DynamoDBFollows> followees) {
        List<User> followeesUsers = new ArrayList<>();

        for (DynamoDBFollows followee : followees) {
            followeesUsers.add(userDAO.getUser(followee.getFollowee_handle()));
        }

        return followeesUsers;
    }

    public List<User> followersToUsers(List<DynamoDBFollows> followees) {
        List<User> followeesUsers = new ArrayList<>();

        for (DynamoDBFollows followee : followees) {
            followeesUsers.add(userDAO.getUser(followee.getFollower_handle()));
        }

        return followeesUsers;
    }
}
