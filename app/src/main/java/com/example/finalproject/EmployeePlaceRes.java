package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeePlaceRes extends AppCompatActivity {

    private RequestQueue queue;
    private static final String BASE_URL = "http://10.0.2.2:80/FinalProject/getUserId.php";


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
    //SharedPreferences preferences;
    //int userId;

    ArrayList<Integer> userIds;
    EditText edtUserId;
    int idAdded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_reservation);
        queue=Volley.newRequestQueue(this);
        userIds=new ArrayList<>();

        checkIn=findViewById(R.id.checkInDateButton);
        checkOut=findViewById(R.id.checkOutDateButton);
        numPeople=findViewById(R.id.edtNumOfPeople);
        confirm=findViewById(R.id.confirmPlaceButton);

        edtUserId = findViewById(R.id.edtUserIdem);

//        tvoutdate=findViewById(R.id.tvoutdate);
//        tvindate=findViewById(R.id.tvindate);
//        tvAdults=findViewById(R.id.tvAdults);

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

                if(date1.isEmpty()||date2.isEmpty()){
                    Toast.makeText(EmployeePlaceRes.this,
                            "You should enter check in & check out date or the date invalid", Toast.LENGTH_SHORT).show();
                }
                else{
                   populateUser();
                    insertPlace();

                }

            }
        });

        Intent intent = getIntent();
        String item = intent.getStringExtra("placeId");
        placeID = Integer.parseInt(item);
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("check_In",date1);
        outState.putString("check_Out",date2);
        outState.putInt("placeId",placeID);
      outState.putInt("numpeople",numPeople);

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

        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EmployeePlaceRes.this,
                                response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployeePlaceRes.this,
                        "Fail to get response = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("userId", idAdded+"");
//                params.put("userId", String.valueOf(userId));
                params.put("placeID", placeID+"");
                params.put("check_In", date1);
                params.put("check_Out",date2);
                params.put("numpeople", String.valueOf(numPeople.getText().toString()));

                /*Log.d("palce id ", String.valueOf(placeID));
                Log.d("user id  ", String.valueOf(userId));

                Log.d("check in ", date1);
                Log.d("check out ", date2);
                Log.d("num of people ", String.valueOf(numPeople));*/
                return params;
            }
        };
        queue.add(request);
        Toast.makeText(EmployeePlaceRes.this,
                "place Reserved successfully", Toast.LENGTH_SHORT).show();
    }

    public void populateUser(){

        StringRequest request=new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray userIdList=new JSONArray(response);
                            for(int i=0;i< userIdList.length();i++){
                                JSONObject jsonObject= userIdList.getJSONObject(i);
                                int user= jsonObject.getInt("id");
                                userIds.add(user);
                            }
                            String s = String.valueOf(edtUserId.getText());
                            if(s.isEmpty())
                                Toast.makeText(EmployeePlaceRes.this, "Enter The User Id",
                                        Toast.LENGTH_SHORT).show();
                            else {
                                int uId = Integer.parseInt(s);
                                boolean flag = false;
                                for (int i = 0; i < userIds.size(); i++) {
                                    if (uId == userIds.get(i)) {
                                        idAdded = uId;
                                        flag = true;
                                        break;
                                    }
                                }
                                if (!flag) {
                                    Toast.makeText(EmployeePlaceRes.this,"there is no user has this id",
                                            Toast.LENGTH_SHORT).show();
                                    //add user
                                    Intent intent=new Intent(EmployeePlaceRes.this,addPersonAdmin.class);
                                    startActivity(intent);

                                }
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployeePlaceRes.this, error.toString(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);
    }
//    public void btnEnterOnClickEm(View view) {
//        populateUser();
//
//    }


}
