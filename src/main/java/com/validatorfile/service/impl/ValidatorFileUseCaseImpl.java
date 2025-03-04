package com.validatorfile.service.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.StringUtils;
import com.validatorfile.service.ValidatorFileUseCase;

import java.util.Arrays;;import static java.lang.String.format;

public class ValidatorFileUseCaseImpl implements ValidatorFileUseCase {

    private static final String SEPARATOR = ",";
    private static final String EXTENSIONS_ENVIRONMENT_VARIABLE_NAME = "acceptedExtensions";

    @Override
    public boolean isValid(Context context, LambdaLogger logger, String fileName) {
        String extension = getExtensionFile(logger, fileName);
        String[] extensionsAccepted = getAcceptedExtensions(logger);

        boolean isValid = Arrays.stream(extensionsAccepted).anyMatch(extension::equalsIgnoreCase);

        String message = "File is valid.: %s";
        logger.log(format(message, isValid ? "Yes" : "No"));

        return isValid;
    }

    private String[] getAcceptedExtensions(LambdaLogger logger) {
        String extensions = System.getenv().get("acceptedExtensions");
        String message = "Accepted extensions found.: %s";
        logger.log(format(message, extensions));

        if (StringUtils.isNullOrEmpty(extensions)) {
            String errorMessage = "Extension is empty in environment variable %s";
            logger.log(format(errorMessage, EXTENSIONS_ENVIRONMENT_VARIABLE_NAME));
            return new String[]{};
        }

        String[] values = extensions.split(SEPARATOR);


        return values;
    }

    public String getExtensionFile(LambdaLogger logger, String fileName) {
        String[] extensions = fileName.split("\\.");

        if (extensions.length > 1) {
            String message = "Extension found.: %s";
            logger.log(format(message, extensions[1]));
            return extensions[1];
        }

        logger.log("File without extension");
        return "";

    }
}
