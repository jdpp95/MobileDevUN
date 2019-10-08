package dinnerqr.unal.edu.co.dinnerqr;

public class Dish {
    //Ordered_dish, Menu_dish
    //public OrderedDish orderedDish;
    //public MenuDish menuDish;
    private String name;
    private String type;
    private double price;
    private int icon;

    public Dish(String name, double price, int icon){
        this.name = name;
        this.price = price;
        this.setIcon(icon);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
