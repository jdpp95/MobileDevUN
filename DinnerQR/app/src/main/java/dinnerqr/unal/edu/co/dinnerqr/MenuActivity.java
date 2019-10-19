package dinnerqr.unal.edu.co.dinnerqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout dishesListView;
    //private ArrayList<OrderedDish> mOrderedDishes;
    private HashMap<Dish, OrderedDish> mOrderedDishes;
    private ArrayList<Dish> mDishes;
    private TextView mTitle;
    private Button mBtnSeeOrder;
    private LinearLayout mOrderedDishesList;
    private Button mBtnBack;
    private Button mBtnSendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dishesListView = findViewById(R.id.dishList);
        mTitle = findViewById(R.id.menuTitle);
        mBtnSeeOrder = findViewById(R.id.btnSeeOrder);
        mOrderedDishesList = findViewById(R.id.orderedDishesList);
        mBtnBack = findViewById(R.id.backToMenu);
        mBtnSendOrder = findViewById(R.id.sendOrder);

        mOrderedDishes = new HashMap<>();

        mDishes = new ArrayList<>();
        mDishes.add(new Dish("Meat", 8.2, R.drawable.meat));
        mDishes.add(new Dish("Beer", 7, R.drawable.club_colombia));
        mDishes.add(new Dish("Shrimps", 35, R.drawable.shrimp));

        for (Dish dish : mDishes){
            dishesListView.addView(getDishView(dish, false));
        }
    }

    private LinearLayout getDishView(final Dish dish, boolean belongsToOrder){
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
        dishName.setGravity(Gravity.CENTER_VERTICAL);
        dishName.setText(dish.getName());

        //Quantity
        TextView txtQuantity = new TextView(this);
        params.weight = 1;
        txtQuantity.setLayoutParams(params);
        txtQuantity.setTextColor(0xffffffff);
        txtQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        txtQuantity.setGravity(Gravity.CENTER);
        OrderedDish orderedDish = mOrderedDishes.get(dish);
        if(orderedDish != null) txtQuantity.setText(String.valueOf(orderedDish.getQuantity()));
        else txtQuantity.setText("0");

        //Price name
        TextView txtPrice = new TextView(this);
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
                if (!mOrderedDishes.containsKey(dish))
                {
                    orderedDish = new OrderedDish(null, dish);

                } else {
                    orderedDish = mOrderedDishes.get(dish);
                }

                if(orderedDish != null) orderedDish.addOrder();
                mOrderedDishes.put(dish, orderedDish);

                if(orderedDish != null){
                    Toast.makeText(MenuActivity.this, orderedDish.getQuantity() + " " + dish.getName() + "(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        item.addView(icon);
        item.addView(dishName);
        if(belongsToOrder) item.addView(txtQuantity);
        item.addView(txtPrice);
        if(!belongsToOrder) item.addView(btnAdd);

        return item;
    }

    public ArrayList<OrderedDish> getOrderedDishesList(){
        return new ArrayList<>(this.mOrderedDishes.values());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSeeOrder:
                mTitle.setText(R.string.order_summary);
                dishesListView.setVisibility(View.GONE);
                mBtnSeeOrder.setVisibility(View.GONE);
                mOrderedDishesList.setVisibility(View.VISIBLE);
                mBtnBack.setVisibility(View.VISIBLE);
                mBtnSendOrder.setVisibility(View.VISIBLE);
                mOrderedDishesList.removeAllViews();
                for (OrderedDish orderedDish: this.getOrderedDishesList()){
                    Dish dish = orderedDish.getDish();
                    if(orderedDish.getQuantity() > 0)
                        mOrderedDishesList.addView(getDishView(dish, true));
                }
                mOrderedDishesList.addView(getTotalView());
                break;
            case R.id.backToMenu:
                mTitle.setText(R.string.today_menu);
                dishesListView.setVisibility(View.VISIBLE);
                mBtnSeeOrder.setVisibility(View.VISIBLE);
                mOrderedDishesList.setVisibility(View.GONE);
                mBtnBack.setVisibility(View.GONE);
                mBtnSendOrder.setVisibility(View.GONE);
                break;
            case R.id.sendOrder:
                Toast.makeText(MenuActivity.this, R.string.order_in_progress, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private View getTotalView() {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.layout(5,5,5,5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(5,5,5,5);

        //Dish name
        TextView dishName = new TextView(this);
        params.weight = 0;
        dishName.setLayoutParams(params);
        dishName.setTextColor(0xffffffff);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        dishName.setGravity(Gravity.CENTER_VERTICAL);
        dishName.setText(R.string.total);

        //Price name
        TextView txtPrice = new TextView(this);
        params.weight = 1;
        txtPrice.setLayoutParams(params);
        txtPrice.setTextColor(0xffffffff);
        txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        txtPrice.setGravity(Gravity.CENTER);

        double totalPrice = 0;
        for(OrderedDish orderedDish : this.getOrderedDishesList())
            totalPrice += orderedDish.getQuantity() * orderedDish.getDish().getPrice();

        txtPrice.setText("$" + totalPrice);

        item.addView(dishName);
        item.addView(txtPrice);
        return item;
    }
}
