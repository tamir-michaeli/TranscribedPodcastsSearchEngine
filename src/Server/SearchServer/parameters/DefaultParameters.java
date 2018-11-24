package Server.SearchServer.parameters;

import Server.SearchServer.parameters.parameter.Country;
import Server.SearchServer.parameters.parameter.Lang;
import Server.SearchServer.parameters.parameter.Limit;
import Server.SearchServer.parameters.parameter.Media;

/**
 * @author Sergii.Zagriichuk
 */
public interface DefaultParameters {
    public final static Country DEFAULT_COUNTRY = new Country("US");
    public final static Media DEFAULT_MEDIA = Media.POD_CAST;
    public final static Lang DEFAULT_LANG = Lang.ENGLISH;
    public final static Limit DEFAULT_LIMIT = new Limit(50);
}
