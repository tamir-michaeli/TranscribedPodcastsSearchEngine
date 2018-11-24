package Server.CloudHandler;

import ie.corballis.sox.SoXEffect;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;
import ie.corballis.sox.WrongParametersException;
import javaFlacEncoder.FLAC_FileEncoder;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static Server.CloudHandler.CloudConstants.AUDIO_BITDEPTH;
import static Server.CloudHandler.CloudConstants.FLAC_EXT;
import static Server.CloudHandler.CloudConstants.WAV_EXT;

public class AudioFormatConverter {
    private final int EXT_LEN = 4;
    private final String POST_WAV_CHANNELS = "1";


    private String Mp3ToWav(String mp3FilePath) throws UnsupportedAudioFileException, IOException, WrongParametersException {

        String wavPreMixFilePath = mp3FilePath.substring(0, mp3FilePath.length() - EXT_LEN) + "_pre" + WAV_EXT;
        String wavPostMixFilePath = mp3FilePath.substring(0, mp3FilePath.length() - EXT_LEN) + WAV_EXT;
        // open stream
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(new File(mp3FilePath));
        AudioFormat sourceFormat = mp3Stream.getFormat();
        // create audio format object for the desired stream/audio format
        // this is *not* the same as the file format (wav)
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(), AUDIO_BITDEPTH,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write stream into a file with file format wav
        AudioSystem.write(converted, AudioFileFormat.Type.WAVE, new File(wavPreMixFilePath));
        setChannelAndSampleRate((int) sourceFormat.getSampleRate(), wavPreMixFilePath, wavPostMixFilePath);

        return wavPostMixFilePath;
    }

    private String WavToFlac(String wavFilePath) {
        String flacFilePath = wavFilePath.substring(0, wavFilePath.length() - EXT_LEN) + FLAC_EXT;
        FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
        File inputFile = new File(wavFilePath);
        File outputFile = new File(flacFilePath);

        flacEncoder.encode(inputFile, outputFile);
        return flacFilePath;
    }


    private void setChannelAndSampleRate(int sampleRate, String preMixFilePath, String postMixFilePath)
            throws WrongParametersException, IOException {
        Sox sox = new Sox("sox-14-4-2/sox.exe");
        sox
                .sampleRate(sampleRate)
                .inputFile(preMixFilePath)
                .encoding(SoXEncoding.SIGNED_INTEGER)
                .bits(AUDIO_BITDEPTH)
                .outputFile(postMixFilePath)
                .effect(SoXEffect.REMIX, POST_WAV_CHANNELS)
                .execute();
        new File(preMixFilePath).delete(); // Delete the temp wav file
    }

    // Returns converted flac file path
    // Does not delete: the original mp3 file (sample rate is needed)
    public String ConvertToFlac(String originalFilePath) {
        String flacFilePath = null;
        try {
            System.out.println("Starting flac conversion of: " + originalFilePath + " " + new Date().toString());
            String wavFilePath = Mp3ToWav(originalFilePath);
            flacFilePath = WavToFlac(wavFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Finshed flac conversion of: " + flacFilePath + " " + new Date().toString());
        return flacFilePath;
    }

    public int GetSampleRate(String filePath) throws IOException, UnsupportedAudioFileException {
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(new File(filePath));
        AudioFormat sourceFormat = mp3Stream.getFormat();
        return (int) sourceFormat.getSampleRate();
    }

    public void CleanAudioFiles(String mp3FilePath, String flacFilePath) {
        String wavFilePath = mp3FilePath.substring(0, mp3FilePath.length() - EXT_LEN) + WAV_EXT;
        new File(new File(mp3FilePath).getAbsolutePath()).delete();
        new File(new File(flacFilePath).getAbsolutePath()).delete();
        new File(new File(wavFilePath).getAbsolutePath()).delete();
    }
}
