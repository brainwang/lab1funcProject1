package com.lab1.project1.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.sql.annotation.CommandType;
import com.microsoft.azure.functions.sql.annotation.SQLInput;

import java.util.Optional;
import java.util.logging.Logger;

public class QueryUploadLogs {
    @FunctionName("QueryUploadLogs")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "")
                HttpRequestMessage<Optional<String>> request,
            @SQLInput(
                name = "result",
                commandText = "SELECT * FROM UploadLog WHERE CreateTime >= CONVERT(DATE, @DateString)",
                parameters = "@DateString={Query.date}",
                commandType = CommandType.Text,
                connectionStringSetting = "SqlConnectionString")
                UploadLog[] result,
            final ExecutionContext context
    ) {
        Logger log=context.getLogger();
        final String date = request.getQueryParameters().get("date");
        log.info("QueryUploadLogs query logs after date : " + date);
        return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "application/json").body(result).build();
    }
}