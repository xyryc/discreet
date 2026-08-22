package com.discreet.backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class StorageService {
    private final S3Client s3Client;

    @Value("${app.s3.bucket-name:discreet-storage}")
    private String bucketName;

    @Value("${app.s3.public-url-prefix:}")
    private String publicUrlPrefix;

    @Value("${app.s3.endpoint:}")
    private String endpoint;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/webp", "image/gif");

    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // upload an avatar on media file to s3
    public String uploadAvatar(MultipartFile file, String userId) {
        // 1. validation: check if file is empty
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Cannot upload empty file.");
        }

        // 2. validation: check MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Invalid file type. Only JPEG, PNG, WEBP and GIF are allowed.");
        }

        // 3. extract file extension (eg. .jpg, .png)
        String originalFilename = file.getOriginalFilename();
        String extension = ".jpg";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 4. generate unique key eg. avatars/usr_f4223f46_a1b2.jpg
        String randomHex = UUID.randomUUID().toString().substring(0, 4);
        String fileKey = "avatars/" + userId + "_" + randomHex + extension;

        try {
            // 5. upload bytes to s3
            PutObjectRequest putObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()));

            // 6. return public image URL
            if (publicUrlPrefix != null && !publicUrlPrefix.trim().isEmpty()) {
                return publicUrlPrefix.replaceAll("/+$", "") + "/" + fileKey;
            } else if (endpoint != null && !endpoint.trim().isEmpty()) {
                return endpoint.replaceAll("/+$", "") + "/" + bucketName + "/" + fileKey;
            } else {
                return "https://" + bucketName + ".s3.amazonaws.com/" + fileKey;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read or upload file: " + e.getMessage());
        }

    }

}
