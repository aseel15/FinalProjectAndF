package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.Trip;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class detailTrip extends AppCompatActivity {
    private TextView edtName,edtDate,edtDesc,edtPrice;
    private EditText edtNumber;
    private String tripData;
    private Trip tripObj;
    private int user_id,tripID;
    private Button clkReserve;
    private Button clkDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        edtName=findViewById(R.id.name);
        edtDate=findViewById(R.id.date);
        edtDesc=findViewById(R.id.description);
        edtPrice=findViewById(R.id.price);
        edtNumber=findViewById(R.id.Number);
        clkReserve=findViewById(R.id.reserve);
        clkDelete=findViewById(R.id.Delete);

        if (savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
        else{
        SharedPreferences preferences=getSharedPreferences("session",MODE_PRIVATE);
        user_id=preferences.getInt("login",-1);
        Intent intent=getIntent();
        tripData=intent.getStringExtra("trip");
        Gson gson = new Gson();
        tripObj = gson.fromJson(tripData, Trip.class);
        tripID=tripObj.getId();
        checkReserved();
        fillTripData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("Name",edtName.getText().toString());
        outState.putString("Date",edtDate.getText().toString());
        outState.putString("Desc",edtDesc.getText().toString());
        outState.putString("price",edtPrice.getText().toString());
        outState.putString("Number",edtNumber.getText().toString());

        outState.putString("tripData",tripData);
        outState.putInt("user_id",user_id);
        outState.putInt("tripID",tripID);

        outState.putInt("invisibilityRes",clkReserve.getVisibility());
        outState.putInt("invisibilityDel",clkDelete.getVisibility());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        edtName.setText(savedInstanceState.getString("Name"));
        edtDate.setText(savedInstanceState.getString("Date"));
        edtDesc.setText(savedInstanceState.getString("Desc"));
        edtPrice.setText(savedInstanceState.getString("price"));
        edtNumber.setText(savedInstanceState.getString("Number"));
        tripData=savedInstanceState.getString("Data");
        user_id=savedInstanceState.getInt("user_id");
        tripID=savedInstanceState.getInt("tripID");
        clkReserve.setVisibility(savedInstanceState.getInt("invisibilityRes"));
        clkDelete.setVisibility(savedInstanceState.getInt("invisibilityDel"));

    }

    public void checkReserved(){
        String url = "http://10.0.2.2:80/FinalProject/SearchReservedTrip.php?user_id="+user_id+"&tripID="+tripID;
        RequestQueue queue = Volley.newRequestQueue(detailTrip.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("reserved").equals("true")){
                        clkDelete.setVisibility(View.VISIBLE);
                        clkReserve.setVisibility(View.INVISIBLE);
                    }
                    else {
                        clkDelete.setVisibility(View.INVISIBLE);
                        clkReserve.setVisibility(View.VISIBLE);
                    }

                }   catch (JSONException e) {
                    e.printStackTrace();
                }

            }} ,new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {Toast.makeText(detailTrip.this,
                    "Fail to get response = " + error, Toast.LENGTH_SHORT).show();}});

        queue.add(request);
    }


    public void fillTripData(){
      edtName.setText(tripObj.getName());
        edtDate.setText("Date of trip: "+tripObj.getDate());
       edtDesc.setText("About: \n\n"+tripObj.getDescription());
       edtPrice.setText("Price of trip: "+String.valueOf(tripObj.getPrice()));
    }
    public void btnClkReserve(View view) {
        if (edtNumber.getText().toString().isEmpty())
            Toast.makeText(detailTrip.this,"Please Enter Number of people",
                    Toast.LENGTH_SHORT).show();
        else{
        int Number=Integer.parseInt(edtNumber.getText().toString());
        int totalPrice=Number*tripObj.getPrice();
        addTrip(totalPrice);
        clkReserve.setVisibility(View.INVISIBLE);
        clkDelete.setVisibility(View.VISIBLE);}
    }

    private void addTrip(int totalPrice) {
        String url = "http://10.0.2.2:80/FinalProject/addTrip.php";
        RequestQueue queue = Volley.newRequestQueue(detailTrip.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(detailTrip.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(detailTrip.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.

                params.put("user_id", String.valueOf(user_id));
                params.put("tripID", String.valueOf(tripID));
                params.put("totalprice", String.valueOf(totalPrice));

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void btnClkDelete(View view) {
        Delete();
        clkReserve.setVisibility(View.VISIBLE);
        clkDelete.setVisibility(View.INVISIBLE);
    }

    public void Delete(){
            String url = "http://10.0.2.2:80/FinalProject/deleteTrip.php";
            RequestQueue queue = Volley.newRequestQueue(detailTrip.this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Toast.makeText(detailTrip.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    Toast.makeText(detailTrip.this,
                            "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    // as we are passing data in the form of url encoded
                    // so we are passing the content type below
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {


                    // below line we are creating a map for storing
                    // our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();

                    // on below line we are passing our
                    // key and value pair to our parameters.

                    params.put("user_id", String.valueOf(user_id));
                    params.put("tripID", String.valueOf(tripID));
                    // at last we are returning our params.
                    return params;
                }
            };
            // below line is to make
            // a json object request.
            queue.add(request);
        }

}