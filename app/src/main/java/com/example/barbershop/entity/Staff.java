package com.example.barbershop.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.barbershop.db.SQLLite;

import java.util.ArrayList;

public class Staff {
    public int id;
    public String name;
    public String loginname;
    public String password;
    public String role;
    public byte[] avatar;
    public int rating;

    public static ContentValues getEntity(String name, String lgname, String pass, String role, byte[] image, int rating){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("loginname", lgname);
        cv.put("password", pass);
        cv.put("role", role);
        cv.put("avatar", image);
        cv.put("rating", rating);

        return cv;
    }

    public static ArrayList<Staff> loadfromDB(){
        ArrayList<Staff> lst = new ArrayList<Staff>();
        Cursor c1 = SQLLite.db.rawQuery("Select * from Staffs where role='CUTTER'");

        c1.moveToPosition(-1);
        while (c1.moveToNext()) {
            Staff s = new Staff();
            s.id = c1.getInt(0);
            s.name = c1.getString(1);
            s.loginname = c1.getString(2);
            s.password = c1.getString(3);
            s.role = c1.getString(4);
            s.avatar = c1.getBlob(5);
            s.rating = c1.getInt(6);
            lst.add(s);
        }

        return lst;
    }

    public static Staff Login(String loginname, String password){
        Cursor c1 = SQLLite.db.rawQuery("Select * from Staffs where loginname='" + loginname + "' and password='" + password + "'");
        if(c1 == null || c1.getCount() == 0) return null;

        c1.moveToPosition(0);
        Staff s = new Staff();
        s.id = c1.getInt(0);
        s.name = c1.getString(1);
        s.loginname = c1.getString(2);
        s.password = c1.getString(3);
        s.role = c1.getString(4);
        s.avatar = c1.getBlob(5);
        s.rating = c1.getInt(6);

        return s;
    }

    public static Staff curUser;
}
