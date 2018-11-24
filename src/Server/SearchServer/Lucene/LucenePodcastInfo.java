package Server.SearchServer.Lucene;

public class LucenePodcastInfo {
    private String Artist = "";
    private String PodcastName= "";
    private String ArtworkURL = "";
    private float NumSearchHits =0 ;
    private String PodcastURL = "";
    private String ReleaseDate = "";
    private String EpisodeName = "";
    private String Text = "";


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getEpisodeName() {
        return EpisodeName;
    }

    public void setEpisodeName(String episodeName) {
        EpisodeName = episodeName;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getPodcastName() {
        return PodcastName;
    }

    public void setPodcastName(String podcastName) {
        PodcastName = podcastName;
    }

    public String getArtworkURL() {
        return ArtworkURL;
    }

    public void setArtworkURL(String artworkURL) {
        ArtworkURL = artworkURL;
    }

    public float getNumSearchHits() {
        return NumSearchHits;
    }

    public void setNumSearchHits(float numSearchHits) {
        NumSearchHits = numSearchHits;
    }

    public String getPodcastURL() {
        return PodcastURL;
    }

    public void setPodcastURL(String podcastURL) {
        PodcastURL = podcastURL;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }
}
