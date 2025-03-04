package com.validatorfile.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public interface S3DeleteObjectUseCase {

    void deleteObject(Context context, LambdaLogger logger, S3Event event, String fileName);
}
