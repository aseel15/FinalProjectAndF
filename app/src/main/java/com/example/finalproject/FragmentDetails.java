package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.model.Place;

public class FragmentDetails extends Fragment {

    private Place place;
    private Button buttonReserve;
    private View rootView;

    public FragmentDetails() {
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_details,container,false);
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        buttonReserve = rootView.findViewById(R.id.buttonReservePlace);


       /* buttonReserve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Your Message", Toast.LENGTH_LONG).show();            }
        });*/
        buttonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(view.getContext(), HomeFragment.class);
                //view.getContext().startActivity(intent);
               // rootView = inflater.inflate(R.layout.reservation_details, container, false);
               openNewActivity();

            }
        });
        return rootView;
    }

    public void openNewActivity(){
        Intent intent = new Intent(FragmentDetails.this.getActivity(), PlaceRes.class);
        intent.putExtra("placeId",place.getPlaceId()+"");

        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        if(view!=null){
            TextView Title=view.findViewById(R.id.textViewDetailsTitle);
            TextView desc=view.findViewById(R.id.textViewDetailsDescription);
            TextView price=view.findViewById(R.id.textViewDetailsPrice);
            ImageView imageView=(ImageView)view.findViewById(R.id.imageViewDetails);
            TextView placeId=view.findViewById(R.id.textViewDetailsPlaceId);
            imageView.setImageResource((R.drawable.garden));

          //  ImageView cardImage= (ImageView) view.findViewById(R.id.imageViewDetails);
           // Drawable drawable= ContextCompat.getDrawable(view.getContext(),place.getPlaceId());
            //cardImage.setImageDrawable(drawable);

            Title.setText(place.getName());
            desc.setText(place.getDesc());

            price.setText("Price : "+place.getPrice()+"$");
            placeId.setText("Place Id : "+place.getPlaceId());
        }
    }

}
