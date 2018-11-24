package Server.SearchServer;

import Server.SearchServer.parameters.SearchParameters;
import Server.SearchServer.parameters.parameter.Term;

import java.util.List;

/**
 * main idea of: @author szagriichuk
 * improvements by: Bari Halag,Tamir Michali,Elad Taannmi
 *
 * */
class ParametersStringBuilder {

    /**
     * create a valid search URL with the parameters that supplied by user
     * */
    public static String buildSearchStringParams(SearchParameters params) {
        StringBuilder resultQuery = new StringBuilder();
        buildTerms(params.getTerms(), resultQuery);
        resultQuery.append(params.getCountry().createSearchParameter()).append("&");
        resultQuery.append(params.getMedia().createSearchParameter()).append("&");
        if (params.getEntity() != null) {
            resultQuery.append(params.getEntity().createSearchParameter()).append("&");
        }
        if (params.getAttribute() != null) {
            resultQuery.append(params.getAttribute().createSearchParameter()).append("&");
        }

        resultQuery.append(params.getLimit().createSearchParameter()).append("&");
        resultQuery.append(params.getLang().createSearchParameter());
        return resultQuery.toString();
    }

    private static void buildTerms(List<String> terms, StringBuilder resultQuery) {
        if (terms != null) {
            resultQuery.append(new Term(terms).createSearchParameter()).append("&");
        } else {
            throw new iTunesSearchApiException("Terms are mandatory for search, please specify and try again.");
        }
    }
}
