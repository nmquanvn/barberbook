package com.example.barbershop.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.MainActivity;
import com.example.barbershop.R;
import com.example.barbershop.entity.Booking;
import com.example.barbershop.entity.Staff;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class staffAdapter extends RecyclerView.Adapter<staffAdapter.ViewHolder> {
    ArrayList<Staff> staffList;
    ArrayList<LinearLayout> layoutList;
    Context context;
    public static int defaultSelect = 0;

    public staffAdapter(Context c, ArrayList<Staff> lst){
        context = c;
        layoutList = new ArrayList<LinearLayout>();
        if(lst == null) staffList = new ArrayList<Staff>();
        else staffList = lst;
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton btnStaffAvatar;
        public TextView txtStaffName;

        public ViewHolder(View view) {
            super(view);
            btnStaffAvatar = (ImageButton) view.findViewById(R.id.btnStaffAvatar);
            txtStaffName = (TextView) view.findViewById(R.id.txtStaffName);
        }
    }

    @NonNull
    @NotNull
    @Override
    public staffAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.staff_item, viewGroup, false);
        layoutList.add((LinearLayout) view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull staffAdapter.ViewHolder holder, int position) {
        Staff s = staffList.get(position);
        holder.btnStaffAvatar.setImageBitmap(BitmapFactory.decodeByteArray(s.avatar, 0, s.avatar.length));
        String rating = s.id == -1 ? "" : s.rating + "/10";
        holder.txtStaffName.setText(s.name + "\n" + rating);
        if(position == defaultSelect){
            layoutList.get(position).setBackgroundColor(MainActivity.main.getColor(R.color.colorPrimary));
            holder.txtStaffName.setTextColor(MainActivity.main.getColor(R.color.white));
        }

        holder.btnStaffAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<layoutList.size();i++){
                    if(i!=position){
                        layoutList.get(i).setBackgroundColor(MainActivity.main.getColor(R.color.white));
                        ((TextView)layoutList.get(i).findViewById(R.id.txtStaffName)).setTextColor(MainActivity.main.getColor(R.color.black));
                    }
                    else{
                        layoutList.get(i).setBackgroundColor(MainActivity.main.getColor(R.color.colorPrimary));
                        ((TextView)layoutList.get(i).findViewById(R.id.txtStaffName)).setTextColor(MainActivity.main.getColor(R.color.white));
                        defaultSelect = i;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public Staff getSelected(){
        return staffList.get(defaultSelect);
    }
}
