package com.example.barbershop.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.databinding.adapters.ImageViewBindingAdapter;

import com.example.barbershop.R;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.entity.Service;
import com.example.barbershop.helper;

import java.util.ArrayList;

public class serviceAdapter extends BaseAdapter {
    ArrayList<Service> serviceList;
    Context context;

    public serviceAdapter(Context c, ArrayList<Service> lst){
        context = c;
        if(lst == null) serviceList = new ArrayList<Service>();
        else serviceList = lst;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.service_item, null);
        Service s = serviceList.get(position);

        // get and set control
        TableLayout serviceItem = (TableLayout) row.findViewById(R.id.serviceItem);
        LinearLayout itemDetail = (LinearLayout) row.findViewById(R.id.itemDetail);
        ImageView ic_serviceItem = (ImageView) row.findViewById(R.id.ic_serviceItem);
        TextView txtServiceDetail = (TextView) row.findViewById(R.id.txtServiceDetail);
        TextView txtServiceTitle = (TextView) row.findViewById(R.id.txtServiceTitle);
        TextView txtServicePrice = (TextView) row.findViewById(R.id.txtServicePrice);
        ImageView ivServiceImage = (ImageView) row.findViewById(R.id.ivServiceImage);

        ivServiceImage.setImageBitmap(BitmapFactory.decodeByteArray(s.image, 0, s.image.length));
        txtServiceTitle.setText(s.title);
        txtServicePrice.setText(helper.formatmoney(s.price) + " vnd");
        txtServiceDetail.setText(s.description);

        serviceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ic_serviceItem.getTag().toString() == "expand"){
                    ic_serviceItem.setImageResource(R.drawable.ic_collapse);
                    ic_serviceItem.setTag("collapse");
                    itemDetail.setVisibility(View.VISIBLE);
                }
                else{
                    ic_serviceItem.setImageResource(R.drawable.ic_expand);
                    ic_serviceItem.setTag("expand");
                    itemDetail.setVisibility(View.GONE);
                }
            }
        });
        serviceItem.performClick();

        return row;
    }
}
