package edu.msrit.shahrotees;

import android.os.Parcel;
import android.os.Parcelable;


public class FoodItem implements Parcelable {
    public String name, price;
    String quantity = "0";

    public FoodItem(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    FoodItem () {
        //Do Nothing
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toString () {
        return "Name: " + name + " Price: " + price;
    }

    protected FoodItem(Parcel in) {
        quantity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
    }



    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}