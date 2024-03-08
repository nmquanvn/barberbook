package com.example.barbershop.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.barbershop.BookingActivity;
import com.example.barbershop.R;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.helper;

public class BookingUserInfo extends Fragment {
    public static Booking data;
    EditText txtPhone, txtName, txtComment;
    Context context;

    public BookingUserInfo(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_user_info, container, false);

        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtName = (EditText) view.findViewById(R.id.txtName);
        txtComment = (EditText) view.findViewById(R.id.txtComment);

        TextView txtServices = (TextView) view.findViewById(R.id.txtServices);
        txtServices.setText(data.bookingServices);
        TextView txtBookingNumber = (TextView) view.findViewById(R.id.txtBookingNumber);
        txtBookingNumber.setText(data.bookingNumber + " người");
        TextView txtTotalCost = (TextView) view.findViewById(R.id.txtTotalCost);
        txtTotalCost.setText(helper.formatmoney(data.totalCost) + " vnd");

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.username = txtName.getText().toString();
                data.userphone = txtPhone.getText().toString();
                data.comment = txtComment.getText().toString();

                BookingActivity.main.switchFragment("Form");
            }
        });

        Button btnOK = (Button) view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPhone.getText().toString().isEmpty() || txtName.getText().toString().isEmpty()){
                    helper.ShowMessage(context, "Vui lòng điền SĐT và Họ Tên để tiếp tục!");
                    return;
                }
                data.username = txtName.getText().toString();
                data.userphone = txtPhone.getText().toString();
                data.comment = txtComment.getText().toString();

                BookingActivity.main.switchFragment("OTP");
            }
        });

        if(data != null){
            txtName.setText(data.username);
            txtPhone.setText(data.userphone);
            txtComment.setText(data.comment);
        }

        return view;
    }
}