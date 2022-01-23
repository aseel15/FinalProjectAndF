package com.example.finalproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.model.ReservedRoom;
import com.example.finalproject.model.Room;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RequestQueue queue;
    private RequestQueue queue1;
    private static final String BASE_URL = "http://10.0.2.2:80/FinalProject/getRommsData.php";
    private static final String string_Url="http://10.0.2.2:80/FinalProject/getReservedRoom.php";
    RecyclerView recycler;
    Spinner spinRoomType;
    EditText checkIn;
    EditText checkOut;
    List<Room> rooms=new ArrayList<>();
    HashMap<Integer, ReservedRoom>reservedRoomHashMap=new HashMap<>();

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        recycler = findViewById(R.id.room_recycler);
        checkIn=findViewById(R.id.edtCheckIn);
        checkOut = findViewById(R.id.edtCheckOut);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rooms");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        queue = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        if (savedInstanceState!=null)
            onRestoreInstanceState(savedInstanceState);
        populateAllData();
        removeDeadLineCheckOut();
        populateReservedRooms();



    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("DateCheckIn",checkIn.getText().toString());
        outState.putString("DateCheckOut",checkOut.getText().toString());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        checkIn.setText(savedInstanceState.getString("DateCheckIn"));
        checkOut.setText(savedInstanceState.getString("DateCheckOut"));
    }
    public void populateAllData(){


        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray roomList=new JSONArray(response);
                            for(int i=0;i<roomList.length();i++){
                                JSONObject jsonObject= roomList.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String roomType = jsonObject.getString("roomType");
                                int price = jsonObject.getInt("price");
                                String bedType = jsonObject.getString("bedType");
                                int numOfBeds = jsonObject.getInt("numOfBeds");
                                String roomSize = jsonObject.getString("roomSize");
                                String imageName = jsonObject.getString("imageName");
                                Room room = new Room(id, roomType, price, bedType, numOfBeds, roomSize, imageName);
                                rooms.add(room);

                            }
                            String dateCheckIn=checkIn.getText().toString();
                            String dateCheckOut=checkOut.getText().toString();
                            recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            com.example.finalproject.CaptionedImageAdapter adapter = new com.example.finalproject.CaptionedImageAdapter(MainActivity.this,rooms,dateCheckIn,dateCheckOut);

                            recycler.setAdapter(adapter);



                            //Filter according to check & check out
                            ImageButton checkInButton= findViewById(R.id.checkInIc);
                            ImageButton checkOutButton= findViewById(R.id.checkOutIc);
                            selectDate(checkInButton,checkOutButton);

                            //Filter according to room type
                            spinRoomType =findViewById(R.id.spinRoom);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
    public void removeDeadLineCheckOut(){
        String url="http://10.0.2.2:80/RoomDataBase/deleteReservedRoom.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textTry.setText(error.getMessage());
                Toast.makeText(MainActivity.this,
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

                params.put("check_Out",date);
                return params;
            }
        };
        queue.add(request);

    }
    public void populateReservedRooms(){

        StringRequest request=new StringRequest(Request.Method.GET, string_Url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray reservedRoomList=new JSONArray(response);
                    for(int i=0;i< reservedRoomList.length();i++){
                        JSONObject jsonObject= reservedRoomList.getJSONObject(i);
                        int id= jsonObject.getInt("id");
                        int roomID= jsonObject.getInt("roomsID");
                        int userId= jsonObject.getInt("userId");
                        String check_In= jsonObject.getString("check_In");
                        String check_Out= jsonObject.getString("check_Out");
                        int totalPrice=jsonObject.getInt("totalPrice");
                        reservedRoomHashMap.put(roomID,new ReservedRoom(roomID,check_In,check_Out));
                    }
                    //checkIn.setText(response);
                } catch (JSONException e) {
                   // checkIn.setText(response);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        queue1.add(request);
    }



    public void selectDate(ImageButton checkInButton, ImageButton checkOutButton){
        DatePickerDialog.OnDateSetListener setListener;
        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        checkIn.setText(date);


                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        checkOut.setText(date);


                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
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


    public void btnNextOnClick(View view) {
        String checkInTxt=(checkIn.getText().toString()).replace("/","-");
        String checkOutTxt=(checkOut.getText().toString()).replace("/","-");
        String roomTypeTxt=spinRoomType.getSelectedItem().toString();
        Date inUserDate=formatDate(checkInTxt);
        Date outUserDate=formatDate(checkOutTxt);
        ArrayList<Room>roomsFiltered=new ArrayList<>();
       //checkIn.setText(String.valueOf(inUserDate));


        if(!roomTypeTxt.equalsIgnoreCase("select type")&&checkInTxt!=null&&checkOutTxt!=null) {

            if (inUserDate.compareTo(outUserDate) < 0) {
                for (int i = 0; i < rooms.size(); i++) {
                   // if(!reservedRoomHashMap.isEmpty())
                        if (reservedRoomHashMap.containsKey(rooms.get(i).getId())) {
                            ReservedRoom reservedRoom = reservedRoomHashMap.get(rooms.get(i).getId());
                            Date inReservedDate = formatDate(reservedRoom.getCheck_In());
                            Date outReservedDate = formatDate(reservedRoom.getCheck_Out());

                            if (inUserDate.compareTo(outReservedDate) > 0 || outUserDate.compareTo(inReservedDate) < 0) {
                               if(rooms.get(i).getRoomType().equalsIgnoreCase(roomTypeTxt)) {
                                   roomsFiltered.add(rooms.get(i));



                               }
                            }

                       }
                        else {
                            if(rooms.get(i).getRoomType().equalsIgnoreCase(roomTypeTxt))
                                 roomsFiltered.add(rooms.get(i));
                        }
                }

                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                com.example.finalproject.CaptionedImageAdapter adapter = new com.example.finalproject.CaptionedImageAdapter(MainActivity.this,roomsFiltered,checkInTxt,checkOutTxt);

                recycler.setAdapter(adapter);
            } else {

                Toast.makeText(MainActivity.this, "Invalid Date", Toast.LENGTH_SHORT);
            }
        }

        else if(checkInTxt!=null&&checkOutTxt!=null&&roomTypeTxt.equalsIgnoreCase("select type")){
            if (inUserDate.compareTo(outUserDate) < 0) {
                for (int i = 0; i < rooms.size(); i++) {
                  //  if(!reservedRoomHashMap.isEmpty())
                        if (reservedRoomHashMap.containsKey(rooms.get(i).getId())) {
                            ReservedRoom reservedRoom = reservedRoomHashMap.get(rooms.get(i).getId());
                            Date inReservedDate = formatDate(reservedRoom.getCheck_In());
                            Date outReservedDate = formatDate(reservedRoom.getCheck_Out());

                            if (inUserDate.compareTo(outReservedDate) > 0 || outUserDate.compareTo(inReservedDate) < 0) {
                                roomsFiltered.add(rooms.get(i));

                            }

                        }
                        else {
                            roomsFiltered.add(rooms.get(i));
                        }
                    }

                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                com.example.finalproject.CaptionedImageAdapter adapter = new com.example.finalproject.CaptionedImageAdapter(MainActivity.this,roomsFiltered,checkInTxt,checkOutTxt);

                recycler.setAdapter(adapter);
            } else {

                Toast.makeText(MainActivity.this, "Invalid Date", Toast.LENGTH_SHORT);
            }


        }

        else if(checkInTxt!=null&&checkOutTxt==null){
            Toast.makeText(MainActivity.this, "Invalid Date",Toast.LENGTH_SHORT);
        }
        else if(checkInTxt==null&&checkOutTxt!=null){
            Toast.makeText(MainActivity.this, "Invalid Date",Toast.LENGTH_SHORT);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}