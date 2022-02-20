package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APIActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recycler;
    ArrayList<String>imagesList;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences preferences;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        user_id=preferences.getInt("login",-1);

        setContentView(R.layout.api_activity_main);
        recycler=findViewById(R.id.api_recycler);
        imagesList=new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wedding Plan");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
    //read from api
    //add url into array
    //pass array to captionedImg

    public void btnDecorateOnClick(View view) {
        //call volly
        imagesList=new ArrayList<>();
        String url ="https://api.unsplash.com/search/photos?client_id=a343tF8cTQJPm3NR2LZhMbZf0QUOLZ-MlOAw0GI2njQ&page=1&per_page=30&query=wedding%20decoration";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                           JSONArray jsonArray=response.getJSONArray("results");
                           int length=jsonArray.length();

                           for(int i=0;i<length;i++) {
                               JSONObject obj = jsonArray.getJSONObject(i);
                               String imageUrl = "";
                               JSONObject imgUrl =obj.getJSONObject("urls");
                               imageUrl = imgUrl.getString("small");
                               imagesList.add(imageUrl);
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        recycler.setLayoutManager(new LinearLayoutManager(APIActivity.this));
                        CaptionImageAPI adapter = new CaptionImageAPI(APIActivity.this, imagesList);
                        adapter.setType("Decor");
                        adapter.setUserId(user_id);
                        recycler.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error


                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void btnCakeOnClick(View view) {
        imagesList=new ArrayList<>();
        String url ="https://api.unsplash.com/search/photos?client_id=a343tF8cTQJPm3NR2LZhMbZf0QUOLZ-MlOAw0GI2njQ&page=1&per_page=30&query=wedding%20cake";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("results");
                            int length=jsonArray.length();

                            for(int i=0;i<length;i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String imageUrl = "";
                                JSONObject imgUrl =obj.getJSONObject("urls");
                                imageUrl = imgUrl.getString("small");
                                imagesList.add(imageUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        recycler.setLayoutManager(new LinearLayoutManager(APIActivity.this));
                        CaptionImageAPI adapter = new CaptionImageAPI(APIActivity.this, imagesList);
                        adapter.setType("Cake");
                        adapter.setUserId(user_id);

                        recycler.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error


                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void btnFoodOnClick(View view) {
        imagesList=new ArrayList<>();
        String url ="https://api.unsplash.com/search/photos?client_id=a343tF8cTQJPm3NR2LZhMbZf0QUOLZ-MlOAw0GI2njQ&page=1&per_page=60&query=wedding%20food";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("results");
                            int length=jsonArray.length();

                            for(int i=0;i<length;i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String imageUrl = "";
                                JSONObject imgUrl =obj.getJSONObject("urls");
                                imageUrl = imgUrl.getString("small");
                                imagesList.add(imageUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        recycler.setLayoutManager(new LinearLayoutManager(APIActivity.this));
                        CaptionImageAPI adapter = new CaptionImageAPI(APIActivity.this, imagesList);
                        adapter.setType("Food");
                        adapter.setUserId(user_id);

                        recycler.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error


                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home:
                intent=new Intent(APIActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_services:
                intent=new Intent(APIActivity.this, ServiceActivityCustomer.class);
                startActivity(intent);
                break;
            case R.id.nav_trips:
                intent=new Intent(APIActivity.this, TripList.class);
                startActivity(intent);
                break;

            case R.id.nav_person:
                intent=new Intent(APIActivity.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_wedding:
                intent=new Intent(APIActivity.this, APIActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent=new Intent(APIActivity.this, LogOut.class);
                startActivity(intent);
                break;
            case R.id.nav_parties:
                intent=new Intent(APIActivity.this, PlaceActivityView.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}