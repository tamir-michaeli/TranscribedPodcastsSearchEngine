package Server.SearchServer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * authors:Bari Halag,Tamir Michali,Elad Taannmi
 * <p>
 * this class is for download an episode, needed to download and convert the mp3 file to flac in order to Transcript the podcast.
 */
public class Downloader {

    private static final String SAVE_PATH = "Audio";
    private static String PodCastURL;

    public Downloader(String podCastURL) {
        PodCastURL = podCastURL;
        checkAndCreateDownloadDir();
    }

    private void checkAndCreateDownloadDir() {
        File transcribtionDirectory = new File("Audio");
        if (!transcribtionDirectory.exists()) {
            transcribtionDirectory.mkdir();
        }
    }

    public static String getPodCastURL() {
        return PodCastURL;
    }

    public static void setPodCastURL(String podCastURL) {
        PodCastURL = podCastURL;
    }

    public static void getFile() {
        try {
            URLConnection connection = new URL(PodCastURL).openConnection();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File("check.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String DownloadFile()
            throws IOException {
        String saveFilePath = null;
        URL url = new URL(PodCastURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();


            // extracts file name from URL
            fileName = PodCastURL.substring(PodCastURL.lastIndexOf("/") + 1,
                    PodCastURL.indexOf(".mp3", PodCastURL.lastIndexOf("/")) + 4);

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            saveFilePath = SAVE_PATH + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return saveFilePath;
    }
}
