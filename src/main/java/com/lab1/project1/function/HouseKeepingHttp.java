package com.lab1.project1.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.sql.annotation.CommandType;
import com.microsoft.azure.functions.sql.annotation.SQLInput;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HouseKeepingHttp {
    @FunctionName("HouseKeepingHttp")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<String>> request,
            @SQLInput(
                    name = "result",
                    commandText = "SELECT * FROM UploadLog WHERE CreateTime < DATEADD(DAY, -7, GETDATE())",
                    commandType = CommandType.Text,
                    connectionStringSetting = "SqlConnectionString")
            UploadLog[] result,
            final ExecutionContext context) {
        Logger log = context.getLogger();
        log.info(context.getFunctionName() + ">>> Http trigger function executed at: " + LocalDateTime.now());
        HouseKeeping hw=new HouseKeeping();
        int count=hw.houseKeepingUploadFiles(result, context);
        String message = "Delete " + count + " files.";
        return request.createResponseBuilder(HttpStatus.OK).body(message).build();
    }
}
