package edu.byu.cs.tweeter.server.security;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBUser;

/**
 * This class is to help set attributes for a user pool
 * and create an instance of CognitoUserPool
 */
public class AppHelper {
    /**
     * Add your pool id here
     */
    //TODO: Set your pool id here (find this by going under general settings in your user pool)
    private static final String userPoolId = "us-east-1_GZOWz47cR";

    /**
     * Add your app id here
     */
    //TODO: Set your client id here (find this by going under app clients in your user pool)
    private static final String clientId = "s9t6gsicnjsgotdvct2lg7cfh";

    /**
     * Add your client secret here
     */
    //TODO: Set your client secret here (find this by going under app clients and INTO show Details in your user pool)
    private static final String clientSecret = "ulkcr0por9div08kbuiah2s0knluh3i0pmse33ldo08t62tbplc";

    public static AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
        AWSCredentials credentials =
                new BasicAWSCredentials("AKIAZXYWUYVKBBT5UDZA", "E6RmC4FN7j2hkCBdnllAbrLJqIuiF72y8AcYWljd");

        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public static SignUpResult signUp(DynamoDBUser signUpRequest) {
        AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

        SignUpRequest signUp = new SignUpRequest();
        signUp.setClientId(clientId);
        signUp.setUsername(signUpRequest.getAlias());
        signUp.setPassword(signUpRequest.getPassword());

        Collection<AttributeType> userAttributes = new ArrayList<AttributeType>();
        userAttributes.add(new AttributeType().withName("custom:firstName").withValue(signUpRequest.getFirstName()));
        userAttributes.add(new AttributeType().withName("custom:lastName").withValue(signUpRequest.getLastName()));
        userAttributes.add(new AttributeType().withName("picture").withValue(signUpRequest.getImageUrl()));

        signUp.setUserAttributes(userAttributes);

        return cognitoClient.signUp(signUp);
    }

    public static Map<String, String> login(String alias, String password) {
        AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

        Map<String, String> authParams = new LinkedHashMap<>() {{
            put("USERNAME", alias);
            put("PASSWORD", password);
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPoolId)
                .withClientId(clientId)
                .withAuthParameters(authParams);

        AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();

        return new LinkedHashMap<>() {{
            put("idToken", resultType.getIdToken());
            put("accessToken", resultType.getAccessToken());
            put("refreshToken", resultType.getRefreshToken());
            put("message", "Successfully login");
        }};
    }
}
