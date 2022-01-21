package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    SharedPreferences preferences;
    private int user_id,totalPayment=0;
    private TextView edtUserID, edtUserName,edtRooms,edtParties,edtTrips,edtPayment,edtEmail;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        user_id=preferences.getInt("login",-1);
        edtUserName=findViewById(R.id.UserName);
        edtUserID=findViewById(R.id.UserId);
        edtRooms=findViewById(R.id.GivenRooms);
        edtParties=findViewById(R.id.GivenParties);
        edtPayment=findViewById(R.id.Payments);
        edtTrips=findViewById(R.id.GivenTrips);
        edtEmail=findViewById(R.id.Email);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(savedInstanceState!=null){

           onRestoreInstanceState(savedInstanceState);
        }
         else {
            edtUserID.setText("ID: " + String.valueOf(user_id));
            GetPersonalInfo();
            getData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("username",edtUserName.getText().toString());
        outState.putString("userid",edtUserID.getText().toString());
        outState.putString("rooms",edtRooms.getText().toString());
        outState.putString("parties",edtParties.getText().toString());
        outState.putString("payment",edtPayment.getText().toString());
        outState.putString("trips",edtTrips.getText().toString());
        outState.putString("email",edtEmail.getText().toString());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        edtUserName.setText(savedInstanceState.getString("username"));
        edtUserID.setText(savedInstanceState.getString("userid"));
        edtRooms.setText(savedInstanceState.getString("rooms"));
        edtParties.setText(savedInstanceState.getString("parties"));
        edtPayment.setText(savedInstanceState.getString("payment"));
        edtTrips.setText(savedInstanceState.getString("trips"));
        edtEmail.setText(savedInstanceState.getString("email"));
    }

    public void GetPersonalInfo(){
        String url = "http://10.0.2.2:80/FinalProject/Search.php?user_id="+user_id;
        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("error").equals("false")){

                        edtUserName.setText("Name: "+jsonObject.getString("username"));
                        edtEmail.setText("Email: "+jsonObject.getString("email"));

                }


                }   catch (JSONException e) {
                    e.printStackTrace();
                }

            }} ,new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();}});

        queue.add(request);

        }



    public void btnClkDeleteAccount(View view) {
        String url = "http://10.0.2.2:80/FinalProject/AddUsers.php";
        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    SharedPreferences remember = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = remember.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                    SharedPreferences.Editor editor2 = preferences.edit();
                    editor2.putInt("login", -1);
                    editor2.apply();

                    Intent intent =new Intent(Profile.this,SignIn.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", String.valueOf(user_id));
                return params;
            }
        };
        queue.add(request);
    }

    public void getData(){
        String url = "http://10.0.2.2:80/FinalProject/profile.php?user_id="+user_id;
        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getString("tripName").equals("false")){

                        edtTrips.setText(jsonObject.getString("tripName"));
                        totalPayment+=Integer.parseInt(jsonObject.getString("tripPrice"));
                    }else
                        edtTrips.setText("There is not reserved trips");

                    Toast.makeText(Profile.this,jsonObject.getString("roomName"),Toast.LENGTH_SHORT).show();
                    if (!jsonObject.getString("roomName").equals("false")){
                        edtRooms.setText(jsonObject.getString("roomName"));
                        totalPayment+=Integer.parseInt(jsonObject.getString("roomPrice"));
                    }else
                        edtRooms.setText("There is no reserved rooms");


                    if (!jsonObject.getString("servicePrice").equals("false")){
                        totalPayment+=Integer.parseInt(jsonObject.getString("servicePrice"));}


//                    if (!jsonObject.getString("partyName").equals("false")){
//                        edtParties.setText(jsonObject.getString("partyName"));
//                        totalPayment+=Integer.parseInt(jsonObject.getString("partyPrice"));
//                    }else
//                        edtRooms.setText("There is no reserved parties");


                    edtPayment.setText(String.valueOf(totalPayment));
                }   catch (JSONException e) {
                    e.printStackTrace();
                }

            }} ,new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();}});

        queue.add(request);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home:
                intent=new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_services:
                intent=new Intent(Profile.this, ServiceActivityCustomer.class);
                startActivity(intent);
                break;
            case R.id.nav_trips:
                intent=new Intent(Profile.this, TripList.class);
                startActivity(intent);
                break;

            case R.id.nav_person:
                intent=new Intent(Profile.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_wedding:
                intent=new Intent(Profile.this, APIActivity.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}