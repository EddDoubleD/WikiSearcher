package com.edddoubled.wikisearcher.service.index.job;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.edddoubled.wikisearcher.model.LightweightWikiPage;
import com.edddoubled.wikisearcher.service.index.Indexer;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageHandler {
    private static final Gson GSON = new Gson();
    String queueUrl;
    AmazonSQSAsync sqsAsync;
    Indexer indexer;

    public void handleMessage() throws ExecutionException, InterruptedException {
        ReceiveMessageRequest receiveMessageRequest = batchRequest();
        Future<ReceiveMessageResult> receiveMessageResult = sqsAsync.receiveMessageAsync(receiveMessageRequest);
        receiveMessageResult.get().getMessages().forEach(message -> {
            log.info("handle message {}, text {}", message.getMessageId(), message.getBody());
            indexer.index(Collections.singletonList(GSON.fromJson(message.getBody(), LightweightWikiPage.class)));
            sqsAsync.deleteMessage(queueUrl, message.getReceiptHandle());
            log.info("message {} removed from queue", message.getMessageId());
        });
    }

    private ReceiveMessageRequest batchRequest() {
        ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setQueueUrl(queueUrl);
        request.setMaxNumberOfMessages(5);
        return request;
    }
}
