package edu.msrit.shahrotees;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItem {

    OrderItem() {
        //Do Nothing
    }

    public String orderNumber, totalPrice, time, totalQuantity, paymentMode;
    ArrayList<FoodItem> orderList;

    public OrderItem(String orderNumber, String totalPrice, String time, String totalQuantity, ArrayList<FoodItem> orderList, String paymentMode) {
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.time = time;
        this.orderList = orderList;
        this.totalQuantity = totalQuantity;
        this.paymentMode = paymentMode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<FoodItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<FoodItem> orderList) {
        this.orderList = orderList;
    }

    public HashMap<String, String> getStringHashmap() {
        HashMap<String, String> result = new HashMap<>();

        result.put("time", time);
        result.put("orderno", orderNumber);
        result.put("totalPrice", totalPrice);
        result.put("totalQuantity", totalQuantity);


        Integer i = 0;
        for(FoodItem item : orderList) {
            String foodItem = item.getName() + "/" + item.getPrice() + "/" + item.getQuantity();
            result.put(i.toString(), foodItem);
            i++;
        }



        return result;
    }

}