package com.edddoubled.wikisearcher.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageSender {

    String queueUrl;
    AmazonSQSAsync sqsAsync;

    public void sendMessage(String messageBody) throws ExecutionException, InterruptedException {
        Future<SendMessageResult> future = sqsAsync.sendMessageAsync(queueUrl, messageBody);
        SendMessageResult sendMessageResult = future.get();
        log.info("Message {} success send", sendMessageResult.getMessageId());
    }


}
