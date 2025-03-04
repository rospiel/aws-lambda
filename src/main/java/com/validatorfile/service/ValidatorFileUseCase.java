package com.validatorfile.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public interface ValidatorFileUseCase {

    boolean isValid(Context context, LambdaLogger logger, String fileName);
}
