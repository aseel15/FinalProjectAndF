package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.Room;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    String dateCheckIn;
    String dateCheckOut;
    List<Room>roomsList;
    int roomNumber;
    TextView textTry;
    int days;
    SharedPreferences preferences;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       preferences=getSharedPreferences("session",MODE_PRIVATE);
        userId=preferences.getInt("login",-1);

        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();


        if (savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
        else {
            String item = intent.getStringExtra("roomNum");
            dateCheckIn = intent.getStringExtra("checkInDate");
            dateCheckOut = intent.getStringExtra("checkOutDate");
            roomNumber = Integer.parseInt(item);
            roomsList = (List<Room>) getIntent().getSerializableExtra("arrayList");
        }
        populateData(roomNumber);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("DateCheckIn",dateCheckIn);
        outState.putString("DateCheckOut",dateCheckOut);
        outState.putInt("roomNumber",roomNumber);
        outState.putInt("days",days);
        outState.putString("textTry",textTry.getText().toString());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        dateCheckIn=savedInstanceState.getString("DateCheckIn");
        dateCheckOut=savedInstanceState.getString("DateCheckOut");
        roomNumber=savedInstanceState.getInt("roomNumber");
        days=savedInstanceState.getInt("days");
        textTry.setText(savedInstanceState.getString("textTry"));

    }
    public Room getRoomObject(int objectNumber){
        for(int i=0;i<roomsList.size();i++)
            if(objectNumber==roomsList.get(i).getId())
                return roomsList.get(i);
        return null;
    }
    public void populateData(int roomNum){

        Room room=getRoomObject(roomNum);
        ListView listView=findViewById(R.id.listView);
        ArrayList<String> arr=new ArrayList<>();
        arr.add("Room Number : "+room.getId());
        arr.add("Room Type : "+room.getRoomType());
        arr.add("Price : "+room.getPrice()+"$");
        arr.add("Room Size : "+room.getRoomSize());
        arr.add("Bed Type : "+room.getBedType());
        arr.add("Room Status : "+room.getRoomStatus());
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        listView.setAdapter(arrayAdapter);


    }
    public String formatDateString(String date) {
        String[] split = date.split("-");
        String readyFormat = "";
        for (int i = split.length - 1; i >= 0; i--) {
            if (split[i].length() == 1)
                split[i] = "0" + split[i];

            readyFormat += split[i];
            if (i != 0)
                readyFormat += "-";
        }
        return readyFormat;
    }
    public Date formatDate(String date){
        String[]split=date.split("-");
        String readyFormat="";
        for(int i=split.length-1;i>=0;i--){
            if(split[i].length()==1)
                split[i]="0"+split[i];

            readyFormat+=split[i];
            if(i!=0)
                readyFormat+="-";
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date d1=null;
        try {
            d1 = sdf.parse(readyFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1;


    }
    public int calculateDays(){
        Date dateIn=formatDate(dateCheckIn);
        Date dateOut=formatDate(dateCheckOut);
        Long date1InMs = dateIn.getTime();
        int date1Min=date1InMs.intValue();
        Long date2InMs = dateOut.getTime();
        int date2Min=date2InMs.intValue();
        int timeDif=date2Min-date1Min;
        int day= (int) (timeDif / (1000 * 60 * 60* 24));
        return day;
    }
    public void postData(){
        String url="http://10.0.2.2:80/FinalProject/reserveRoom.php";
        days= calculateDays();

        String dateIn=formatDateString(dateCheckIn);
        String dateOut=formatDateString(dateCheckOut);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(DetailActivity.this,
                        response, Toast.LENGTH_LONG).show();
                //textTry.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textTry.setText(error.getMessage());
                Toast.makeText(DetailActivity.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Room room=getRoomObject(roomNumber);
                Map<String, String> params = new HashMap<>();


                params.put("roomsID", roomNumber+"");
                //by shared preference
                params.put("userId", String.valueOf(userId));
                params.put("check_In", String.valueOf(dateIn));
                params.put("check_Out",String.valueOf(dateOut));
                params.put("totalPrice",(days*room.getPrice())+"");

                return params;
            }
        };
        queue.add(request);
    }



    public void btnReserveOnClick(View view) {
        if(dateCheckIn.isEmpty()||dateCheckOut.isEmpty()){
            Toast.makeText(DetailActivity.this,
                    "You should enter check in & check out date or the date invalid", Toast.LENGTH_SHORT).show();
        }
        else{

            //call method to post it to database
            postData();
            Toast.makeText(DetailActivity.this,
                    "Room Reserved successfully", Toast.LENGTH_SHORT).show();
        }
    }
}