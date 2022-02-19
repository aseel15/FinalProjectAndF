package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class addPersonAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText edtUserName, edtEmail, edtPassword;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        edtUserName = findViewById(R.id.UserName);
        edtEmail = findViewById(R.id.editTextTextEmailAddress);
        edtPassword = findViewById(R.id.Password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert Customer");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    public void btnClkRegister(View view) {
        String UserName = edtUserName.getText().toString();
        String Email = edtEmail.getText().toString();
        String Password = edtPassword.getText().toString();

        if (UserName.isEmpty())
            Toast.makeText(addPersonAdmin.this, ("Please fill username field"), Toast.LENGTH_SHORT).show();

        else if (Email.isEmpty())
            Toast.makeText(addPersonAdmin.this, ("Please fill email field"), Toast.LENGTH_SHORT).show();

        else if (Password.isEmpty())
            Toast.makeText(addPersonAdmin.this, ("Please fill password field"), Toast.LENGTH_SHORT).show();

        else addPerson(UserName, Email, Password);
    }

    private void addPerson(String UserName, String Email, String Password) {
        String url = "http://10.0.2.2:80/FinalProject/AddUsers.php";
        RequestQueue queue = Volley.newRequestQueue(addPersonAdmin.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(addPersonAdmin.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(addPersonAdmin.this,
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

                params.put("username", UserName);
                params.put("email", Email);
                params.put("password", Password);

                return params;
            }
        };
        queue.add(request);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home_a:
                intent=new Intent(addPersonAdmin.this, EmployeeResRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_addroom_a:
                intent=new Intent(addPersonAdmin.this, AddRoom.class);
                startActivity(intent);
                break;
            case R.id.nav_services_a:
                intent=new Intent(addPersonAdmin.this, AcceptServiceByEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_trips_a:
                intent=new Intent(addPersonAdmin.this, ControlTripAdmin.class);
                startActivity(intent);
                break;

            case R.id.nav_persons_a:
                intent=new Intent(addPersonAdmin.this, AllUser.class);
                startActivity(intent);
                break;

            case R.id.nav_add_Person_a:
                intent=new Intent(addPersonAdmin.this, addPersonAdmin.class);
                startActivity(intent);
                break;
            case R.id.nav_logout_a:
                intent=new Intent(addPersonAdmin.this, LogOut.class);
                startActivity(intent);
                break;
            case R.id.nav_parties_a:
                intent=new Intent(addPersonAdmin.this, PlacesEmployeeView.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}