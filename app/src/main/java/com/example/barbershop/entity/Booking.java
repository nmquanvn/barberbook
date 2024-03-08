package com.example.barbershop.entity;

import android.database.Cursor;
import android.text.Editable;
import android.util.Log;

import com.example.barbershop.db.SQLLite;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Booking {
    public int id;
    public int userid;
    public String username;
    public String userphone;
    public String bookingAddress;
    public String bookingDate;
    public String bookingServices;
    public String assignTo;
    public int bookingNumber;
    public float totalCost;
    public String status;
    public String comment;
    public String bookingServiceIds;
    public int assignToId;

    public void SaveToDB(){
        String[] dateChars = bookingDate.split(" ")[0].split("/");
        String[] timeChars = bookingDate.split(" ")[1].split(":");
        Date date = new Date(Integer.valueOf(dateChars[2]), Integer.valueOf(dateChars[1]), Integer.valueOf(dateChars[0]), Integer.valueOf(timeChars[0]), Integer.valueOf(timeChars[1]));

        if(id > 0){
            SQLLite.db.execSQL(String.format("update Bookings set username='%s', userphone='%s', bookingAddress='%s', bookingDate='%s', bookingServices='%s', assignTo='%s', bookingNumber=%d, totalCost=%f, comment='%s', status='%s', bookingServiceIds='%s', assignToId=%d, bookingDateNumber=%d where id=%d"
                    , username, userphone, bookingAddress, bookingDate, bookingServices, assignTo, bookingNumber, totalCost, comment, status, bookingServiceIds, assignToId, date.getTime(), id));
        }
        else {
            SQLLite.db.execSQL("insert into Bookings(username, userphone, bookingAddress, bookingDate, bookingServices, assignTo, bookingNumber, totalCost, comment, status, bookingServiceIds, assignToId, bookingDateNumber)" +
                    String.format(" values ('%s', '%s', '%s', '%s', '%s', '%s', %d, %f, '%s', 'New', '%s', %d, %d);", username, userphone, bookingAddress, bookingDate, bookingServices, assignTo, bookingNumber, totalCost, comment, bookingServiceIds, assignToId, date.getTime()));
        }
    }

    public static ArrayList<Booking> getByStatus(String s, String condition){
        ArrayList<Booking> lst = new ArrayList<Booking>();
        String sql = "Select * from Bookings where status='" + s + "'";
        if(!condition.isEmpty()) sql += condition;
        Cursor c1 = SQLLite.db.rawQuery(sql);

        c1.moveToPosition(-1);
        while (c1.moveToNext()) {
            Booking b = new Booking();
            b.id = c1.getInt(0);
            //b.userid = c1.getInt(1);
            b.username = c1.getString(2);
            b.userphone = c1.getString(3);
            b.bookingAddress = c1.getString(4);
            b.bookingDate = c1.getString(5);
            b.bookingServices = c1.getString(6);
            b.assignTo = c1.getString(7);
            b.bookingNumber = c1.getInt(8);
            b.totalCost = c1.getFloat(9);
            b.status = c1.getString(10);
            b.comment = c1.getString(11);
            b.bookingServiceIds = c1.getString(12);
            b.assignToId = c1.getInt(13);
            lst.add(b);
        }

        return lst;
    }
}
