package subscribenlike.mogupick.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public S3Service(S3Client s3Client,
                     @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
                     @Value("${spring.cloud.aws.region.static}") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }
}

