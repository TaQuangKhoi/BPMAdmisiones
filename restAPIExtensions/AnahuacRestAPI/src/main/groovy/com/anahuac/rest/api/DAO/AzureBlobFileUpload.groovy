package com.anahuac.rest.api.DAO

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.logging.Level;
import java.util.logging.Logger;

class AzureBlobFileUpload {
/*
    public static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;" + "AccountName=integrasoa;" + "AccountKey=NMuzPqjIfIlJ5RVtpyDbkboC4V6Viw3ONlUBm9J0KSb+lCNeMUapCaZUK/oKfGESN7Jlkj0PuRK69ImeOgIzsA==";
    private static final Logger LOGGER = Logger.getLogger(AzureBlobFileUpload.class.getName());

    public static void main(String[] args) throws URISyntaxException, StorageException, InvalidKeyException, IOException {

        AzureBlobFileUpload ab = new AzureBlobFileUpload();
        ab.uploadFile(new File(args[0]));
    }

    public void uploadFile(File file) throws URISyntaxException, StorageException, InvalidKeyException, IOException {

        try {
            CloudStorageAccount account = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("public");
            container.createIfNotExists();
            CloudBlockBlob blob = container.getBlockBlobReference(file.getName());
            blob.uploadFromFile(file.getAbsolutePath());
            System.out.println("File uploaded successfully");
            //new File( file.getName()).delete(); //deleting files from local system.
        } catch (StorageException | IOException | URISyntaxException | InvalidKeyException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());
            System.exit(-1);
        }
    }*/
}