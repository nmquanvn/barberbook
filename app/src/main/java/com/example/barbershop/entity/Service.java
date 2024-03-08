package com.example.barbershop.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.barbershop.MainActivity;
import com.example.barbershop.db.SQLLite;

import java.util.ArrayList;

public class Service {
    public int id;
    public String title;
    public String description;
    public float price;
    public byte[] image;

    public static ContentValues getEntity(String title, String description, float price, byte[] image){
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("description", description);
        cv.put("price", price);
        cv.put("image", image);

        return cv;
    }

    public static ArrayList<Service> loadfromDB(){
        ArrayList<Service> lst = new ArrayList<Service>();
        Cursor c1 = SQLLite.db.rawQuery("Select * from Services");

        c1.moveToPosition(-1);
        while (c1.moveToNext()) {
            Service s = new Service();
            s.id = c1.getInt(0);
            s.title = c1.getString(1);
            s.description = c1.getString(2);
            s.image = c1.getBlob(3);
            s.price = c1.getFloat(4);
            lst.add(s);
        }

        return lst;
    }

    public static Service getbyid(int id){
        Cursor c1 = SQLLite.db.rawQuery("Select * from Services where id=" + id);
        c1.moveToPosition(0);

        Service s = new Service();
        s.id = c1.getInt(0);
        s.title = c1.getString(1);
        s.description = c1.getString(2);
        s.image = c1.getBlob(3);
        s.price = c1.getFloat(4);

        return s;
    }
}
