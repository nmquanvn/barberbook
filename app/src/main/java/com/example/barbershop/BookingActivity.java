package com.example.barbershop;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.barbershop.adapter.staffAdapter;
import com.example.barbershop.databinding.ActivityBookingBinding;
import com.example.barbershop.fragments.BookingForm;
import com.example.barbershop.fragments.BookingOTP;
import com.example.barbershop.fragments.BookingUserInfo;
import com.example.barbershop.fragments.Services;
import com.example.barbershop.fragments.Services2;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BookingActivity extends FragmentActivity {
    public static BookingActivity main;
    private ActivityBookingBinding binding;
    CollapsingToolbarLayout toolBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;

        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle("Đặt lịch cắt tóc");

        getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new BookingForm(this)).commit();

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        main = null;
        BookingForm.selectedService = null;
        BookingUserInfo.data = null;
        staffAdapter.defaultSelect = 0;
        SubActivity.frgMame = "";
    }

    public void switchFragment(String frgName){
        Fragment frg = null;
        switch (frgName){
            case "Confirm":
                toolBarLayout.setTitle("Thông tin cá nhân");
                frg = new BookingUserInfo(this);
                break;
            case "Form":
                toolBarLayout.setTitle("Đặt lịch cắt tóc");
                frg = new BookingForm(this);
                break;
            case "OTP":
                toolBarLayout.setTitle("Xác thực SĐT");
                frg = new BookingOTP(this);
                break;
        }
        if(frg != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, frg).commit();
        }
    }
}