package Server.CloudHandler;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class CloudFileUploader {
    private Storage m_CloudStorage;

    public CloudFileUploader() throws IOException {
        authenticateCredentials();
    }

    // Authenticating Google credentials and creating a cloud storage instance
    private void authenticateCredentials() throws IOException {
        List<String> credentialsScope = new ArrayList<>();
        credentialsScope.add(CloudConstants.CLOUD_API_URL);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new URL(CloudConstants.CREDENTIALS_URL).openStream())
                .createScoped(credentialsScope);
        m_CloudStorage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    // Uploads a specific file to Google Cloud Storage
    public String UploadFile(String filePath, String fileName)
            throws IOException {

        System.out.println("Strating upload to google cloud " + fileName + " " + new Date().toString());
        InputStream fileStream = new FileInputStream(filePath);
        BlobInfo blobInfo =
                m_CloudStorage.create(
                        BlobInfo
                                .newBuilder(CloudConstants.CLOUD_BUCKET_NAME, fileName).setContentType(CloudConstants.FLAC_CONTENT_TYPE)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(), fileStream);
        fileStream.close();
        // return the google cloud file path
        System.out.println("Finished upload to google cloud " + fileName + " " + new Date().toString());
        return CloudConstants.GC_URL_PREFIX + CloudConstants.CLOUD_BUCKET_NAME
                + "/" + fileName;
    }

    public void DeleteFileFromCloud(String bucketName, String fileName) {
        System.out.println("Deleting file from cloud: " + fileName + " " + new Date().toString());
        BlobId fileToDelete = BlobId.of(bucketName, fileName);
        m_CloudStorage.delete(fileToDelete);
    }
}
