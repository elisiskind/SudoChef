package sudochef.inventory.shopping;

import sudochef.inventory.Product;
import sudochef.inventory.Units;

/**
 * Created by eli on 4/14/2015.
 */
public class ShoppingProduct {

    private boolean checked;
    private String name;
    private Units unit;
    private Double amount;
    private String recipeID;
    private String recipeName;

    public ShoppingProduct(boolean checked, String name, Units unit, Double amount, String recipeID, String recipeName) {
        this.checked = checked;
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.recipeID = recipeID;
        this.recipeName = recipeName;
    }

    public ShoppingProduct(Product p, String id, String recipeName) {
        this.checked = false;
        this.name = p.generalName;
        this.unit = p.amountUnit;
        this.amount = p.amount;
        this.recipeID = id;
        this.recipeName = recipeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Units getUnit() {
        return unit;
    }

    public Double getAmount() {
        return amount;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

}
