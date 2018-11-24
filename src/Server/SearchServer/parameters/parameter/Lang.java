package Server.SearchServer.parameters.parameter;

/**
 * @author szagriichuk
 */
public enum Lang implements Parameter {
    ENGLISH("en_us"),HEBREW("he_il");
    public final String value;

    private Lang(String value) {
        this.value = value;
    }

    public String createSearchParameter() {
        return "lang=" + value;
    }
}
