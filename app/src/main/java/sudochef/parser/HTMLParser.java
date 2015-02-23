package sudochef.parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import sudochef.inventory.Product;
import sudochef.yummly.Config;

/**
 * This class is responsible for fetching the html of a recipe from the source and extracting the recipe text from it
 */
public class HTMLParser {

    JSONObject recipeJSON;
    Document yummlyDoc;
    Document recipeDoc;
    List<String> stepList;

    HTMLParser(String id, String recipe) {
        // Get source URL from JSON
        String url = "";
        stepList = new LinkedList<>();

        try {
            recipeJSON = new JSONObject(recipe);
            url = recipeJSON.getJSONObject("source").getString("sourceRecipeUrl");
        } catch(Exception e) {
            Log.w("SC.HTMLParser", "JSON parser error");
        }

        // Build yummly URL
        String yummlyUrl = Config.YUMMLY_BASE_URL + id;

        Log.i("SC.HTMLParser", "Yummly URL: " + yummlyUrl);

        try {
            yummlyDoc = Jsoup.connect(yummlyUrl).get();
            recipeDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            Log.w("SC.HTMLParser", "Could not connect to get documents");
        }

        Log.i("SC.HTMLParser", "Parser initialized.");
    }


    List<Product> getIngredients(){
        return null;
    }

    /**
     * Extract the text of the directions from the recipe HTML
     */
    public void findDirections() {
        String source = "";
        try {
            source = recipeJSON.getJSONObject("source").getString("sourceDisplayName");
        } catch (JSONException e) {
            Log.w("SC.HTMLParser", "Source display name not found.");
            e.printStackTrace();
        }

        Log.i("SC.HTMLParser", "Extracting steps.");

        // Check out the source, and parse according to the formatting of that website
        try {
            if (source.equals("Williams-Sonoma")) {
                Log.i("SC.HTMLParser", "Williams Sonoma");

                Element directions = recipeDoc.select("div.directions").first();
                String tokens[] = directions.toString().split("<br>");
                for (String step : tokens) {
                    step = step.replaceAll("<.*?>", "");
                    step = step.trim();
                    if(step.isEmpty() || step.contains("Williams-Sonoma")) continue;
                    stepList.add(step);
                }
            } else if (source.equals("Simply Recipes")) {

                /*TODO: Remove numbers from steps */
                Log.i("SC.HTMLParser", "Simply Recipes");
                Element directions = recipeDoc.select("div.instructions").first().child(1);
                Elements steps = directions.getElementsByTag("p");
                for (Element step : steps) {
                    stepList.add(step.text());
                }
            } else if (source.equals("AllRecipes")) {
                Log.i("SC.HTMLParser", "AllRecipes");
                Element directions = recipeDoc.select("div.directions").first().child(1);
                Elements steps = directions.getElementsByTag("li");
                for(Element step : steps) {
                    String text = step.child(0).text();
                    stepList.add(text);
                }
            } else if (source.equals("Food52")) {
                Log.i("SC.HTMLParser", "Food52");
                Elements steps = recipeDoc.getElementsByAttributeValue("itemprop", "recipeInstructions");
                for(Element step : steps) {
                    String text = step.text().trim();
                    stepList.add(text);
                }
            } else if (source.equals("Food52")) {
                Log.i("SC.HTMLParser", "Food52");
                Elements steps = recipeDoc.getElementsByAttributeValue("itemprop", "recipeInstructions");
                for(Element step : steps) {
                    String text = step.text().trim();
                    stepList.add(text);
                }
            }

        } catch (Exception e) {
            Log.e("SC.HTMLParser", "HTML Parsing failed.");
            e.printStackTrace();
        }

        Log.i("SC.Parser", "Steps extracted.");
    }

    /**
     * @return a list of Strings where each string is a step in the directions
     */
    public List<String> getSteps() {
        return stepList;
    }
}
