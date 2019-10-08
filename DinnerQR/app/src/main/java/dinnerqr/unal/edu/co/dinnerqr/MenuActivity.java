package dinnerqr.unal.edu.co.dinnerqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private LinearLayout dishesListView;
    //private ArrayList<OrderedDish> orderedDishes;
    private HashMap<Dish, OrderedDish> orderedDishes;
    private ArrayList<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dishesListView = findViewById(R.id.dish_list);

        orderedDishes = new HashMap<>();

        dishes = new ArrayList<>();
        dishes.add(new Dish("Meat", 8.2, R.drawable.meat));
        dishes.add(new Dish("Beer", 7, R.drawable.club_colombia));
        dishes.add(new Dish("Shrimps", 35, R.drawable.shrimp));

        for (Dish dish : dishes){
            dishesListView.addView(getDishView(dish));
        }
    }

    private LinearLayout getDishView(final Dish dish){
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.layout(5,5,5,5);
        LinearLayout.LayoutParams params;

        //Icon
        ImageView icon = new ImageView(this);
        icon.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        icon.setImageResource(dish.getIcon());

        params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(5,5,5,5);

        //Dish name
        TextView dishName = new TextView(this);
        params.weight = 0;
        dishName.setLayoutParams(params);
        dishName.setTextColor(0xffffffff);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        dishName.setGravity(Gravity.LEFT);
        dishName.setText(dish.getName());

        //Price name
        TextView txtPrice = new TextView(this);
        params.weight = 1;
        txtPrice.setLayoutParams(params);
        txtPrice.setTextColor(0xffffffff);
        txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        txtPrice.setGravity(Gravity.CENTER);
        txtPrice.setText("$" + dish.getPrice());

        //Add button
        Button btnAdd = new Button(this);
        btnAdd.setLayoutParams(params);
        btnAdd.setText("+");
        btnAdd.setGravity(Gravity.CENTER);
        btnAdd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderedDish orderedDish = null;
                if (!orderedDishes.containsKey(dish))
                {
                    orderedDish = new OrderedDish(null, dish);

                } else {
                    orderedDish = orderedDishes.get(dish);
                }

                if(orderedDish != null) orderedDish.addOrder();
                orderedDishes.put(dish, orderedDish);

                if(orderedDish != null){
                    Toast.makeText(MenuActivity.this, orderedDish.getQuantity() + " " + dish.getName() + "(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        item.addView(icon);
        item.addView(dishName);
        item.addView(txtPrice);
        item.addView(btnAdd);

        return item;
    }
}
