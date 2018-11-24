package Server.SearchServer;

import Server.SearchServer.parameters.SearchParameters;
import Server.SearchServer.result.SearchResults;
import com.google.gson.Gson;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Server.SearchServer.ParametersStringBuilder.buildSearchStringParams;

/**
 *
 * main idea of: @author szagriichuk
 * improvements by: Bari Halag,Tamir Michali,Elad Tanaami
 *
 * this class is for searching with Itunes API for PodCasts.
 */
public class SearchApi {
    private static final String searchUrl = "https://itunes.apple.com/search?"; //start of every podcast search
    private static final Logger logger = Logger.getLogger(SearchApi.class.getName());

    /**
     * this function is the "Main function of this class" will start the search with Itunes API
     */
    public static SearchResults search(SearchParameters params) {
        URL url;
        url = createUrl(searchUrl, buildSearchStringParams(params)); //will build the full url with all the search parameters
        HttpURLConnection connection = openConnection(url); //make the connection to the URL
        return parseResponseData(readResponse(connection));
    }


    /**
     * this function will open the connection to the search URL
     */
    private static HttpURLConnection openConnection(URL url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            logger.log(Level.ALL, e.getMessage(), e);
            throw new iTunesSearchApiException(e.getMessage());
        }
        return connection;
    }

    /**
     * this function will make a JSON from the results from the API
     */
    private static SearchResults parseResponseData(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, SearchResults.class);
    }


    /**
     * this function will return to the user
     */
    private static String readResponse(HttpURLConnection connection) {
        return readDataFromResponseStream(createResponseReader(connection));
    }

    /**
     * this function will return String with all the data that return from the URL
     * this string will be like a JSON format
     */
    private static String readDataFromResponseStream(BufferedReader responseReader) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = responseReader.readLine()) != null) {
                builder.append(line);
            }
            responseReader.close();
        } catch (IOException e) {
            logger.log(Level.ALL, e.getMessage(), e);
            throw new iTunesSearchApiException(e.getMessage());
        }
        return builder.toString();
    }

    /**
     * this function will create the reader for the DATA in URL
     */
    private static BufferedReader createResponseReader(HttpURLConnection connection) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e) {
            logger.log(Level.ALL, e.getMessage(), e);
            throw new iTunesSearchApiException(e.getMessage());
        }
        return in;
    }

    /**
     * this function will build the complete URL with params
     */
    private static URL createUrl(String mainUrl, String stringParams) {
        URL url;
        try {
            url = new URL(mainUrl + stringParams);
        } catch (MalformedURLException e) {
            logger.log(Level.ALL, e.getMessage(), e);
            throw new iTunesSearchApiException(e.getMessage());
        }
        return url;
    }
}
