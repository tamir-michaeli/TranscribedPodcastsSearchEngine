package Server.CloudHandler;

import Server.SearchServer.Downloader;
import Server.SearchServer.result.SearchResult;

import javax.servlet.ServletException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutionException;

public class TextItManager{

    public void TextIt(SearchResult searchResult, String podcastURL, String episodeName,String language) throws IOException,
            ExecutionException, InterruptedException, ServletException, UnsupportedAudioFileException {
        AudioFormatConverter formatConverter = new AudioFormatConverter();
        AudioToTextTranscriber textTranscriber = new AudioToTextTranscriber();
        CloudFileUploader fileUploader = new CloudFileUploader();
        Downloader downloader = new Downloader(podcastURL);
        String mp3FilePath = downloader.DownloadFile();
        int originalSampleRate = formatConverter.GetSampleRate(mp3FilePath);
        String flacFilePath = formatConverter.ConvertToFlac(mp3FilePath);
        String formattedEpisodeName= checkAndReplaceSpecialChars(episodeName); //make file name valid (without '[]<>|/\?:"')
        String uploadedFileURL = fileUploader.UploadFile(flacFilePath, formattedEpisodeName);
        textTranscriber.AsyncTranscribtion(uploadedFileURL, formattedEpisodeName, podcastURL, searchResult, originalSampleRate,language);
        formatConverter.CleanAudioFiles(mp3FilePath, flacFilePath);
        fileUploader.DeleteFileFromCloud(CloudConstants.CLOUD_BUCKET_NAME, formattedEpisodeName);
    }

    // Replaces invalid filename chars
    private String checkAndReplaceSpecialChars(String fileName) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\u0590-\u05FF]*");
        Matcher matcher = pattern.matcher(fileName);

        if (!matcher.matches()) {
            return fileName.replaceAll("[^a-zA-Z0-9\u0590-\u05FF]", "");
        }

        return fileName;
    }
}
