package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.example.finalproject.model.ServiceFromTable;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AcceptServiceByEmployee extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<ServiceFromTable>services=new ArrayList<>();;
    RecyclerView recyclerView;
    int id;
    int roomIdUser;
    private static final String BASE_URL = "http://10.0.2.2:80/FinalProject/getServiceFromEm.php";
    private RequestQueue queue;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_service_by_employee);
        queue = Volley.newRequestQueue(this);
        recyclerView=findViewById(R.id.service_recycler);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customers' Service");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getServices();

    }
    public void getServices(){
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ServicesList=new JSONArray(response);
                            for(int i=0;i<ServicesList.length();i++){
                                JSONObject jsonObject= ServicesList.getJSONObject(i);
                                int id= jsonObject.getInt("id");
                                int roomId= jsonObject.getInt("roomId");
                                int userId= jsonObject.getInt("userId");
                                String serviceName= jsonObject.getString("serviceName");
                                int price = jsonObject.getInt("price");
                                services.add(new ServiceFromTable(id,roomId,userId,serviceName,price));
                            }
                            populateServices();
                          /*  boolean flag=false;

                            for(int i=0;i<servicesFromTable.size();i++){
                                // serviceName.append("exi"+servicesFromTable.get(i).getRoomId());
                                if(id==servicesFromTable.get(i).getUserId())
                                    if(roomIdUser==servicesFromTable.get(i).getRoomId()) {
                                        int idService=servicesFromTable.get(i).getId();
                                        updateService(idService,servicesFromTable.get(i).getTotalPrice());
                                        //call updateService();
                                        serviceName.setText("enter to update");
                                        flag=true;
                                        break;
                                    }
                            }
                            if(!flag) {
                                addServices();
                                serviceName.setText("enter to add");
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(error.toString());
            }
        });
        queue.add(request);
    }
    public void populateServices(){
       /* TextView text=findViewById(R.id.textTT);
        text.setText("size = "+services.size());*/
        recyclerView.setLayoutManager(new LinearLayoutManager(AcceptServiceByEmployee.this));
        CaptionedServiceAdapter adapter = new CaptionedServiceAdapter(AcceptServiceByEmployee.this,services);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home_a:
                intent=new Intent(AcceptServiceByEmployee.this, EmployeeResRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addroom_a:
                intent=new Intent(AcceptServiceByEmployee.this, AddRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_services_a:
                intent=new Intent(AcceptServiceByEmployee.this, AcceptServiceByEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_trips_a:
                intent=new Intent(AcceptServiceByEmployee.this, ControlTripAdmin.class);
                startActivity(intent);
                break;

            case R.id.nav_persons_a:
                intent=new Intent(AcceptServiceByEmployee.this, AllUser.class);
                startActivity(intent);
                break;

            case R.id.nav_add_Person_a:
                intent=new Intent(AcceptServiceByEmployee.this, addPersonAdmin.class);
                startActivity(intent);
                break;

            case R.id.nav_logout_a:
                intent=new Intent(AcceptServiceByEmployee.this, LogOut.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}