package Server.SearchServer.result;

import java.util.List;

/**
 * this class is for holding the All the RSS results
 * */

public class RssResults {

    //private int resultCount;
    private List<RssResult> results;

//    public int getResultCount() {
//        return resultCount;
//    }
//
//    public void setResultCount(int resultCount) {
//        this.resultCount = resultCount;
//    }

    public List<RssResult> getResults() {
        return results;
    }

    public void setResults(List<RssResult> results) {
        this.results = results;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssResults that = (RssResults) o;

        //if (resultCount != that.resultCount) return false;
        if (results != null ? !results.equals(that.results) : that.results != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "results{" + results +
                '}';
    }

}
