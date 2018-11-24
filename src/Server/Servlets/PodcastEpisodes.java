package Server.Servlets;

import com.google.gson.Gson;
import Server.SearchServer.result.RssResults;
import Server.SearchServer.RssParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * authors:Bari Halag,Tamir Michali,Elad Taannmi
 *
 * this is the servlet to return to the user all the episodes of given podcast
 * */

@WebServlet(name = "PodcastEpisodes", urlPatterns = "/PodcastEpisodes")
public class PodcastEpisodes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String feed = req.getParameter("data");
        RssResults data = new RssParser(feed).search();
        out.println(new Gson().toJson(data));
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
