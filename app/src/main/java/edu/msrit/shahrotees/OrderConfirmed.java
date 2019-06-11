package edu.msrit.shahrotees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class OrderConfirmed extends AppCompatActivity {

    Intent orderIntent;
    DatabaseReference myref;
    String time, orderNo, quantity, totalPrice, paymentMode;
    OrderItem myOrder;
    ListView orderListView;
    public OrderAdapter adapter;
    ArrayList<FoodItem> foodItemList;
    TextView priceText, timeText, orderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        orderListView = findViewById(R.id.confirmList);

        foodItemList = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference().child("orders");

        Random r = new Random();
        int d = r.nextInt(32000);
        String temp = "" + d;
        Long ono = 1000000 + Long.parseLong(temp);
        orderNo = "RIT"+ono;

        orderIntent = getIntent();



        priceText = findViewById(R.id.totalPrice);
        timeText = findViewById(R.id.time);
        orderText = findViewById(R.id.orderText);

        time = orderIntent.getExtras().get("time").toString();
        totalPrice = orderIntent.getExtras().get("totalPrice").toString();
        quantity = orderIntent.getExtras().get("totalQuantity").toString();
        paymentMode = orderIntent.getExtras().get("paymentMode").toString();


        try {

            String string = "\u20B9";
            byte[] utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            totalPrice = string + " " + totalPrice;
        } catch(Exception e){
            totalPrice = "Rs "+ totalPrice;
        }


        priceText.setText(totalPrice);
        orderText.setText("Order No. "+orderNo);
        timeText.setText(timeToText(time));

        for (Integer i = 0; i<Integer.parseInt(quantity); i++) {
            //orderList.add(orderIntent.getExtras().get(i.toString()).toString());
            Log.d("OC", orderIntent.getExtras().get(i.toString()).toString());
            String[] arrSplit = orderIntent.getExtras().get(i.toString()).toString().split("/");
            foodItemList.add(new FoodItem(arrSplit[0], arrSplit[1], arrSplit[2]));
        }

        adapter = new OrderAdapter(getApplicationContext(), foodItemList);
        orderListView.setDividerHeight(1);
        orderListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        myOrder = new OrderItem(orderNo, totalPrice, time, quantity, foodItemList, paymentMode);
        myref.child(orderNo).setValue(myOrder);


    }

    public String timeToText(String timeString){
        Long unixSeconds = Long.parseLong(timeString);
        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM d, yyyy '|' h:mm a");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+530"));
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

}
