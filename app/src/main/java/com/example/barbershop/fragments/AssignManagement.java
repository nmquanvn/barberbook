package com.example.barbershop.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.barbershop.R;
import com.example.barbershop.adapter.bookingAdapter;
import com.example.barbershop.entity.Booking;

import java.util.ArrayList;

public class AssignManagement extends Fragment {
    Context context;

    public AssignManagement(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*try {
            context = getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_assign_manager, null);

        ArrayList<Booking> lst = Booking.getByStatus("New", "");
        ListView lvBooking = (ListView) layout.findViewById(R.id.lvBooking);
        TextView txtNoData = (TextView) layout.findViewById(R.id.txtNoData);
        if(lst.size() == 0){
            lvBooking.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
        else{
            bookingAdapter adapter = new bookingAdapter(context, lst);
            lvBooking.setAdapter(adapter);
            lvBooking.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        }
        // Inflate the layout for this fragment
        return layout;
    }
}