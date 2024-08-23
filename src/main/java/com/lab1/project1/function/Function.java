package com.lab1.project1.function;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);
        //Test Body
        Logger log = context.getLogger();
        DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();
        String storageConnString = System.getenv("StorageConnectionString");
        log.info("StorageConnectionString=" + storageConnString);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnString)
                .buildClient();
        String blobContainer = "myblobs";
        String filename = "test0823B.txt";
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(blobContainer);
        log.info("Get containerClient by name:" + blobContainer + "=" + blobContainerClient);
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        log.info("Get client by file name:" + filename + "=" + blobClient);
        boolean deleted = blobClient.deleteIfExists();
        log.info("Delete blob file: " + blobContainer + "/" + filename + "=" + deleted);
        //
        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
