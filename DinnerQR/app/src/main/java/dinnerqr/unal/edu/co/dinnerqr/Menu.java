package dinnerqr.unal.edu.co.dinnerqr;

public class Menu {
    private String name;

    //Menu_dish
    //Restaurant
    private MenuDish menuDish;
    private Restaurant restaurant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuDish getMenuDish() {
        return menuDish;
    }

    public void setMenuDish(MenuDish menuDish) {
        this.menuDish = menuDish;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
