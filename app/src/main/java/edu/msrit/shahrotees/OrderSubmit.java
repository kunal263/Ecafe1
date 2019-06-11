package edu.msrit.shahrotees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderSubmit extends AppCompatActivity {

    String time, quantity, totalPrice;
    ArrayList<String> orderList;
    ArrayList<FoodItem> foodItemList;
    TextView total;
    ListView orderListView;
    public OrderAdapter adapter;
    Button payUp, imPoor;
    Intent orderIntent, intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submit);

        intent = new Intent(getApplicationContext(), PaymentPortal.class);
        total = findViewById(R.id.total_text);
        orderListView = findViewById(R.id.OrderList);
        payUp = findViewById(R.id.pay);
        imPoor = findViewById(R.id.cancel);

        orderList = new ArrayList<>();
        foodItemList = new ArrayList<>();

        orderIntent = getIntent();
        time = orderIntent.getExtras().get("time").toString();
        totalPrice = orderIntent.getExtras().get("totalPrice").toString();
        quantity = orderIntent.getExtras().get("totalQuantity").toString();

        for (Integer i = 0; i<Integer.parseInt(quantity); i++) {
            intent.putExtra(i.toString(), orderIntent.getExtras().get(i.toString()).toString());
            orderList.add(orderIntent.getExtras().get(i.toString()).toString());
            Log.d("TP", orderIntent.getExtras().get(i.toString()).toString());
            String[] arrSplit = orderIntent.getExtras().get(i.toString()).toString().split("/");
            foodItemList.add(new FoodItem(arrSplit[0], arrSplit[1], arrSplit[2]));
            //Log.d("Ghanta", "LOL");
        }

        adapter = new OrderAdapter(getApplicationContext(), foodItemList);
        orderListView.setDividerHeight(1);
        orderListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        Log.d("TP", orderList.toString());

        total.setText("Rs "+totalPrice);

        payUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("time", time);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("totalQuantity", quantity);
                startActivity(intent);
            }
        });

        imPoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });






    }
}
