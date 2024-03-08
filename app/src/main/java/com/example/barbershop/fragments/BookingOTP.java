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
import android.widget.Toast;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.example.barbershop.BookingActivity;
import com.example.barbershop.R;
import com.example.barbershop.helper;

public class BookingOTP extends Fragment {
    Context context;
    OtpEditText txtCode;

    public BookingOTP(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_otp, container, false);

        txtCode = (OtpEditText) view.findViewById(R.id.txtCode);
        txtCode.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                if (!txtCode.getText().toString().equals("1234")) {
                    helper.ShowMessage(context, "OTP không chính xác, vui lòng nhập lại!");
                    return;
                }

                BookingUserInfo.data.SaveToDB();
                helper.ShowMessage(context, "Yêu cầu của quý khách đã được ghi nhận, sẽ có nhân viên liên lạc với quý khách qua SĐT " + BookingUserInfo.data.userphone + ".\nCảm ơn quý khách đã sử dụng dịch vụ của Handsome Barbershop", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookingActivity.main.finish();
                    }
                });

            }
        });

        TextView txtResendCode = (TextView) view.findViewById(R.id.txtResendCode);
        txtResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Mã mới đã được gửi!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingActivity.main.switchFragment("Confirm");
            }
        });

        return view;
    }
}