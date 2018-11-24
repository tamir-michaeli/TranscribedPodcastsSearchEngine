package Server.SearchServer.result;
/**
 *
 * main idea of: @author szagriichuk
 * improvements by: Bari Halag,Tamir Michali,Elad Taannmi
 *
 * this class is for holding single Search result of podcast
 */
public class SearchResult {
    private String wrapperType;
    private String explicitness;
    private String kind;
    private String trackName;
    private String artistName;
    private String collectionViewUrl;
    private String feedUrl;
    private String artworkUrl100;
    private String trackId;
    private String releaseDate;


    public String gettrackId() {return trackId;}

    public void settrackId(String trackId) {this.trackId = trackId;}

    public String getWrapperType() {
        return wrapperType;
    }

    public void setWrapperType(String wrapperType) {
        this.wrapperType = wrapperType;
    }

    public String getExplicitness() {
        return explicitness;
    }

    public void setExplicitness(String explicitness) {
        this.explicitness = explicitness;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getCollectionViewUrl() {return collectionViewUrl;}

    public void setCollectionViewUrl(String collectionViewUrl) {this.collectionViewUrl = collectionViewUrl;}

    public String getfeedUrl() {return feedUrl;}

    public void setfeedUrl(String feedUrl) {this.feedUrl = feedUrl;}

    public String getArtworkUrl100() {return artworkUrl100;}

    public void setArtworkUrl100(String artworkUrl100) {this.artworkUrl100 = artworkUrl100;}

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (collectionViewUrl != null ? !collectionViewUrl.equals(that.collectionViewUrl) : that.collectionViewUrl != null) return false;
        if (artistName != null ? !artistName.equals(that.artistName) : that.artistName != null) return false;
        if (feedUrl != null ? !feedUrl.equals(that.feedUrl) : that.feedUrl != null) return false;
        if (artworkUrl100 != null ? !artworkUrl100.equals(that.artworkUrl100) : that.artworkUrl100 != null) return false;
        if (explicitness != null ? !explicitness.equals(that.explicitness) : that.explicitness != null) return false;
        if (kind != null ? !kind.equals(that.kind) : that.kind != null) return false;
        if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
        if (trackName != null ? !trackName.equals(that.trackName) : that.trackName != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;
        if (wrapperType != null ? !wrapperType.equals(that.wrapperType) : that.wrapperType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = wrapperType != null ? wrapperType.hashCode() : 0;
        result = 31 * result + (explicitness != null ? explicitness.hashCode() : 0);
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        result = 31 * result + (trackName != null ? trackName.hashCode() : 0);
        result = 31 * result + (artistName != null ? artistName.hashCode() : 0);
        result = 31 * result + (collectionViewUrl != null ? collectionViewUrl.hashCode() : 0);
        result = 31 * result + (feedUrl != null ? feedUrl.hashCode() : 0);
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (artworkUrl100 != null ? artworkUrl100.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "wrapperType='" + wrapperType + '\'' +
                ", explicitness='" + explicitness + '\'' +
                ", kind='" + kind + '\'' +
                ", trackId='" + trackId + '\'' +
                ", trackName='" + trackName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", collectionViewUrl='" + collectionViewUrl + '\'' +
                ", feedUrl='" + feedUrl + '\'' +
                ", artworkUrl100='" + artworkUrl100 + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
