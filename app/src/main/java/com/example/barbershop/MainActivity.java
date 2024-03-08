package com.example.barbershop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.*;

import com.example.barbershop.adapter.bookingAdapter;
import com.example.barbershop.db.SQLLite;
import com.example.barbershop.entity.Staff;
import com.example.barbershop.fragments.AssignManagement;
import com.example.barbershop.fragments.BookingManagerForm;
import com.example.barbershop.fragments.Events;
import com.example.barbershop.fragments.Home;
import com.example.barbershop.fragments.Services;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity{
    public static MainActivity main;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareview();
    }

    public void prepareview(){
        main = this;
        SQLLite.db = new SQLLite();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new Home(MainActivity.this)).commit();

        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        loadbottomnav();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                Fragment frg = null;
                switch (item.getItemId()){
                    case R.id.itemHome:
                        frg = new Home(MainActivity.this);
                        break;
                    case R.id.itemAssignment:
                        frg = new AssignManagement(MainActivity.this);
                        break;
                    case R.id.itemEvents:
                        frg = new Events(MainActivity.this);
                        break;
                    default:
                        frg = new Home(MainActivity.this);
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, frg).commit();
                return true;
            }
        });
    }

    public void switchFragment(String frgName){
        Fragment frg = null;
        switch (frgName){
            case "Services":
                frg = new Services(this);
                break;
            case "Assignment":
                frg = new AssignManagement(this);
                break;
            case "AssignmentDetail":
                frg = new BookingManagerForm(this, bookingAdapter.seleted);
                break;
            case "Home":
                bottomNav.setSelectedItemId(R.id.itemHome);
                loadbottomnav();
                break;
        }
        if(frg != null) getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, frg).commit();
    }

    public  void loadbottomnav(){
        if(helper.mode.equals("staff")){
            bottomNav.getMenu().clear();
            bottomNav.inflateMenu(R.menu.bottom_nav_staff);
            if(Staff.curUser == null){
                bottomNav.getMenu().getItem(1).setEnabled(false);
                bottomNav.getMenu().getItem(2).setEnabled(false);
            }
            else{
                bottomNav.getMenu().getItem(1).setEnabled(true);
                bottomNav.getMenu().getItem(2).setEnabled(true);
                if(!Staff.curUser.role.equals("MANAGER")) bottomNav.getMenu().getItem(2).setEnabled(false);
            }
        }
        else if(helper.mode.equals("admin")) {
            bottomNav.getMenu().clear();
            bottomNav.inflateMenu(R.menu.bottom_nav_staff);
        }
    }
}