package sudochef.inventory;

/**
 * Created by LH-21 on 4/1/2015.
 */
public enum CommonFood {
    Apple("Apple"),
    BakingSoda("Baking Soda"),
    Banana("Banana"),
    Milk("Milk"),
    Mayonnaise("Mayonnaise"),
    Margarine("Shitty Butter"),
    Salt("Salt"), //House
    Butter("Butter"),
    Flour("Flour"),
    BakingPowder("Baking Powder"),
    CocoaPowder("Cocoa Powder");


    public static String[] names() {
        CommonFood[] food = values();
        String[] names = new String[food.length];

        for (int i = 0; i < food.length; i++) {
            names[i] = food[i].displayName();
        }

        return names;
    }

    String displayName;

    private CommonFood(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

}
