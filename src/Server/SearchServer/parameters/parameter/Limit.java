package Server.SearchServer.parameters.parameter;

/**
 * @author szagriichuk
 */
public class Limit implements Parameter {
    private int limit;
    private static final int maxLimit = 20;

    public Limit(int limit) {
        this.limit = changeRange(limit);
    }

    private int changeRange(int limit) {
        if (limit > maxLimit)
            limit = maxLimit;
        return limit;
    }

    @Override
    public String createSearchParameter() {
        return "limit=" + limit;
    }
}
