package sudochef.yummly;

import java.net.URLEncoder;

public class SearchCall {
    private final String BASE_URL = "http://api.yummly.com/v1/api/recipes?_app_id=";
    private final int MAX_RESULTS = 15;

    String query;
    int page = 0;

    public SearchCall() {
        page = 0;
    }

    public void nextPage() {
        page++;
    }

    public String buildString() {
        String url = Config.YUMMLY_SEARCH_BASE_URL + Config.APP_ID + "&_app_key=" + Config.APP_KEY + "&q=" + query;
        if (page > 0) {
            url += "&maxResult=" + MAX_RESULTS + "&start=" + String.valueOf(MAX_RESULTS * page);
        }
        return url;
    }

    public void setQuery(String query) throws Exception {
        this.query = URLEncoder.encode(query, "UTF-8");
    }

    public void addAllergy() {
		/* TODO add allergy searching */
    }
}
