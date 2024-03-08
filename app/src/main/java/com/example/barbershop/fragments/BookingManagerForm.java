package com.example.barbershop.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.barbershop.BookingActivity;
import com.example.barbershop.MainActivity;
import com.example.barbershop.R;
import com.example.barbershop.SubActivity;
import com.example.barbershop.adapter.staffAdapter;
import com.example.barbershop.entity.Barbershop;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.entity.Service;
import com.example.barbershop.entity.Staff;
import com.example.barbershop.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingManagerForm extends Fragment {
    Context context;
    LinearLayout selectDay;
    GridLayout selectHour;
    AutoCompleteTextView txtBranch;
    Spinner spnBookingNumber;
    staffAdapter adapter;
    String selectedDate = "";
    int selectedYear;
    String selectedHour = "";
    Booking data;
    EditText txtPhone, txtName, txtComment;
    
    public BookingManagerForm(Context c, Booking b) {
        context = c;
        data = b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_manager_form, container, false);
        spnBookingNumber = (Spinner) view.findViewById(R.id.spnBookingNumber);
        LoadUserInfo(view);
        LoadBranch(view);
        LoadStaff(view);
        LoadSelectDate(view);
        LoadSelectHour(view);
        setupClickEvent(view);

        txtBranch.setText(data.bookingAddress);
        spnBookingNumber.setSelection(data.bookingNumber - 1);
        if(selectedService == null){
            selectedService = new ArrayList<Service>();

            String[] serviceIds = data.bookingServiceIds.split(",");
            for(int i=0; i<serviceIds.length; i++){
                if(!serviceIds[i].isEmpty()) selectedService.add(Service.getbyid(Integer.valueOf(serviceIds[i])));
            }

            if(selectedService.size() > 0) btnServiceSelect.setText("Đẵ chọn " + selectedService.size() + " dịch vụ");
            else btnServiceSelect.setText("Nhấn vào đây để chọn dịch vụ");
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BookingManagerForm.selectedService = null;
        staffAdapter.defaultSelect = 0;
        SubActivity.frgMame = "";
        data = null;
    }

    void LoadUserInfo(View view){
        txtPhone = (EditText) view.findViewById(R.id.txtUserphone);
        txtName = (EditText) view.findViewById(R.id.txtUsername);
        txtComment = (EditText) view.findViewById(R.id.txtComment);

        txtName.setText(data.username);
        txtPhone.setText(data.userphone);
        txtComment.setText(data.comment);
    }
    
    void LoadBranch(View view){
        txtBranch = (AutoCompleteTextView) view.findViewById(R.id.txtBranch);
        ArrayAdapter adapterBranch = new ArrayAdapter(context, android.R.layout.simple_list_item_1, Barbershop.loadfromDB().toArray());
        txtBranch.setAdapter(adapterBranch);
    }

    void LoadStaff(View view){
        RecyclerView rvStaff = (RecyclerView) view.findViewById(R.id.rvStaff);
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvStaff.setLayoutManager(layout);
        ArrayList<Staff> staffdata = Staff.loadfromDB();
        Staff def = new Staff();
        def.id = -1;
        def.name = "Không chọn";
        def.avatar = helper.getDrawableAsByteArray(R.raw.img_app);
        staffdata.add(0, def);
        
        adapter = new staffAdapter(context, staffdata);
        for(int i=0; i<staffdata.size(); i++){
            if(staffdata.get(i).id == data.assignToId){
                staffAdapter.defaultSelect = i;
                break;
            }
        }
        
        rvStaff.setAdapter(adapter);
    }

    void LoadSelectDate(View view){
        selectDay = (LinearLayout) view.findViewById(R.id.selectDay);
        String[] days = new String[] { "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN" };
        for(int i=1;i<=7;i++){
            LocalDateTime date = LocalDateTime.now().plusDays(i);
            Button btn = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(helper.dpToPixel(80), helper.dpToPixel(110));
            btn.setLayoutParams(params);
            btn.setTextSize(12);
            btn.setText(days[date.getDayOfWeek().getValue() - 1] + "\n" + date.getDayOfMonth() + "/" + date.getMonthValue());
            btn.setTag("btnDay" + i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btnCur = (Button)v;
                    Button def = new Button(context);
                    for(int j=0; j<selectDay.getChildCount(); j++){
                        Button b = (Button) selectDay.getChildAt(j);
                        if(btnCur.getTag().toString() == b.getTag().toString()){
                            b.setBackgroundColor(context.getColor(R.color.colorPrimary));
                            b.setTextColor(context.getColor(R.color.white));
                            selectedDate = b.getText().toString().split("\n")[1];
                            selectedYear = LocalDateTime.now().plusDays(Integer.valueOf(b.getTag().toString().split("Day")[1])).getYear();
                        }
                        else{
                            b.setBackground(def.getBackground());
                            b.setTextColor(context.getColor(R.color.black));
                        }
                    }
                }
            });

            selectDay.addView(btn);
            if(data.bookingDate.contains(date.getDayOfMonth() + "/" + date.getMonthValue())){
                btn.performClick();
            }
        }
    }

    void LoadSelectHour(View view){
        selectHour = (GridLayout) view.findViewById(R.id.selectHour);
        String[] hours = new String[] { "8:00", "9:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00" };
        for(int i=0; i<hours.length; i++){
            Button btn = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(helper.dpToPixel(62), helper.dpToPixel(40));
            btn.setLayoutParams(params);
            btn.setText(hours[i]);
            btn.setTag("btnHour" + i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btnCur = (Button)v;
                    Button def = new Button(context);
                    for(int j=0; j<selectHour.getChildCount(); j++){
                        Button b = (Button) selectHour.getChildAt(j);
                        if(btnCur.getTag().toString() == b.getTag().toString()){
                            b.setBackgroundColor(context.getColor(R.color.colorPrimary));
                            b.setTextColor(context.getColor(R.color.white));
                            selectedHour = b.getText().toString();
                        }
                        else{
                            b.setBackground(def.getBackground());
                            b.setTextColor(context.getColor(R.color.black));
                        }
                    }
                }
            });

            selectHour.addView(btn);

            if(data.bookingDate.contains(btn.getText())){
                btn.performClick();
            }
        }
    }

    void setupClickEvent(View view){
        btnServiceSelect = (Button) view.findViewById(R.id.btnServiceSelect);
        btnServiceSelect.setText("Nhấn vào đây để chọn dịch vụ");
        btnServiceSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActivityIntent = new Intent(context, SubActivity.class);
                SubActivity.frgMame = "BookingManagerForm";
                startActivity(ActivityIntent);
            }
        });

        Button btnApprove = (Button) view.findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().isEmpty() || txtPhone.getText().toString().isEmpty() || txtBranch.getText().toString().isEmpty() || selectedService == null || selectedService.size() == 0 || selectedDate.isEmpty() || selectedHour.isEmpty()){
                    helper.ShowMessage(context, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }
                if(!Barbershop.isAddressTrue(txtBranch.getText().toString())){
                    helper.ShowMessage(context, "Vui lòng chọn địa chỉ của Barbershop!");
                    return;
                }
                if(adapter.getSelected().id == -1){
                    helper.ShowMessage(context, "Vui lòng chọn nhân viên cho lịch hẹn!");
                    return;
                }

                data.username = txtName.getText().toString();
                data.userphone = txtPhone.getText().toString();
                data.comment = txtComment.getText().toString();
                data.bookingAddress = txtBranch.getText().toString();
                data.bookingNumber = spnBookingNumber.getSelectedItemPosition() + 1;

                data.bookingServices = "";
                data.totalCost = 0;
                data.bookingServiceIds = "";
                for(int i=0;i<selectedService.size();i++){
                    data.bookingServices += selectedService.get(i).title + ", ";
                    data.totalCost += selectedService.get(i).price;
                    data.bookingServiceIds += selectedService.get(i).id + ",";
                }
                data.totalCost *= data.bookingNumber;
                if(data.bookingServices != "") data.bookingServices = data.bookingServices.substring(0, data.bookingServices.length() - 2);

                data.assignTo = adapter.getSelected().name;
                data.assignToId = adapter.getSelected().id;
                data.bookingDate = selectedDate + "/" + selectedYear + " " + selectedHour;
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

        Button btnReject = (Button) view.findViewById(R.id.btnReject);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Hủy lịch hẹn")
                        .setMessage("Bạn có chắc rằng bạn muốn hủy lịch hẹn này?")
                        .setCancelable(false)
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(txtComment.getText().toString().isEmpty()){
                                    helper.ShowMessage(context, "Vui lòng điền lý do từ chối vào mục ghi chú!");
                                    return;
                                }

                                data.comment = txtComment.getText().toString();
                                data.status = "Rejected";
                                data.SaveToDB();

                                helper.ShowMessage(context, "Lịch hẹn đã bị hủy.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainActivity.main.switchFragment("Assignment");
                                    }
                                });
                            }
                        }).show();
            }
        });

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.main.switchFragment("Assignment");
            }
        });
    }

    static Button btnServiceSelect;
    public static ArrayList<Service> selectedService;
    public static void saveService(ArrayList<Service> d){
        selectedService = d;
        if(d.size() > 0) btnServiceSelect.setText("Đẵ chọn " + d.size() + " dịch vụ");
        else btnServiceSelect.setText("Nhấn vào đây để chọn dịch vụ");
    }
}