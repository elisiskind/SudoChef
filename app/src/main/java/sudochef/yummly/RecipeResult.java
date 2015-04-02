package sudochef.yummly;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * This class represents
 */
public class RecipeResult {
    private String[] courses;
    private String[] cuisines;
    private String[] holidays;

    private int rating;
    private String id;
    private String imageURL;
    private String source;
    private int time;
    private String[] ingredients;
    private String name;
    private Bitmap thumb;

    /**
     * @return the courses
     */
    public String[] getCourses() {
        return courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    /**
     * @return the cuisines
     */
    public String[] getCuisines() {
        return cuisines;
    }

    /**
     * @param cuisines the cuisines to set
     */
    public void setCuisines(String[] cuisines) {
        this.cuisines = cuisines;
    }

    /**
     * @return the holidays
     */
    public String[] getHolidays() {
        return holidays;
    }

    /**
     * @param holidays the holidays to set
     */
    public void setHolidays(String[] holidays) {
        this.holidays = holidays;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the imageURL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * @param imageURL the imageURL to set
     */
    public void setThumbnail(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the ingredients
     */
    public String[] getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public RecipeResult() {
        courses = null;
        cuisines = null;
        holidays = null;
        rating = -1;
        id = "";
        imageURL = "";
        source = "";
        time = 0;
        ingredients = null;
        name = "";
    }

    public void print(){
        System.out.println();
        System.out.println(name);
        System.out.print("\tIngredients:");
        for(int i = 0; i < this.ingredients.length; i++) {
            if(i > 0) System.out.print(",");
            System.out.print(" " + this.ingredients[i]);
        }
        System.out.println();
        System.out.print("\tRating: ");
        for(int i = 0; i < this.rating; i++) System.out.print("\u2605");
        System.out.println();
        System.out.println("\tID: " + this.id);
        System.out.println("\tImage URL: " + this.imageURL);
        System.out.println("\tTime: " + readableTime(this.time));
    }

    public String descriptionString() {
        String output = "Ingredients:";
        for(int i = 0; i < this.ingredients.length; i++) {
            if(i > 0) output += ",";
            output += " " + this.ingredients[i];
        }
        output += "\nRating: ";
        for(int i = 0; i < this.rating; i++) output += "\u2605";
        //output += "\nID: " + this.id;
        //output += "\n\tImage URL: " + this.imageURL;
        if(this.time > 0)
            output += "\nTime: " + readableTime(this.time);
        return output;
    }

    private String readableTime(int timeSeconds){

        int minutes = (int) ((timeSeconds / 60) % 60);
        int hours   = (int) ((timeSeconds / (60*60)) % 24);

        String output = "";

        if(hours > 0){
            String unit = (hours > 1) ? " hours" : " hour";
            output += hours + unit;
            if(minutes > 0) output += " and ";
        }
        if(minutes > 0){
            String unit = (minutes > 1) ? " minutes" : " minute";
            output += minutes + unit;
        }

        return output;
    }

    public void loadThumb() {
        HTTPGet httpGet = new HTTPGet();
        Log.d("SC.Search", "Starting HTTP GET for thumb.");
        thumb = httpGet.bitmapGET(imageURL);
    }

    public Bitmap getThumb() {
        return thumb;
    }
}
