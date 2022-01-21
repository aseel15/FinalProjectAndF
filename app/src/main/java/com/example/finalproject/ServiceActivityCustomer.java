package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.ServiceItem;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceActivityCustomer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Spinner spinnerService;
    Spinner spinnerRoomId;
    ArrayList<ServiceItem>serviceItems;
    ArrayList<String>selectedServices;
    ListView selectedView;
    ArrayAdapter<String>arrayAdapter;
    ArrayList<String>roomIdList;
    ArrayAdapter<String>roomIdAdapter;
    String roomIdChosen;
    TextView textView;
    String n=String.valueOf(2);
    private String BASE_URL = "http://10.0.2.2:80/FinalProject/getRoomId.php?userId=" + n;
    private RequestQueue queue;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_customer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Services");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        queue = Volley.newRequestQueue(this);
        textView=findViewById(R.id.databa);
        spinnerService=findViewById(R.id.spinnerService);
        spinnerRoomId=findViewById(R.id.spinnerRoomId);
        serviceItems=getServiceItems();
        selectedView=findViewById(R.id.listViewServices);
        ServiceAdapter serviceAdapter=new ServiceAdapter(ServiceActivityCustomer.this,serviceItems);
        spinnerService.setAdapter(serviceAdapter);
        selectedServices=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,selectedServices);
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                ServiceItem serviceItem=(ServiceItem) parentView.getSelectedItem();
                if(!serviceItem.getNameService().equalsIgnoreCase("select")) {
                    selectedServices.add(serviceItem.getNameService());
                    arrayAdapter.notifyDataSetChanged();
                    selectedView.setAdapter(arrayAdapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        selectedView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceDeleted=adapterView.getItemAtPosition(i).toString();
                selectedServices.remove(i);
                arrayAdapter.notifyDataSetChanged();
                selectedView.setAdapter(arrayAdapter);
                return true;
            }

        });
        roomIdList=new ArrayList<>();
        getRoomId();
        roomIdAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,roomIdList);
        spinnerRoomId.setAdapter(roomIdAdapter);
        spinnerRoomId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String serviceItem=(String) parentView.getSelectedItem();
                if(!serviceItem.equalsIgnoreCase("select")) {
                    roomIdChosen=serviceItem;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private ArrayList<ServiceItem> getServiceItems() {
        serviceItems=new ArrayList<>();
        serviceItems.add(new ServiceItem("select",R.drawable.ic_toiletries));
        serviceItems.add(new ServiceItem("Beverage",R.drawable.beverage));
        serviceItems.add(new ServiceItem("Extra Toiletries", R.drawable.toiletries));
        serviceItems.add(new ServiceItem("Extra Towels",R.drawable.towels));
        serviceItems.add(new ServiceItem("Food",R.drawable.fastfood));
        serviceItems.add(new ServiceItem("Valet Parking",R.drawable.keys));
        serviceItems.add(new ServiceItem("House Keeping",R.drawable.housecleaning));

        return serviceItems;
    }
    public void getRoomId(){
        roomIdList.add("select");
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray roomList=new JSONArray(response);
                            for(int i=0;i<roomList.length();i++){
                                JSONObject jsonObject= roomList.getJSONObject(i);
                                int id = jsonObject.getInt("roomsID");
                                roomIdList.add(id+"");
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(ServiceActivityCustomer.this, error.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    });
        queue.add(request);
    }
    public void addServiceToTable(){
        String url="http://10.0.2.2:80/FinalProject/addServiceToEmployee.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ServiceActivityCustomer.this,
                                "Your Request Sent Successfully", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textTry.setText(error.getMessage());
                Toast.makeText(ServiceActivityCustomer.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                //ServiceFromTable service=new ServiceFromTable(userId,roomId,totalPrice);
                Map<String, String> params = new HashMap<>();
                //by shared preference

                params.put("roomId", roomIdChosen+"");
                params.put("userId", n+"");
                params.put("serviceName",selectedServices.toString());
                params.put("price", "6");
                return params;
            }
        };
        queue.add(request);
    }


    public void btnRequestClick(View view) {
        addServiceToTable();
       /* Intent intent= new Intent(this,AcceptServiceByEmployee.class);
        //get the id from the shared preference
         intent.putExtra("userId","1");
         intent.putExtra("roomId",roomIdChosen);
         intent.putExtra("userRequest",selectedServices);
         startActivity(intent);*/
        //add to the service table to appear in the employee screen



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home:
                intent=new Intent(ServiceActivityCustomer.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_services:
                intent=new Intent(ServiceActivityCustomer.this, ServiceActivityCustomer.class);
                startActivity(intent);
                break;
            case R.id.nav_trips:
                intent=new Intent(ServiceActivityCustomer.this, TripList.class);
                startActivity(intent);
                break;

            case R.id.nav_person:
                intent=new Intent(ServiceActivityCustomer.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_wedding:
                intent=new Intent(ServiceActivityCustomer.this, APIActivity.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}