package com.lab1.project1.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.sql.annotation.SQLOutput;

import java.util.logging.Logger;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class UploadTriggerToSqlDB {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("UploadTriggerToSqlDB")
    @StorageAccount("StorageConnectionString")
    public void run(
        @BlobTrigger(name = "content", path = "myblobs/{name}", dataType = "binary")
        byte[] content,

        @BindingName("name")
        String name,

        @SQLOutput(name = "output", commandText = "UploadLog",connectionStringSetting = "SqlConnectionString")
        OutputBinding<UploadLog> output,

        final ExecutionContext context
    ) {
        Logger log=context.getLogger();
        log.info("UploadTriggerToSqlDB processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
        UploadLog uploadLog = new UploadLog(name,content.length);
        log.info("Created UploadLog object: "+uploadLog);
        output.setValue(uploadLog);
        log.info("Set output binding UploadLog to: " + uploadLog);
    }
}
