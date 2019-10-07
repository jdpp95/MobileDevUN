package dinnerqr.unal.edu.co.dinnerqr;

import java.util.ArrayList;

public class Restaurant {
    //Table, Menu
    private String name;
    public ArrayList<Table> tables;

    public Restaurant() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
