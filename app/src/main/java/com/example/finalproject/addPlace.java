package com.example.finalproject;

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

public class addPlace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Spinner spinPlaceType;
    private EditText edtdesc,edtPrice;
    int req_Code=1;
    String urlImage="";
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        spinPlaceType=findViewById(R.id.placeTypeSpinner);

        edtdesc=findViewById(R.id.placeDescT);
        edtPrice=findViewById(R.id.PriceAdded);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Place");
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
       super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
      super.onRestoreInstanceState(savedInstanceState);
        urlImage=savedInstanceState.getString("ImageUrlSaved");
    }


    public void btnClkAddPlace(View view) {
        String priceTxt=edtPrice.getText().toString();
        String descTxt=edtdesc.getText().toString();
        String placeType=spinPlaceType.getSelectedItem().toString();
        if(placeType.equalsIgnoreCase("select type"))
            Toast.makeText(addPlace.this, ("Please choose the place type"), Toast.LENGTH_SHORT).show();
        else if(priceTxt.isEmpty())
            Toast.makeText(addPlace.this, ("Please enter the price"), Toast.LENGTH_SHORT).show();
        else if(descTxt.isEmpty())
            Toast.makeText(addPlace.this, ("Please enter the Description"), Toast.LENGTH_SHORT).show();
        else {
            //call database method
            AddPlaceToDB();

        }


    }
    public void AddPlaceToDB(){
        String url="http://10.0.2.2:80/FinalProject/addPlace.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //textTry.setText(response);
                        Toast.makeText(addPlace.this,
                                response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(addPlace.this,
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
                params.put("name", spinPlaceType.getSelectedItem().toString());
                params.put("description",edtdesc.getText().toString());

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
                intent=new Intent(addPlace.this, EmployeeResRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addroom_a:
                intent=new Intent(addPlace.this, AddRoom.class);
                startActivity(intent);
                break;

            case R.id.nav_addPlace_a:
                intent=new Intent(addPlace.this, addPlace.class);
                startActivity(intent);
                break;
            case R.id.nav_parties_a:
                intent=new Intent(addPlace.this, PlacesEmployeeView.class);
                startActivity(intent);
                break;
            case R.id.nav_services_a:
                intent=new Intent(addPlace.this, AcceptServiceByEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_trips_a:
                intent=new Intent(addPlace.this, ControlTripAdmin.class);
                startActivity(intent);
                break;

            case R.id.nav_persons_a:
                intent=new Intent(addPlace.this, AllUser.class);
                startActivity(intent);
                break;

            case R.id.nav_add_Person_a:
                intent=new Intent(addPlace.this, addPersonAdmin.class);
                startActivity(intent);
                break;
            case R.id.nav_logout_a:
                intent=new Intent(addPlace.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}