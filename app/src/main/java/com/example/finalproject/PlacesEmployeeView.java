package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.Place;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesEmployeeView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private static final String URL_PRODUCTS = "http://10.0.2.2:80/FinalProject/place.php";

    private RequestQueue queue1;
    private static final String string_Url="http://10.0.2.2:80/FinalProject/getReservedPlaces.php";

    HashMap<Integer, PlaceReservation> placeHashMap=new HashMap<Integer, PlaceReservation>();

    List<Place> placeList;
    RecyclerView recyclerView;
    Toolbar toolbar;
    //TextView test;
    SharedPreferences preferences;
    int userId;

    Spinner placeSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_parties_e);
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userId=preferences.getInt("login",-1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("parties");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        queue1 = Volley.newRequestQueue(this);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.home_RecyclerView);
        recyclerView = (RecyclerView)findViewById(R.id.home_RecyclerView);

        placeList = new ArrayList<>();
        loadPlaces();
        getReservedPlaces();

    }

    //when we press "back" button while nav drawer is opened, we don't want to leave the activity immediately@Override
 /*  public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }*/

    private void loadPlaces() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject placeObject = array.getJSONObject(i);
                                int placeId = placeObject.getInt("id");
                                String name = placeObject.getString("name");
                                String desc = placeObject.getString("description");

                                double price = placeObject.getInt("price");
                                String image = placeObject.getString("image");
                                Place place = new Place(placeId, name,desc, price, image);
                                placeList.add(place);

                                placeSpinner =findViewById(R.id.categoriesListSpinner);
                            }
                            //creating adapter object and setting it to recyclerview
                            recyclerView.setLayoutManager(new LinearLayoutManager(PlacesEmployeeView.this));
                            EmployeePlaceAdapter adapter = new EmployeePlaceAdapter(PlacesEmployeeView.this, placeList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void initSpinner(){


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home_a:
                intent=new Intent(PlacesEmployeeView.this, EmployeeResRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addroom_a:
                intent=new Intent(PlacesEmployeeView.this, AddRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addPlace_a:
                intent=new Intent(PlacesEmployeeView.this, addPlace.class);
                startActivity(intent);
                break;
            case R.id.nav_services_a:
                intent=new Intent(PlacesEmployeeView.this, AcceptServiceByEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_trips_a:
                intent=new Intent(PlacesEmployeeView.this, ControlTripAdmin.class);
                startActivity(intent);
                break;
            case R.id.nav_parties_a:
                intent=new Intent(PlacesEmployeeView.this, PlacesEmployeeView.class);
                startActivity(intent);
                break;
            case R.id.nav_persons_a:
                intent=new Intent(PlacesEmployeeView.this, AllUser.class);
                startActivity(intent);
                break;

            case R.id.nav_add_Person_a:
                intent=new Intent(PlacesEmployeeView.this, addPersonAdmin.class);
                startActivity(intent);
                break;

            case R.id.nav_logout_a:
                intent=new Intent(PlacesEmployeeView.this, LogOut.class);
                startActivity(intent);
     break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnSearchOnClick(View view) {
        String roomTypeTxt = placeSpinner.getSelectedItem().toString();
        ArrayList<Place> roomsFiltered = new ArrayList<>();

        if (!roomTypeTxt.equalsIgnoreCase("All")) {
            for (int i = 0; i < placeList.size(); i++) {
                if (placeList.get(i).getName().equalsIgnoreCase(roomTypeTxt)) {
                    roomsFiltered.add(placeList.get(i));
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(PlacesEmployeeView.this));
            EmployeePlaceAdapter adapter = new EmployeePlaceAdapter(PlacesEmployeeView.this, roomsFiltered);
            recyclerView.setAdapter(adapter);
        }
        else if (roomTypeTxt.equalsIgnoreCase("All")) {
            for (int i = 0; i < placeList.size(); i++) {
                roomsFiltered.add(placeList.get(i));

            }
            recyclerView.setLayoutManager(new LinearLayoutManager(PlacesEmployeeView.this));
            EmployeePlaceAdapter adapter = new EmployeePlaceAdapter(PlacesEmployeeView.this, roomsFiltered);
            recyclerView.setAdapter(adapter);
        }


    }

    public void getReservedPlaces(){

        StringRequest request=new StringRequest(Request.Method.GET, string_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray reservedRoomList=new JSONArray(response);
                            for(int i=0;i< reservedRoomList.length();i++){
                                JSONObject jsonObject= reservedRoomList.getJSONObject(i);
//                                int id= jsonObject.getInt("id");
                                int placeID= jsonObject.getInt("placeID");
//                                int userId= jsonObject.getInt("userId");
                                String check_In= jsonObject.getString("check_In");
                                String check_Out= jsonObject.getString("check_Out");
                                int numpeople=jsonObject.getInt("numpeople");

                                placeHashMap.put(placeID,new PlaceReservation(placeID,check_In,check_Out,numpeople));
                            }
                            Toast.makeText(PlacesEmployeeView.this, "tru",
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // checkIn.setText(response);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlacesEmployeeView.this, error.toString(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        queue1.add(request);
    }
}
