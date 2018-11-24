package Server.Servlets;

import com.google.gson.Gson;
import Server.SearchServer.Lucene.LuceneConstants;
import Server.SearchServer.Lucene.LucenePodcastInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "ShowAllTranscripts" , urlPatterns = "/ShowAllTranscripts")
public class ShowAllTranscripts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        List<LucenePodcastInfo> lucenePodcastInfoList = new LinkedList<>();
        File TranscribeFolder = new File(LuceneConstants.DATA_DIR);
        PrintWriter jsonWriter = resp.getWriter();

        for (File file : TranscribeFolder.listFiles())
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            LucenePodcastInfo lucenePodcastInfo = new LucenePodcastInfo();
            String Line;

            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setArtist(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setPodcastName(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setReleaseDate(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setArtworkURL(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setPodcastURL(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setEpisodeName(Line);
            }
            if ((Line = br.readLine()) != null) {
                lucenePodcastInfo.setText(Line);
            }
            lucenePodcastInfoList.add(lucenePodcastInfo);
        }
        jsonWriter.println(new Gson().toJson(lucenePodcastInfoList));
        jsonWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
