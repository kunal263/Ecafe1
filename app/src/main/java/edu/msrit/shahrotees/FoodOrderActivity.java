package edu.msrit.shahrotees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodOrderActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static ArrayList<FoodItem> mealsList = new ArrayList<>();
    public static ArrayList<FoodItem> drinksList = new ArrayList<>();
    public ProgressDialog dialog;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        };


        Button fab = (Button) findViewById(R.id.fab);
        int unicode = 0x1F525;
        String s = new String(Character.toChars(unicode));
        fab.setText("Place Order "+s);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<FoodItem> orderList = new ArrayList<>();

                try {
                    for (int i = 0; i < mSectionsPagerAdapter.mealTab.adapter.getCount(); i++) {
                        FoodItem item = mSectionsPagerAdapter.mealTab.adapter.getItem(i);
                        if(Integer.parseInt(item.getQuantity()) > 0) {
                            orderList.add(item);
                        }
                    }

                    for (int i = 0; i < mSectionsPagerAdapter.drinksTab.adapter.getCount(); i++) {
                        FoodItem item = mSectionsPagerAdapter.drinksTab.adapter.getItem(i);
                        if(Integer.parseInt(item.getQuantity()) > 0) {
                            orderList.add(item);
                        }
                    }

                    if(orderList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Add something to the cart first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int orderT = 0;
                    int quantityT = 0;
                    for (FoodItem item : orderList) {
                        orderT = orderT + Integer.parseInt(item.getQuantity()) * Integer.parseInt(item.getPrice());
                        quantityT = quantityT + 1;
                    }
                    String orderTotal = ""+orderT;
                    String quantityTotal = ""+quantityT;
                    Log.d("q", quantityTotal);

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();

                    OrderItem orderItem = new OrderItem(null, orderTotal, ts, quantityTotal, orderList, null);

                    HashMap<String, String> hashMap = orderItem.getStringHashmap();

                    Intent intent = new Intent(getApplication(), OrderSubmit.class);
                    intent.putExtra("time", hashMap.get("time"));
                    intent.putExtra("orderno", hashMap.get("orderno"));
                    intent.putExtra("totalPrice", hashMap.get("totalPrice"));
                    intent.putExtra("totalQuantity", hashMap.get("totalQuantity"));

                    for(Integer i = 0; i<Integer.parseInt(quantityTotal); i++) {
                        intent.putExtra(i.toString(), hashMap.get(i.toString()));
                        Log.d("fuck", i.toString() + " " + hashMap.get(i.toString()));
                    }

                    startActivity(intent);

                }
                catch (Exception nikhil) {
                    nikhil.printStackTrace();
                    System.out.println("Catch nikhil. The nigger stole your order and phone. And basically everything else");
                }

            }
        });

        myRef.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mealsList.clear();
                drinksList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot ch : snapshot.getChildren()) {
                        FoodItem item = ch.getValue(FoodItem.class);
                        if (snapshot.getKey().equals("meals")) {
                            mealsList.add(item);
                        } else if (snapshot.getKey().equals("drinks")) {
                            drinksList.add(item);
                        }
                        else {
                            Log.e("Unidentified Food Item", "FML");
                        }
                    }
                }

                System.out.println(mealsList);
                System.out.println(drinksList);


                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                tabLayout.setupWithViewPager(mViewPager);

                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static ArrayList<FoodItem> getFoodItemList(String listName) {
        if (listName.equals("meals") || listName.equals("meal")) {
            return mealsList;
        }
        if (listName.equals("drink") || listName.equals("drinks")) {
            return drinksList;
        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_setting) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_order, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        MealTab mealTab;
        DrinksTab drinksTab;



        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {

                case 0:
                     mealTab = new MealTab();
                    return mealTab;

                case 1:
                    drinksTab = new DrinksTab();
                    return drinksTab;

            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Meals";
                case 1:
                    return "Drinks";

            }

            return null;
        }
    }
}
