package sudochef.inventory;

public class Product {
    public Product(String string, double amt, Units u, ProductTime d) {
        name = string;
        amount = amt;
        amountUnit = u;
        date = d;
    }
    public Product(String n, String gen, double amt, Units u, ProductTime d) {
        name = n;
        generalName = gen;
        amount = amt;
        amountUnit = u;
        date = d;
    }
    public String name;
    public String generalName;
    public double amount;
    public Units amountUnit;
    public ProductTime date;
}
