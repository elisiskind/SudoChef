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

    public boolean remove(Product p) {
        if (p.name.equals(this.name) && this.hasEnough(p.amount, p.amountUnit)) {
            this.amount -= Units.convert(p.amountUnit, this.amountUnit, p.amount);
            return true;
        }
        return false;
    }

    public void add(Product p) {
        if (p.name.equals(this.name)) {
            this.amount += Units.convert(p.amountUnit, this.amountUnit, p.amount);
        }
    }

    public boolean hasEnough(double amt, Units u) {
        return this.amount >= Units.convert(u, this.amountUnit, amt);
    }

    public String name;
    public String generalName;
    public double amount;
    public Units amountUnit;
    public ProductTime date;
}
