package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class AddRoom extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Spinner spinRoomType, spinBedType, spinNumOfBeds;
    private EditText edtSize,edtPrice;
    int req_Code=1;
    String urlImage="";
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        spinBedType=findViewById(R.id.bedTypeAdded);
        spinRoomType=findViewById(R.id.roomTypeAdded);
        spinNumOfBeds=findViewById(R.id.numOfBAdded);
        edtSize=findViewById(R.id.roomSizeAddedn);
        edtPrice=findViewById(R.id.PriceAdded);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert Room");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState!=null)
            onRestoreInstanceState(savedInstanceState);

       // populateImages();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("ImageUrlSaved",urlImage);

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        urlImage=savedInstanceState.getString("ImageUrlSaved");
    }


    public void btnClkAddRoom(View view) {
        String priceTxt=edtPrice.getText().toString();
        String sizeTxt=edtSize.getText().toString();
        String roomType=spinRoomType.getSelectedItem().toString();
        String bedType=spinBedType.getSelectedItem().toString();
        if(roomType.equalsIgnoreCase("select type"))
            Toast.makeText(AddRoom.this, ("Please choose the room type"), Toast.LENGTH_SHORT).show();
        else if(bedType.equalsIgnoreCase("select type"))
            Toast.makeText(AddRoom.this, ("Please choose the bed type"), Toast.LENGTH_SHORT).show();
        else if(priceTxt.isEmpty())
            Toast.makeText(AddRoom.this, ("Please enter the price"), Toast.LENGTH_SHORT).show();
        else if(sizeTxt.isEmpty())
            Toast.makeText(AddRoom.this, ("Please enter the size"), Toast.LENGTH_SHORT).show();
        else if(urlImage.isEmpty())
            Toast.makeText(AddRoom.this, ("Please choose the image "), Toast.LENGTH_SHORT).show();
        else {
            //call database method
            AddRoomToDB();

        }


    }
    public void AddRoomToDB(){
        String url="http://10.0.2.2:80/FinalProject/addRoomToDB.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //textTry.setText(response);
                        Toast.makeText(AddRoom.this,
                                response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddRoom.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();


                params.put("price", edtPrice.getText().toString());
                params.put("imageName", urlImage);
                params.put("roomType", spinRoomType.getSelectedItem().toString());
                params.put("bedType",spinBedType.getSelectedItem().toString());
                params.put("numOfBeds",spinNumOfBeds.getSelectedItem().toString());
                params.put("roomSize",edtSize.getText().toString());

                return params;
            }
        };
        queue.add(request);
    }

    public void btnImgOnClick(View view) {
        startActivityForResult(new Intent(this, ImageHotelActivity.class), req_Code);
    }
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == req_Code) {
            if (resultCode == RESULT_OK) {
                urlImage = data.getData().toString();

            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home_a:
                intent=new Intent(AddRoom.this, EmployeeResRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addroom_a:
                intent=new Intent(AddRoom.this, AddRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_services_a:
                intent=new Intent(AddRoom.this, AcceptServiceByEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_trips_a:
                intent=new Intent(AddRoom.this, TripList.class);
                startActivity(intent);
                break;

            case R.id.nav_persons_a:
                intent=new Intent(AddRoom.this, AllUser.class);
                startActivity(intent);
                break;

            case R.id.nav_add_Person_a:
                intent=new Intent(AddRoom.this, addPersonAdmin.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}