package com.example.finalproject;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CaptionImageAPI extends RecyclerView.Adapter<CaptionImageAPI.ViewHolder> {
    Context context;
    private Button[]detailButtons;
    ArrayList<String>imageList;
    int size=0;
    private String type;
    String imgCake="No Picture chosen",imgDecor="No Picture chosen",imgFood="No Picture chosen";
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CaptionImageAPI(Context context, ArrayList<String>imageList){
        this.context=context;
        this.imageList=imageList;
        size=imageList.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_captioned_image_api,
                parent,
                false);

        return new ViewHolder(v);
    }
    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, null, context.getPackageName());

        return drawableResourceId;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView img = (ImageView) cardView.findViewById(R.id.imageAPI);
        Glide.with(context).load(imageList.get(position)).into(img);


        Button detailButton=(Button) cardView.findViewById(R.id.btnRes);
        detailButton.setOnClickListener(view -> {

            if (type.equalsIgnoreCase("Cake")){
                imgCake=imageList.get(position);
                btnClkAddCake();
            }
            else if (type.equalsIgnoreCase("Decor")){
                imgDecor=imageList.get(position);
                btnClkAddDecor();
            }
            else if (type.equalsIgnoreCase("Food")){
               imgFood=imageList.get(position);
                btnClkAddFood();
            }

        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;

        }

    }

    public void btnClkAddCake(){
        String url = "http://10.0.2.2:80/FinalProject/addPartyInfo.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(context,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
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

                params.put("cake", imgCake);
                params.put("user_id", String.valueOf(userId));


                return params;
            }
        };
        queue.add(request);
    }

    public void btnClkAddDecor(){

        String url = "http://10.0.2.2:80/FinalProject/addPartyInfo.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TAG", "RESPONSE IS " + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(context,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
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

                params.put("decor", imgDecor);
                params.put("user_id", String.valueOf(userId));


                return params;
            }
        };
        queue.add(request);
    }

    public void btnClkAddFood(){
        String url = "http://10.0.2.2:80/FinalProject/addPartyInfo.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(context,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
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


                params.put("food", imgFood);
                params.put("user_id", String.valueOf(userId));


                return params;
            }
        };
        queue.add(request);
    }
}