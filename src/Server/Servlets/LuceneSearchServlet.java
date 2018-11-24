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
import Server.SearchServer.Lucene.LucenePodcastInfo;
import Server.SearchServer.Lucene.LuceneSearcher;
import com.google.gson.Gson;

/**
 * authors:Bari Halag,Tamir Michali,Elad Taannmi
 *
 * this is the servlet to search for podcast in the pool of Transcription podcasts .
 * */
@WebServlet(name = "LuceneSearch", urlPatterns = "/luceneSearch")
public class LuceneSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter jsonWriter = response.getWriter();
        try {
            LuceneSearcher luceneSearcher = new LuceneSearcher();
            List<LucenePodcastInfo> searchResults = luceneSearcher.SearchPhrase(request.getParameter("data"));
            jsonWriter.println(new Gson().toJson(searchResults));
        } catch (ParseException parseException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        jsonWriter.close();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
