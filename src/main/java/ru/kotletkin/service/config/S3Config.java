package ru.kotletkin.service.config;

import ru.kotletkin.config.AppConfig;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

public class S3Config {

    public static S3Client getS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(AppConfig.S3_ACCESS_KEY, AppConfig.S3_SECRET_KEY);
        return S3Client.builder()
                .region(Region.US_WEST_2)
                .endpointOverride(URI.create(AppConfig.S3_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
