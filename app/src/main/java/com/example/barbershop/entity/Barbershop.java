package com.example.barbershop.entity;

import android.database.Cursor;

import com.example.barbershop.db.SQLLite;

import java.util.ArrayList;

public class Barbershop {
    public int id;
    public String title;
    public String address;
    public float latitude;
    public float longitude;

    @Override
    public String toString(){
        return address;
    }

    public static ArrayList<Barbershop> loadfromDB(){
        ArrayList<Barbershop> lst = new ArrayList<Barbershop>();
        Cursor c1 = SQLLite.db.rawQuery("Select * from Barbershops");

        c1.moveToPosition(-1);
        while (c1.moveToNext()) {
            Barbershop s = new Barbershop();
            s.id = c1.getInt(0);
            s.title = c1.getString(1);
            s.address = c1.getString(2);
            s.latitude = c1.getFloat(3);
            s.longitude = c1.getFloat(4);
            lst.add(s);
        }

        return lst;
    }

    public static Boolean isAddressTrue(String address){
        Cursor c1 = SQLLite.db.rawQuery("Select * from Barbershops where address='" + address + "'");
        return c1.getCount() > 0;
    }
}
