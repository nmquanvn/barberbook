package com.example.barbershop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.barbershop.MainActivity;
import com.example.barbershop.R;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.helper;

import java.util.ArrayList;

public class bookingAdapter extends BaseAdapter {
    ArrayList<Booking> bookingList;
    Context context;
    public static Booking seleted;

    public bookingAdapter(Context c, ArrayList<Booking> lst){
        context = c;
        if(lst == null) bookingList = new ArrayList<Booking>();
        else bookingList = lst;
        seleted = null;
    }
    @Override
    public int getCount() {
        return bookingList.size();
    }

    @Override
    public Object getItem(int position) {
        if(bookingList.size() <= position || position < 0) return null;
        return bookingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(bookingList.size() <= position || position < 0) return -1;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.booking_item, null);

        // get and set control
        TableLayout bookingItem = (TableLayout) row.findViewById(R.id.bookingItem);
        LinearLayout itemShortDetail = (LinearLayout) row.findViewById(R.id.itemShortDetail);
        ImageView ic_bookingItem = (ImageView) row.findViewById(R.id.ic_bookingItem);

        bookingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ic_bookingItem.getTag().toString() == "expand"){
                    ic_bookingItem.setImageResource(R.drawable.ic_collapse);
                    ic_bookingItem.setTag("collapse");
                    itemShortDetail.setVisibility(View.VISIBLE);
                }
                else{
                    ic_bookingItem.setImageResource(R.drawable.ic_expand);
                    ic_bookingItem.setTag("expand");
                    itemShortDetail.setVisibility(View.GONE);
                }
            }
        });
        bookingItem.performClick();

        Booking b = bookingList.get(position);
        TextView txtUsername = (TextView) row.findViewById(R.id.txtUsername);
        TextView txtbookingDate = (TextView) row.findViewById(R.id.txtbookingDate);
        TextView txtbookingServices = (TextView) row.findViewById(R.id.txtbookingServices);
        TextView txtUserphone = (TextView) row.findViewById(R.id.txtUserphone);
        TextView txtAssignTo = (TextView) row.findViewById(R.id.txtAssignTo);
        TextView txtbookingNumber = (TextView) row.findViewById(R.id.txtbookingNumber);
        TextView txtTotal = (TextView) row.findViewById(R.id.txtTotal);
        txtUsername.setText(b.username);
        txtbookingDate.setText(b.bookingDate);
        txtbookingServices.setText(txtbookingServices.getText() + b.bookingServices);
        txtUserphone.setText(txtUserphone.getText() + b.userphone);
        txtAssignTo.setText(txtAssignTo.getText() + b.assignTo);
        txtbookingNumber.setText(txtbookingNumber.getText().toString() + b.bookingNumber + " người");
        txtTotal.setText(txtTotal.getText().toString() + helper.formatmoney(b.totalCost) + " VND");

        Button btnView = (Button) row.findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleted = bookingList.get(position);
                MainActivity.main.switchFragment("AssignmentDetail");
            }
        });

        Button btnApprove = (Button) row.findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Booking data = bookingList.get(position);

                if(data.assignToId == -1){
                    helper.ShowMessage(context, "Vui lòng chọn nhân viên cho lịch hẹn!");
                    return;
                }

                data.status = "Approved";
                data.SaveToDB();

                helper.ShowMessage(context, "Yêu cầu đã được chấp thuận.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.main.switchFragment("Assignment");
                    }
                });
            }
        });

        return row;
    }
}
