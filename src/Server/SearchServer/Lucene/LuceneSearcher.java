package Server.SearchServer.Lucene;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneSearcher {

    private Indexer dataIndexer;
    private Searcher querySearcher;


    // Indexing all files once LuceneSearcher is created
    public LuceneSearcher() throws IOException {
        createIndex();
    }

    private void createIndex() throws IOException {
        dataIndexer = new Indexer(LuceneConstants.INDEX_DIR);
        dataIndexer.createIndex(LuceneConstants.DATA_DIR, new TextFileFilter());
        dataIndexer.close();
    }

    public List<LucenePodcastInfo> SearchPhrase(String searchQuery) throws IOException, ParseException {
        querySearcher = new Searcher(LuceneConstants.INDEX_DIR);
        TopDocs topResults = querySearcher.search(searchQuery);
        System.out.println(topResults.totalHits);
        List<LucenePodcastInfo> podcastInfoList = new LinkedList<>();

        for (ScoreDoc docResult : topResults.scoreDocs) {
            Document resultFile = querySearcher.getDocument(docResult);
            podcastInfoList.add(extractInfoFromFile(resultFile, docResult.score));
        }

        querySearcher.close();
        return podcastInfoList;
    }

    // Extracts podcast info from file found on search
    private LucenePodcastInfo extractInfoFromFile(Document podcastDoc, float numHits) throws IOException {
        File transcribtion = new File(podcastDoc.get(LuceneConstants.FILE_PATH));
        LucenePodcastInfo podcastInfo = new LucenePodcastInfo();
        BufferedReader br = new BufferedReader(new FileReader(transcribtion));
        String st;

        if ((st = br.readLine()) != null) {
            podcastInfo.setArtist(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setPodcastName(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setReleaseDate(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setArtworkURL(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setPodcastURL(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setEpisodeName(st);
        }
        if ((st = br.readLine()) != null) {
            podcastInfo.setText(st);
        }
        podcastInfo.setNumSearchHits(numHits);
        br.close();

        return podcastInfo;
    }
}