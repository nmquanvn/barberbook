package com.example.barbershop.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.barbershop.R;
import com.example.barbershop.SubActivity;
import com.example.barbershop.entity.Service;
import com.example.barbershop.fragments.Services2;
import com.example.barbershop.helper;

import java.util.ArrayList;

public class serviceAdapter2 extends BaseAdapter {
    ArrayList<Service> serviceList;
    Context context;
    public ArrayList<Service> selected;

    public serviceAdapter2(Context c, ArrayList<Service> lst){
        context = c;
        if(lst == null) serviceList = new ArrayList<Service>();
        else serviceList = lst;
        selected = new ArrayList<Service>();
    }
    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        if(serviceList.size() <= position || position < 0) return null;
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(serviceList.size() <= position || position < 0) return -1;
        return serviceList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.service_item2, null);
        Service s = serviceList.get(position);

        // get and set control
        TextView txtServiceTitle = (TextView) row.findViewById(R.id.txtServiceTitle);
        TextView txtServicePrice = (TextView) row.findViewById(R.id.txtServicePrice);
        ImageView ivServiceImage = (ImageView) row.findViewById(R.id.ivServiceImage);
        TableLayout serviceItem = (TableLayout) row.findViewById(R.id.serviceItem);
        CheckBox cbxSelectService = (CheckBox) row.findViewById(R.id.cbxSelectService);

        ivServiceImage.setImageBitmap(BitmapFactory.decodeByteArray(s.image, 0, s.image.length));
        txtServiceTitle.setText(s.title);
        txtServicePrice.setText(helper.formatmoney(s.price) + " vnd");

        serviceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cbx = v.findViewById(R.id.cbxSelectService);
                cbx.performClick();
            }
        });


        ArrayList<Service> data = null;
        switch (SubActivity.frgMame) {
            case "BookingForm":
                data = com.example.barbershop.fragments.BookingForm.selectedService;
                break;
            case "BookingManagerForm":
                data = com.example.barbershop.fragments.BookingManagerForm.selectedService;
                break;
        }
        if(data != null){
            for(int i=0;i<data.size();i++){
                Service s2 = data.get(i);
                if(s2.id == serviceList.get(position).id){
                    cbxSelectService.setChecked(true);
                    selected.add(serviceList.get(position));
                    break;
                }
            }
        }

        cbxSelectService.setTag(position);
        cbxSelectService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Service s = serviceList.get((int)buttonView.getTag());
                if(isChecked){
                    selected.add(s);
                }
                else{
                    if(selected.contains(s)) selected.remove(s);
                }
            }
        });

        return row;
    }
}
