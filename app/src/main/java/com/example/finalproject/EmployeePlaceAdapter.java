package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.model.Place;

import java.io.Serializable;
import java.util.List;

public class EmployeePlaceAdapter extends RecyclerView.Adapter<EmployeePlaceAdapter.ViewHolder> {

    private Context mCtx;
    private List<Place> PlaceList;


    public Context getmCtx() {
        return mCtx;
    }

    public void setmCtx(Context mCtx) {
        this.mCtx = mCtx;
    }

    public List<Place> getPlaceList() {
        return PlaceList;
    }

    public void setPlaceList(List<Place> placeList) {
        PlaceList = placeList;
    }

    public EmployeePlaceAdapter(Context mCtx, List<Place> placeList) {
        this.mCtx = mCtx;
        PlaceList = placeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        CardView view = (CardView) inflater.inflate(R.layout.cardview, parent,false);
        return new ViewHolder(view);
    }

    public int getImage(String imageName){

        int drawableResourceId=mCtx.getResources().getIdentifier(imageName,null,mCtx.getPackageName());
        return drawableResourceId;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.imageViewHome);
//        Glide.with(mCtx).load(getImage("@drawable/"+imageIds[position])).into(imageView);
        Glide.with(mCtx).load(getImage("@drawable/"+PlaceList.get(position).getImage())).into(imageView);

        TextView textPlaceName =(TextView) cardView.findViewById(R.id.textViewTitle);

        TextView textViewTitle = cardView.findViewById(R.id.textViewHomeTitle);
        TextView textViewPrice = cardView.findViewById(R.id.textViewHomePrice);
        textViewTitle.setText(PlaceList.get(position).getName());
        textViewPrice.setText("$" + PlaceList.get(position).getPrice() + "");

        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(cardView.getContext(),EmployeeDetailsActivity.class);
                intent.putExtra("Place", (Serializable) PlaceList.get(position));
                cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlaceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }
    }

}