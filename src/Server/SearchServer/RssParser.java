package Server.SearchServer;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import org.jdom2.Element;
import Server.SearchServer.result.RssResults;
import com.google.gson.Gson;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


/**
 *
 * main idea of: @author szagriichuk
 * improvements by: Bari Halag,Tamir Michali,Elad Taannmi
 *
 * this class is for searching with Itunes API for PodCasts.
 * This class is simmeler to the SearchAPI class
 */
public class RssParser {

    private static String RssUrl;
    public final static int RssLimit = 20; //return results to the user
    public RssParser(String rssUrl) {
        RssUrl = rssUrl;
    }

    public String getRssUrl() {
        return RssUrl;
    }
    public void setRssUrl(String rssUrl) {
        RssUrl = rssUrl;
    }


    /**
     * this function is the "Main function of this class" will start the search with Itunes API
     *
     * */
    public static RssResults search() {
//        URL url;
//        url = createUrl(RssUrl);
//        HttpURLConnection connection = openConnection(url);
//        connection.addRequestProperty("User-Agent","Chrome");
        return parseRSSResponseData(RsFeed());
    }

    /**
     * this function will make a JSON from the results from the API
     * */
    private static RssResults parseRSSResponseData(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, RssResults.class);
    }


    /**
     * this function will return String that include all the relevant data from the RSS of the podcast page.
     *
     * the format of the string is simmeler to the JSON format
     * */

    private static String RsFeed()
    {
        StringBuilder builder = new StringBuilder();
        String Time="";
        builder.append("{\"results\":[");
        int limit = RssLimit;
        URL feedSource = null;
        try {
            feedSource = new URL(RssUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = null;
        try {
            feed = input.build(new XmlReader(feedSource));
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SyndEntry> RssEntrys = feed.getEntries();
        if (RssEntrys.size() < RssLimit)
        {
            limit = RssEntrys.size();
        }
        for (int i=0;i<limit;i++) {
            String EpisodeName = RssEntrys.get(i).getTitle().replace("\"","'");
            builder.append("{\"EpisodeName\":" + "\"" + EpisodeName + "\","); //enter to the string the name of the episode
            builder.append("\"PodCastURL\":" + "\"" + RssEntrys.get(i).getEnclosures().get(0).getUrl() + "\",");
            List<Element> foreignMarkup = RssEntrys.get(i).getForeignMarkup();
            for(Element elem : foreignMarkup)
            {
                if (elem.getName().equals("duration"))
                {
                    Time = elem.getContent(0).getValue();
                }
            }

            int DurationInSeconeds;

            if (Time == "")
            {
                Time = "--:--:--";
            }
            else if(Time.indexOf(":") == -1) { //duration in seconds
                DurationInSeconeds = Integer.parseInt(Time);
                LocalTime lt = LocalTime.MIN.plusSeconds(DurationInSeconeds);
                DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");
                String output = lt.format(f);
                Time = output;
            }
            else if (Time.indexOf("CDATA") != -1) //CDATA[HH:MM:SS]
            {
                String output="";
                int CDATAFirstIndex = Time.indexOf("A[") + 2;
                int CDATALastIndex = Time.indexOf("]]");
                if(CDATAFirstIndex != -1 || CDATALastIndex !=-1)
                    output = Time.substring(CDATAFirstIndex,CDATALastIndex);
                Time = output;
            }
            builder.append("\"Duration\":" + "\"" + Time + "\"},");

        }
        int EndIndex= builder.length()-1-1; //end index of the result string

        String res = builder.substring(0,EndIndex);
        builder.replace(0,builder.length(),res);
        builder.append("}]}"); //close the string with JSON format
        return builder.toString();
    }


}
