package Server.CloudHandler;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import Server.SearchServer.Lucene.LuceneConstants;
import Server.SearchServer.result.SearchResult;

import static Server.CloudHandler.CloudConstants.TXT_EXT;


// Transcribing Audio to text using Google Speech API
public class AudioToTextTranscriber {
    private final int THREAD_SLEEP_TIME = 10000;

    private SpeechClient m_SpeechClient;

    public AudioToTextTranscriber() throws IOException {
        checkAndCreateTranscribtionDir();
        createSpeechClient();
    }

    private void checkAndCreateTranscribtionDir() {
        File transcribtionDirectory = new File(LuceneConstants.DATA_DIR);
        if (!transcribtionDirectory.exists()) {
            transcribtionDirectory.mkdir();
        }
    }

    // Validating GoogleAPI connection using Credentials specified in the URL
    private void createSpeechClient() throws IOException {
        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(
                new URL(CloudConstants.CREDENTIALS_URL).openStream()));

        SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        m_SpeechClient = SpeechClient.create(settings);
    }

    public void AsyncTranscribtion(String gcsUri, String fileName, String podcastURL,
                                   SearchResult podcastInfo, int sampleRate, String language) throws InterruptedException,
            FileNotFoundException, ExecutionException {
        System.out.println("Starting Transcribtion " + fileName + " lang: " + language + " " + new Date().toString());
        // Configure remote file request for Flac format
        RecognitionConfig config =
                RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                        .setLanguageCode(language)
                        .setSampleRateHertz(sampleRate)
                        .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

        // Use non-blocking call for getting file transcription
        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                m_SpeechClient.longRunningRecognizeAsync(config, audio);
        while (!response.isDone()) {
            // Waiting for reponse
            Thread.sleep(THREAD_SLEEP_TIME);
        }

        List<SpeechRecognitionResult> results = response.get().getResultsList();
        saveTranscribtionToFile(fileName, results, podcastURL, podcastInfo);
        System.out.println("Finished Transcribtion " + fileName + " " + new Date().toString());
    }

    // Saving transcribed audio to a specified file
    private void saveTranscribtionToFile(String fileName, List<SpeechRecognitionResult> transcribeResult,
                                         String podcastURL, SearchResult podcastInfo)
            throws FileNotFoundException {
        File transcribtionFile = new File(LuceneConstants.DATA_DIR + File.separator + fileName + TXT_EXT);
        System.out.println("File Name: " + fileName + TXT_EXT);
        System.out.println("File path: " + transcribtionFile.getAbsolutePath());
        try (PrintWriter filePrinter = new PrintWriter(transcribtionFile)) {
            filePrinter.println(podcastInfo.getArtistName());   // Artist
            filePrinter.println(podcastInfo.getTrackName());    // Track Name
            filePrinter.println(podcastInfo.getReleaseDate());     // Release Date
            filePrinter.println(podcastInfo.getArtworkUrl100());    // Artwork Url
            filePrinter.println(podcastURL);                        //Podcast Url
            filePrinter.println(fileName);                       //Episode Name
            for (SpeechRecognitionResult recongnitionResult : transcribeResult) {
                SpeechRecognitionAlternative recongnitionAlternative = recongnitionResult.getAlternativesList().get(0);
                filePrinter.print(recongnitionAlternative.getTranscript());
            }
        }
    }

    // FOR TEST PURPOSES ONLY! Transcribs a specified file and prints the transcribtion
    private void transcribtionTest() throws InterruptedException, ExecutionException {
        RecognitionConfig config =
                RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                        .setLanguageCode("en-US")
                        .setSampleRateHertz(44000)
                        .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder().setUri("gs://" + CloudConstants.CLOUD_BUCKET_NAME
                + "/" + "converted_example.flac").build();

        // Use non-blocking call for getting file transcription
        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                m_SpeechClient.longRunningRecognizeAsync(config, audio);
        while (!response.isDone()) {
            System.out.println("Waiting for response...");
            Thread.sleep(10000);
        }

        List<SpeechRecognitionResult> results = response.get().getResultsList();
        for (SpeechRecognitionResult result : results) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcription: %s\n", alternative.getTranscript());
        }
    }
}



