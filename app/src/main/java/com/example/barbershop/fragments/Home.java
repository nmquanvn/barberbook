package com.example.barbershop.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.*;
import android.widget.*;

import com.example.barbershop.BookingActivity;
import com.example.barbershop.LoginActivity;
import com.example.barbershop.MainActivity;
import com.example.barbershop.MapsFragment;
import com.example.barbershop.R;
import com.example.barbershop.entity.Staff;
import com.example.barbershop.helper;

public class Home extends Fragment  implements View.OnClickListener  {
    Context context;
    Button btnLogin;

    public Home(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_home, container, false);

        TableLayout bannerTools = (TableLayout) view.findViewById(R.id.bannerTools);
        LinearLayout bannerLogin = (LinearLayout) view.findViewById(R.id.bannerLogin);
        if(com.example.barbershop.helper.mode.equals("staff")){
            bannerTools.setVisibility(View.GONE);
            bannerLogin.setVisibility(View.VISIBLE);
        }

        ImageView btnFind = (ImageView) view.findViewById(R.id.btnFind);
        ImageView btnBooking = (ImageView) view.findViewById(R.id.btnBooking);
        ImageView btnServices = (ImageView) view.findViewById(R.id.btnServices);
        btnFind.setOnClickListener(this);
        btnBooking.setOnClickListener(this);
        btnServices.setOnClickListener(this);

        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginActivityIntent = new Intent(context, LoginActivity.class);
                startActivity(LoginActivityIntent);
            }
        });

        if(Staff.curUser != null){
            ImageView ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(Staff.curUser.avatar, 0, Staff.curUser.avatar.length));

            TextView txtCurUsername = (TextView) view.findViewById(R.id.txtCurUsername);
            txtCurUsername.setText("Chào " + Staff.curUser.name);

            TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
            txtMessage.setText("Bạn sẽ được tự động đăng xuất sau khi đóng ứng dụng.");
            txtMessage.getLayoutParams().width = helper.dpToPixel(200);

            btnLogin.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFind:
                Intent MapsFragmentIntent = new Intent(context, MapsFragment.class);
                startActivity(MapsFragmentIntent);
                break;
            case R.id.btnBooking:
                Intent BookingActivityIntent = new Intent(context, BookingActivity.class);
                startActivity(BookingActivityIntent);
                break;
            case R.id.btnServices:
                MainActivity.main.switchFragment("Services");
                break;
        }
    }
}