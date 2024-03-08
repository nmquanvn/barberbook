package com.example.barbershop.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.barbershop.R;
import com.example.barbershop.adapter.serviceAdapter;
import com.example.barbershop.adapter.serviceAdapter2;
import com.example.barbershop.entity.Service;

public class Services2 extends Fragment {
    ListView lvServices;
    Context context;
    serviceAdapter2 adapter;
    String action;

    public Services2(Context c, String a) {
        // Required empty public constructor
        context = c;
        action = a;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_services2, null);
        lvServices = (ListView) layout.findViewById(R.id.lvServices);
        adapter = new serviceAdapter2(context, Service.loadfromDB());
        lvServices.setAdapter(adapter);

        Button btnBack = (Button) layout.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });

        Button btnOK = (Button) layout.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (action){
                    case "BookingForm":
                        BookingForm.saveService(adapter.selected);
                        break;
                    case "BookingManagerForm":
                        BookingManagerForm.saveService(adapter.selected);
                        break;
                }

                ((Activity)context).finish();
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }
}