package com.kmbl.eventmanagementservice.unittest;

import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.kmbl.eventmanagementservice.utils.DynamoDbUtils;
import org.junit.jupiter.api.BeforeAll;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDbSetupBase {

    /**
     * Share one common DDB Local instance across all tests. Make sure you use random data for your
     * tests to avoid collision across multiple tests.
     * <p>
     * Do NOT clear tables because they will affect other tests running in parallel. Generate your own
     * unique random data for your unit tests.
     */
    protected static final AmazonDynamoDBLocal ddbEmbedded = DynamoDBEmbedded.create();

    protected static final DynamoDbClient ddbClient = ddbEmbedded.dynamoDbClient();

    protected static final DynamoDbEnhancedClient ddb =
            DynamoDbEnhancedClient.builder().dynamoDbClient(ddbClient).build();

    @BeforeAll
    public static void setupBeforeAll() {
        DynamoDbUtils.createAllTables(ddbClient);
    }
}
