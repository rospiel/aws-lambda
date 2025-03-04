package com.validatorfile.service.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.validatorfile.service.S3DeleteObjectUseCase;

import static java.lang.String.format;

public class S3DeleteObjectUseCaseImpl implements S3DeleteObjectUseCase {

    @Override
    public void deleteObject(Context context, LambdaLogger logger, S3Event event, String fileName) {
        String bucket = "";
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3EventNotification.S3EventNotificationRecord s3EventNotificationRecord = event.getRecords().get(0);
            bucket = s3EventNotificationRecord.getS3().getBucket().getName();
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
            s3Client.deleteObject(request);
            String message = "File of name %s deleted from bucket %s";
            logger.log(format(message, fileName, bucket));
        } catch (Exception error) {
            String errorMessage = "Error deleting file of name %s in bucket %s";
            logger.log(format(errorMessage, fileName, bucket));
            logger.log(error.getMessage());
        }
    }
}
