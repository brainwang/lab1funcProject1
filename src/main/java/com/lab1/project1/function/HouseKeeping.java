package com.lab1.project1.function;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import com.microsoft.azure.functions.sql.annotation.CommandType;
import com.microsoft.azure.functions.sql.annotation.SQLInput;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Azure Functions with Timer trigger.
 */
public class HouseKeeping {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("HouseKeeping")
    public void run(
            @TimerTrigger(name = "timerInfo", schedule = "0 */5 * * * *")
            String timerInfo,

            @SQLInput(
                    name = "result",
                    commandText = "SELECT * FROM UploadLog WHERE CreateTime < DATEADD(DAY, -7, GETDATE())",
                    commandType = CommandType.Text,
                    connectionStringSetting = "SqlConnectionString")
            UploadLog[] result,
            final ExecutionContext context
    ) {
        Logger log = context.getLogger();
        log.info(context.getFunctionName() + ">>> Timer trigger function executed at: " + LocalDateTime.now());
        houseKeepingUploadFiles(result, context);
    }

    public int houseKeepingUploadFiles(UploadLog[] result, ExecutionContext context) {
        Logger log = context.getLogger();
        String storageConnString = System.getenv("StorageConnectionString");
        log.info(context.getFunctionName() + ">>> StorageConnectionString=" + storageConnString);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnString)
                .buildClient();
        String blobContainer = "myblobs";
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(blobContainer);
        int count = 0;
        log.info(context.getFunctionName() + ">>> Found " + result.length + " files to be deleted!!!");
        for (UploadLog f : result) {
            String filename = f.getBlobName();
            BlobClient blobClient = blobContainerClient.getBlobClient(filename);
            boolean deleted = blobClient.deleteIfExists();
            if (deleted) {
                count++;
                log.info(context.getFunctionName() + ">>> Deleted blob file: " + blobContainer + "/" + filename);
            }
        }
        log.info(context.getFunctionName() + ">>> is completed, deleted " + count + " files!!!");
        return count;
    }

}
