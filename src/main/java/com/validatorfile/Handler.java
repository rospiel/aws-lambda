package com.validatorfile;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.validatorfile.service.S3DeleteObjectUseCase;
import com.validatorfile.service.ValidatorFileUseCase;
import com.validatorfile.service.impl.S3DeleteObjectUseCaseImpl;
import com.validatorfile.service.impl.ValidatorFileUseCaseImpl;

public class Handler implements RequestHandler<S3Event, String> {

    private ValidatorFileUseCase validatorFileUseCase = new ValidatorFileUseCaseImpl();

    @Override
    public String handleRequest(S3Event event, Context context) {
        LambdaLogger logger = context.getLogger();
        S3EventNotification.S3EventNotificationRecord s3EventNotificationRecord = event.getRecords().get(0);
        String fileName = s3EventNotificationRecord.getS3().getObject().getUrlDecodedKey();
        logger.log("Starting validation of file ".concat(fileName));

        boolean isValid = validatorFileUseCase.isValid(context, logger, fileName);
        if (isValid) {
            return "OK";
        }

        S3DeleteObjectUseCase s3DeleteObjectUseCase = new S3DeleteObjectUseCaseImpl();
        s3DeleteObjectUseCase.deleteObject(context, logger, event, fileName);
        return "NOK";
    }
}
