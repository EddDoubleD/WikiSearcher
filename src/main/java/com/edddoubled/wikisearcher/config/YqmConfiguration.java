package com.edddoubled.wikisearcher.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.edddoubled.wikisearcher.service.MessageSender;
import com.edddoubled.wikisearcher.service.index.Indexer;
import com.edddoubled.wikisearcher.service.index.job.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class YqmConfiguration {


    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(@Value("${ymq.credentials.access-key}") String accessKey,
                                                         @Value("${ymq.credentials.secret-key}") String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Bean
    public AwsCredentialsProvider awsAsyncCredentialsProvider(@Value("${ymq.credentials.access-key}") String accessKey,
                                                              @Value("${ymq.credentials.secret-key}") String secretKey) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    @Bean
    public AmazonSQSAsync asyncYandexMessageQueue(AWSCredentialsProvider awsCredentialsProvider,
                                                  @Value("${ymq.queue.region}") String region,
                                                  @Value("${ymq.queue.endpoint}") String endpoint) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                //.withRegion(region)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        endpoint,
                        region
                ))
                .build();
    }

    @Bean
    public MessageSender messageSender(@Value("${ymq.queue.url}") String url,
                                       AmazonSQSAsync sqsAsync) {
        return new MessageSender(url, sqsAsync);
    }

    @Bean
    public MessageHandler messageHandler(
            @Value("${ymq.queue.url}") String url,
            AmazonSQSAsync sqsAsync,
            Indexer indexer
    ) {
        return new MessageHandler(url, sqsAsync, indexer);
    }
}
