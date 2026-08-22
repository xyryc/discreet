package com.discreet.backend.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class S3Config {
    @Value("${app.s3.endpoint:}")
    private String endpoint;

    @Value("${app.s3.region:ap-southeast-1}")
    private String region;

    @Value("${app.s3.access-key:discreet_dev_key}")
    private String accessKey;

    @Value("${app.s3.secret-key:discreet_dev_secret}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3ClientBuilder builder = S3Client.builder().region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))

                // enable universal compatability with MinIO, Supabase, Cloudfare R2 & AWS
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());

        if (endpoint != null && !endpoint.trim().isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
