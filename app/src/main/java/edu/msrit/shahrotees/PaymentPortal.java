package edu.msrit.shahrotees;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PaymentPortal extends AppCompatActivity {

    Button paymentProceed;
    RadioGroup rgp;
    RadioButton radioButton;
    Intent orderIntent, intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_portal);

        rgp = findViewById(R.id.rgp);

        orderIntent = getIntent();
        intent = new Intent(getApplicationContext(), OrderConfirmed.class);

        String time = orderIntent.getExtras().get("time").toString();
        String totalPrice = orderIntent.getExtras().get("totalPrice").toString();
        String quantity = orderIntent.getExtras().get("totalQuantity").toString();

        intent.putExtra("time", time);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("totalQuantity", quantity);

        for (Integer i = 0; i<Integer.parseInt(quantity); i++) {
            intent.putExtra(i.toString(), orderIntent.getExtras().get(i.toString()).toString());
        }

        paymentProceed = findViewById(R.id.order_button);
        paymentProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgp.getCheckedRadioButtonId();
                System.out.println(selectedId);

                if (selectedId == -1){
                    Toast.makeText(getApplicationContext(), "Please select something", Toast.LENGTH_SHORT).show();
                }
                else {
                    radioButton = (RadioButton) findViewById(selectedId);
                    String paymentMode = radioButton.getText().toString();
                    intent.putExtra("paymentMode", paymentMode);
                    startActivity(intent);
                }



            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Transaction Cancelled", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
