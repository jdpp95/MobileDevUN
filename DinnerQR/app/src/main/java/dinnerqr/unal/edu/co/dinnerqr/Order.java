package dinnerqr.unal.edu.co.dinnerqr;

import java.util.LinkedList;

public class Order {
    //Client, table, comment, order_dish
    private Client client;
    private Table table;
    private Comment comment;
    private LinkedList<OrderedDish> orderedDishes;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LinkedList<OrderedDish> getOrderedDishes() {
        return orderedDishes;
    }

    public void setOrderedDishes(LinkedList<OrderedDish> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }
}
