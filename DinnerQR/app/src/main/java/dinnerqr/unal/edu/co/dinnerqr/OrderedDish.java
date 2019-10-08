package dinnerqr.unal.edu.co.dinnerqr;

public class OrderedDish {
    //Order-dish
    private Order order;
    private Dish dish;
    private int quantity;

    public OrderedDish(Order order, Dish dish){
        this.order = order;
        this.dish = dish;
        this.quantity = 0;
    }

    public void addOrder() {
        this.quantity++;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
