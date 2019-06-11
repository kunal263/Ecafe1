package edu.msrit.shahrotees;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<FoodItem>{
    private ArrayList<FoodItem> mFoodItems;
    private Context mContext;

    public FoodAdapter(@NonNull Context context, @NonNull ArrayList<FoodItem> foodItems) {
        super(context, 0, foodItems);
        this.mContext = context;
        this.mFoodItems = foodItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FoodItem item = mFoodItems.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false);
        }

        TextView itemName = convertView.findViewById(R.id.lblListItemName);
        TextView itemPrice = convertView.findViewById(R.id.lblListItemPrice);
        final TextView Quantity = convertView.findViewById(R.id.quantity);
        Quantity.setText(item.getQuantity());
        itemName.setText(item.getName());
        itemPrice.setText("Rs "+item.getPrice());
        Button negButton = convertView.findViewById(R.id.btn_neg);
        Button posButton = convertView.findViewById(R.id.btn_pos);
        negButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(Quantity.getText().toString());
                if (i == 0) {
                    return;
                }
                else {
                    i = i-1;
                    Quantity.setText(""+i);
                    item.quantity = ""+i;
                }
            }
        });

        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(Quantity.getText().toString());
                if (i == 10) {
                    return;
                }
                else {
                    i = i+1;
                    Quantity.setText(""+i);
                    item.quantity = ""+i;
                }
            }
        });

        return convertView;
    }
}
