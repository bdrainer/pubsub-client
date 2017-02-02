package com.gearlaunch.google;

import com.google.cloud.pubsub.Message;
import com.google.cloud.pubsub.PubSub;
import com.google.cloud.pubsub.ReceivedMessage;
import com.google.cloud.pubsub.Subscription;
import com.google.cloud.pubsub.SubscriptionInfo;
import com.google.cloud.pubsub.Topic;
import com.google.cloud.pubsub.TopicInfo;
import com.google.cloud.pubsub.testing.LocalPubSubHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class PubsubClientTest {

    private static final LocalPubSubHelper PUB_SUB_HELPER = LocalPubSubHelper.create();

    private static final int MAX_MESSAGES_PER_PULL = 100;

    private static final String TOPIC_NAME = "test-topic";

    private static final String SUBSCRIPTION_NAME = "test-subscription";

    private static final String PAYLOAD = "payload";

    private PubSub pubsub;

    @BeforeClass
    public static void startEmulator() throws Exception {
        PUB_SUB_HELPER.start();
    }

    @Before
    public void setup() {
        pubsub = PUB_SUB_HELPER.getOptions().getService();
        createTopic(TOPIC_NAME);
        createSubscription(TOPIC_NAME, SUBSCRIPTION_NAME);
    }

    @After
    public void tearDown() throws Exception {
        deleteSubscription(SUBSCRIPTION_NAME);
        deleteTopic(TOPIC_NAME);
        PUB_SUB_HELPER.reset();
    }

  /* Does not work, throws exception */
//  @AfterClass
//  public static void stopEmulator() throws Exception {
//    PUB_SUB_HELPER.stop(Duration.standardSeconds(1L ));
//  }

    // ------------------------------------------------------

    @Test
    public void testPubsub() {
        final String messageId = publishMessage(TOPIC_NAME, PAYLOAD);
        assertThat(messageId).isNotNull();

        final Iterator<ReceivedMessage> messages = pullMessages(SUBSCRIPTION_NAME, MAX_MESSAGES_PER_PULL);
        assertThat(messages.hasNext()).isTrue();

        final ReceivedMessage receivedMessage = messages.next();
        assertThat(receivedMessage.getId()).isEqualTo(messageId);
        assertThat(receivedMessage.getPayloadAsString()).isEqualTo(PAYLOAD);
    }

    // ------------------------------------------------------

    private Iterator<ReceivedMessage> pullMessages(final String subscriptionName, final int maxMessagesPerPull) {
        return pubsub.pull(subscriptionName, maxMessagesPerPull);
    }

    private String publishMessage(final String topicName, String payload) {
        Message message = Message.of(payload);
        return pubsub.publish(topicName, message);
    }

    private Subscription createSubscription(final String topicName, final String subscriptionName) {
        SubscriptionInfo subscriptionInfo = SubscriptionInfo.of(topicName, subscriptionName);
        return pubsub.create(subscriptionInfo);
    }

    private boolean deleteSubscription(final String subscriptionName) {
        return pubsub.deleteSubscription(subscriptionName);
    }

    private Topic createTopic(final String topicName) {
        final TopicInfo topicInfo = TopicInfo.of(topicName);
        return pubsub.create(topicInfo);
    }

    private void deleteTopic(final String topicName) {
        pubsub.deleteTopic(topicName);
    }
}
