package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.barbershop.fragments.Services2;

public class SubActivity extends FragmentActivity {
    public static String frgMame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Fragment frg = null;
        switch (frgMame){
            case "BookingForm":
                frg = new Services2(this, frgMame);
                break;
            case "BookingManagerForm":
                frg = new Services2(this, frgMame);
                break;
        }
        if(frg != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frgMainSubActivity, frg).commit();
        }
    }
}