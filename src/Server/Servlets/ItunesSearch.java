package Server.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.SearchServer.SearchApi;
import Server.SearchServer.parameters.SearchParameters;
import Server.SearchServer.parameters.parameter.Lang;
import Server.SearchServer.parameters.parameter.Limit;
import Server.SearchServer.parameters.parameter.Media;
import Server.SearchServer.result.SearchResults;
import com.google.gson.Gson;




/**
 * authors:Bari Halag,Tamir Michali,Elad Taannmi
 *
 * this is the main servlet to search for podcast.
 * */
@WebServlet(name = "ItunesSearch", urlPatterns = "/ItunesSearch")
public class ItunesSearch extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        SearchResults data = SearchApi.search(createParams(request.getParameter("data")));
        out.println(new Gson().toJson(data));
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private SearchParameters createParams(String terms) {
        SearchParameters searchParams = new SearchParameters();
        searchParams.addQueryTerm(terms);

        searchParams.setMedia(Media.POD_CAST);
        searchParams.setLimit(new Limit(200));
        return searchParams;
    }


}
