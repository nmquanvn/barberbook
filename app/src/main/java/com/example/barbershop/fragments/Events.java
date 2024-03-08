package com.example.barbershop.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.barbershop.R;
import com.example.barbershop.adapter.bookingAdapter;
import com.example.barbershop.adapter.bookingAdapter2;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.entity.Staff;
import com.example.barbershop.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Events extends Fragment {
    Context context;

    public Events(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_events, null);

        Date date = new Date();
        date = new Date(date.getYear(), date.getMonth(), date.getDate());
        String cond = " and bookingDateNumber>=" + date.getTime();
        if(Staff.curUser != null && Staff.curUser.role.equals("CUTTER")){
            cond += " and assignToId=" + Staff.curUser.id;
        }
        ArrayList<Booking> lst = Booking.getByStatus("Approved", cond);
        /*LocalDateTime now = LocalDateTime.now();
        for(int i=0; i<lst.size(); i++){
            LocalDateTime date = LocalDateTime.parse(lst.get(i).bookingDate, DateTimeFormatter.ofPattern("dd/MM/YYYY hh:mm"));
            if(now.getYear() > date.getYear() ||
                    (now.getYear() == date.getYear() && now.getMonthValue() > date.getMonthValue()) ||
                    (now.getYear() == date.getYear() && now.getMonthValue() == date.getMonthValue() && now.getDayOfMonth() > date.getDayOfMonth())){
                lst.remove(i);
                i--;
            }
        }*/
        ListView lvBooking = (ListView) layout.findViewById(R.id.lvBooking);
        TextView txtNoData = (TextView) layout.findViewById(R.id.txtNoData);
        if(lst.size() == 0){
            lvBooking.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
        else{
            bookingAdapter2 adapter = new bookingAdapter2(context, lst);
            lvBooking.setAdapter(adapter);
            lvBooking.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return layout;
    }
}