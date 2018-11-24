package Server.SearchServer.result;


/**
 * this class is for holding the RSS single result
 * */

public class RssResult {
    private String PodCastURL;
    private String EpisodeName;
    private String Duration;

    public String getDuration() { return Duration; }

    public void setDuration(String duration) { Duration = duration;}

    public String getPodCastURL() {
        return PodCastURL;
    }

    public void setPodCastURL(String podCastURL) {
        PodCastURL = podCastURL;
    }

    public String getEpisodeName() {
        return EpisodeName;
    }

    public void setEpisodeName(String episodeName) {
        EpisodeName = episodeName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssResult that = (RssResult) o;

        if (PodCastURL != null ? !PodCastURL.equals(that.PodCastURL) : that.PodCastURL != null) return false;
        if (EpisodeName != null ? !EpisodeName.equals(that.EpisodeName) : that.EpisodeName != null) return false;
        if (Duration != null ? !Duration.equals(that.Duration) : that.Duration != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = PodCastURL != null ? PodCastURL.hashCode() : 0;
        result = 31 * result + (EpisodeName != null ? EpisodeName.hashCode() : 0);
        result = 31 * result + (Duration != null ? Duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "PodCastURL='" + PodCastURL + '\'' +
                ", EpisodeName='" + EpisodeName + '\'' +
                ", Duration='" + Duration + '\'' +
                '}';
    }
}
