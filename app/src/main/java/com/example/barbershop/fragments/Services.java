package com.example.barbershop.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.barbershop.R;
import com.example.barbershop.adapter.bookingAdapter;
import com.example.barbershop.adapter.serviceAdapter;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.entity.Service;

public class Services extends Fragment {
    ListView lvServices;
    Context context;

    public Services(Context c) {
        // Required empty public constructor
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_services, null);
        lvServices = (ListView) layout.findViewById(R.id.lvServices);
        serviceAdapter adapter = new serviceAdapter(context, Service.loadfromDB());
        lvServices.setAdapter(adapter);

        // Inflate the layout for this fragment
        return layout;
    }
}