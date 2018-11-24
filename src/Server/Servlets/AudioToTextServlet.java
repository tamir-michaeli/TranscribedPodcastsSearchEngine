package Server.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import Server.CloudHandler.TextItManager;
import Server.SearchServer.Lucene.LucenePodcastInfo;
import Server.SearchServer.Lucene.LuceneSearcher;
import Server.SearchServer.result.SearchResult;

/**
 * authors:Bari Halag,Tamir Michaeli,Elad Tanami
 * <p>
 * this is the servlet to Transcript podcast .
 */
@WebServlet(name = "AudioToTextServlet", urlPatterns = "/AudioToTextServlet")
public class AudioToTextServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String PodcastURL = req.getParameter("PodcastURL");
        String EpisodeName = req.getParameter("EpisodeName");
        String Language = req.getParameter("Lang");
        boolean hasTranscription = false;
        try {                             //check if there is a transcription to this episode already
            LuceneSearcher luceneSearcher = new LuceneSearcher();
            List<LucenePodcastInfo> searchResults = luceneSearcher.SearchPhrase(PodcastURL);
            if (searchResults.size() > 0) {
                hasTranscription = true;
            }
        } catch (ParseException parseException) {
            System.out.println(parseException.getMessage());
        }

        if (!hasTranscription) {                           //if there is no transcription gather all the information about the podcast and the episode
            SearchResult searchResult = getParametersFromReponse(req);

            try {
                TextItManager textItManager = new TextItManager();
                textItManager.TextIt(searchResult, PodcastURL, EpisodeName, Language);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        } else {
            PrintWriter out = resp.getWriter();
            out.print(hasTranscription);
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private SearchResult getParametersFromReponse(HttpServletRequest req) {
        SearchResult searchResult = new SearchResult();
        searchResult.setArtistName(req.getParameter("PodcastInfo[artistName]"));
        searchResult.setArtworkUrl100(req.getParameter("PodcastInfo[artworkUrl100]"));
        searchResult.setCollectionViewUrl(req.getParameter("PodcastInfo[collectionViewUrl]"));
        searchResult.setfeedUrl(req.getParameter("PodcastInfo[feedUrl]"));
        searchResult.setKind(req.getParameter("PodcastInfo[kind]"));
        searchResult.setReleaseDate(req.getParameter("PodcastInfo[releaseDate]"));
        searchResult.settrackId(req.getParameter("PodcastInfo[trackId]"));
        searchResult.setWrapperType(req.getParameter("PodcastInfo[wrapperType]"));
        searchResult.setTrackName(req.getParameter("PodcastInfo[trackName]"));

        return searchResult;
    }

}
