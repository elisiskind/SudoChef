package sudochef.inventory;

/**
 * Created by LH-21 on 4/1/2015.
 */
public enum CommonFood {
    Apple,
    Banana,
    Milk,
    Mayonnaise,
    Margarine,
    Salt, //House
    Butter,
    Flour;

    public static String[] names() {
        CommonFood[] food = values();
        String[] names = new String[food.length];

        for (int i = 0; i < food.length; i++) {
            names[i] = food[i].name();
        }

        return names;
    }
}
