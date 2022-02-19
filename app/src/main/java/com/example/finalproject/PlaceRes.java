package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.Place;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlaceRes extends AppCompatActivity {

    private RequestQueue queue1;
    private static final String BASE_URL = "http://10.0.2.2:80/FinalProject/addPlace.php";

    Button checkIn;
    Button checkOut;
    EditText numPeople;
    Button confirm;

    TextView tvoutdate, tvindate,tvAdults;

    String date1,date2;
    int placeID;

    int year2, year3;
    int month2, month3;
    int day2, day3;
    //List<PlaceReservation> placeList;
    List<Place> placeList;
    SharedPreferences preferences;
    int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userId=preferences.getInt("login",-1);

        Log.d("userIDdd", userId+"");


        setContentView(R.layout.reservation_details);

        checkIn=findViewById(R.id.checkInDateButton);
        checkOut=findViewById(R.id.checkOutDateButton);
        numPeople=findViewById(R.id.edtNumOfPeople);
        confirm=findViewById(R.id.confirmPlaceButton);
        tvoutdate=findViewById(R.id.tvoutdate);
        tvindate=findViewById(R.id.tvindate);
        tvAdults=findViewById(R.id.tvAdults);

        //
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCalendar1();

            }
        });


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCalendar2();

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               confirmReservation();
                if(date1.isEmpty()||date2.isEmpty()){
                    Toast.makeText(PlaceRes.this,
                            "You should enter check in & check out date or the date invalid", Toast.LENGTH_SHORT).show();
                }
                else{
                    insertPlace();
                    Toast.makeText(PlaceRes.this,
                            "place Reserved successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

           Intent intent = getIntent();
            String item = intent.getStringExtra("placeId");
          // numPeople.setText(item);

            placeID = Integer.parseInt(item);




    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("check_In",date1);
        outState.putString("check_Out",date2);
        outState.putInt("placeId",placeID);
     //   outState.putInt("numpeople",numPeople);

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        date1=savedInstanceState.getString("check_In");
        date2=savedInstanceState.getString("check_Out");
        placeID=savedInstanceState.getInt("placeId");
      //  days=savedInstanceState.getInt("days");
       // textTry.setText(savedInstanceState.getString("textTry"));

    }*/


    private void confirmReservation() {
        tvoutdate.setText(date1);
        tvindate.setText(date2);
        tvAdults.setText("Number of People: "+numPeople.getText().toString());
    }

    private void loadCalendar2() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date1 =  month + "/" + dayOfMonth + "/" + year;
                month3 = month;
                day3 = dayOfMonth;
                year3 = year;
              //  tvoutdate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void loadCalendar1() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date2 =  month + "/" + dayOfMonth + "/" + year;
                month2 = month;
                day2 = dayOfMonth;
                year2 = year;
            //    tvindate.setText(date);
            }

        }, year, month, day);
        datePickerDialog.show();
    }


    public void insertPlace(){
        String url="http://10.0.2.2:80/FinalProject/placeReserve.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("myTakmjnjnjg", " posttttttttt");
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PlaceRes.this,
                                response, Toast.LENGTH_LONG).show();
                        Log.d("myTakmjnjnjg", " 111111111111111");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceRes.this,
                        "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                Log.d("myTakmjnjnjg", " 222222222222222222");
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("userId", String.valueOf(userId));
                params.put("placeID", placeID+"");
                params.put("check_In", date1);
                params.put("check_Out",date2);
                params.put("numpeople", String.valueOf(numPeople.getText().toString()));

                return params;
            }
        };
        queue.add(request);
    }


}
