package sudochef.recipe.database;

/**
 * Created by eli on 4/13/2015.
 */
public class SavedRecipe {

    private String name;
    private String id;
    private String imageURL;

    public SavedRecipe(String name, String id, String imageURL) {
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }
}

