package sudochef.search.yummly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser {
    String input;


    public ResponseParser(String input){
        this.input = input;
    }

    public Response ParseResponse() throws Exception {

        Response response = new Response();

        JSONObject obj = new JSONObject(input);
        //response.setAttribution(obj.getString("attribution"));
        response.setCount(obj.getInt("totalMatchCount"));

        JSONArray matchesArray = obj.getJSONArray("matches");
        RecipeResult[] matches = new RecipeResult[matchesArray.length()];
        for(int i = 0; i < matches.length; i++){
            JSONObject match = matchesArray.getJSONObject(i);

            matches[i] = new RecipeResult();
            matches[i].setId(match.getString("id"));
            try {
                matches[i].setThumbnail(match.getJSONArray("smallImageUrls").getString(0));
            } catch (JSONException e) {
                matches[i].setThumbnail("");
            }
            matches[i].setName(match.getString("recipeName"));
            matches[i].setRating(match.getInt("rating"));
            matches[i].setSource(match.getString("sourceDisplayName"));

            try {
                matches[i].setTime(match.getInt("totalTimeInSeconds"));
            } catch (JSONException e) {
                matches[i].setTime(0);
            }

            // Extract ingredients array
            JSONArray ingredientsArray = match.getJSONArray("ingredients");
            String[] ingredients = new String[ingredientsArray.length()];
            for(int j = 0; j < ingredients.length; j++)
                ingredients[j] = ingredientsArray.getString(j);
            matches[i].setIngredients(ingredients);
        }
        response.setMatches(matches);

        return response;
    }
}
