package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.model.Trip;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


import java.util.ArrayList;

public class TripList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RequestQueue queue;
    private ListView lst;
    private String[] arr;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        queue = Volley.newRequestQueue(this);
        lst = findViewById(R.id.ListTrip);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trips");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        populateList();
    }


    public void populateList() {
        String url = "http://10.0.2.2:80/FinalProject/trip.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            int tripid, price;
            String name, description, date;

            Trip[] tripObj;

            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> trips = new ArrayList<>();
                tripObj = new Trip[response.length()];
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        trips.add(obj.getString("name"));
                        tripid = obj.getInt("id");
                        name = obj.getString("name");
                        description = obj.getString("description");
                        price = obj.getInt("price");
                        date = obj.getString("date");
                        tripObj[i] = new Trip(tripid, name, description, date, price);

                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }
                arr = new String[trips.size()];
                arr = trips.toArray(arr);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        TripList.this, android.R.layout.simple_list_item_1,
                        arr);
                lst.setAdapter(adapter);

                lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != -1) {
                            Gson gson = new Gson();
                            String tripString = gson.toJson(tripObj[position]);
                            Intent intent = new Intent(view.getContext(), detailTrip.class);
                            intent.putExtra("trip", tripString);
                            startActivity(intent);
                        }
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(TripList.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home:
                intent=new Intent(TripList.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_services:
                intent=new Intent(TripList.this, ServiceActivityCustomer.class);
                startActivity(intent);
                break;
            case R.id.nav_trips:
                intent=new Intent(TripList.this, TripList.class);
                startActivity(intent);
                break;

            case R.id.nav_person:
                intent=new Intent(TripList.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_wedding:
                intent=new Intent(TripList.this, APIActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent=new Intent(TripList.this, LogOut.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}