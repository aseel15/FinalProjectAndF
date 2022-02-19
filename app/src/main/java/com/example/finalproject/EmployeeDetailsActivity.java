package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.model.Place;

public class EmployeeDetailsActivity extends AppCompatActivity {

    EmployeeFragmentDetails fragmentDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_e);

        fragmentDetails= (EmployeeFragmentDetails) getSupportFragmentManager().findFragmentById(R.id.fragmentIDe);
        Intent intent =getIntent() ;
        Place place = (Place) intent.getSerializableExtra("Place");
        fragmentDetails.setPlace(place);
    }
}