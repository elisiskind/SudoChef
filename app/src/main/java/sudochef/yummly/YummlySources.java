package sudochef.yummly;

/**
 * Created by eli on 3/4/2015.
 */
public enum YummlySources {

    WilliamsSonoma("Williams-Sonoma"),
    SimplyRecipes("Simply Recipes"),
    AllRecipes("AllRecipes"),
    Food52("Food52"),
    SeriousEats("Serious Eats"),
    MyRecipes("MyRecipes");

    String displayName;

    private YummlySources(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public static String getAllowedString(){
        String allowed = "";

        for(YummlySources source: YummlySources.values()) {
            allowed += "|" + source.displayName;
        }
        return allowed;
    }

}