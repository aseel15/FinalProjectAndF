package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.model.Place;

import java.io.Serializable;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Context mCtx;
    private List<Place> PlaceList;
    private FragmentActivity fragmentActivity;


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

    public PlaceAdapter(Context mCtx, List<Place> placeList) {
        this.mCtx = mCtx;
        PlaceList = placeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("myTag", "donee card view clickeeeeeeeeeedzftttt111111111111");

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
        Log.d("myTag", "donee card view clickeeeeeeeeeedzftttt");

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
                Log.d("myTag", "donee card view clickeeeeeeeeeed");
               Intent intent = new Intent(cardView.getContext(),DetailsActivity.class);
                intent.putExtra("Place", (Serializable) PlaceList.get(position));
                Log.d("myTag", "donee card view clickeeeeeeeeeed222222222222");

                cardView.getContext().startActivity(intent);
                Log.d("myTag", "donee card view clickeeeeeeeeeed333333333333");

                // Intent intent = new Intent(cardView.getContext(), test.class);
      //          intent.putExtra("roomNum",PlaceList.get(position).getPlaceId()+"");

        //        intent.putExtra("arrayList", (Serializable) PlaceList);

          //      cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlaceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        TextView textViewName, textViewDesc,textViewPrice;
//        ImageView imageView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            textViewName = itemView.findViewById(R.id.textViewTitle);
//            textViewPrice = itemView.findViewById(R.id.textViewHomePrice);
//            imageView = itemView.findViewById(R.id.imageViewHome);
//
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }
    }

}