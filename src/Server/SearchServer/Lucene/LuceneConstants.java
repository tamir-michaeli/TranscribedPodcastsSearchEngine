package Server.SearchServer.Lucene;

public class LuceneConstants {
    static final String CONTENTS = "contents";
    static final String FILE_NAME = "filename";
    static final String FILE_PATH = "filepath";
    static int MAX_SEARCH = 10;
    static final String INDEX_DIR = "Lucene/Index";        // Default path is at the Tomcat/bin folder
    public static final String DATA_DIR = "Lucene/Data";

    public void SetMaxSearchResults(int maxResults) {
        MAX_SEARCH = maxResults;
    }
}
