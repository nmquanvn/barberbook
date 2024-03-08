package com.example.barbershop.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.example.barbershop.MainActivity;
import com.example.barbershop.R;
import com.example.barbershop.entity.*;
import com.example.barbershop.helper;

public class SQLLite {
    SQLiteDatabase database;

    public SQLLite(){
        database = SQLiteDatabase.openDatabase(MainActivity.main.getApplication().getFilesDir() + "/MyDB",null, SQLiteDatabase.CREATE_IF_NECESSARY);
        if(false){
            database.execSQL("DROP TABLE IF EXISTS Barbershops;");
            database.execSQL("DROP TABLE IF EXISTS Staffs;");
            database.execSQL("DROP TABLE IF EXISTS Services;");
            database.execSQL("DROP TABLE IF EXISTS Bookings;");
        }
        if(!isTableExisted(database, "Barbershops")) prepareData();
    }

    public void execSQL(String sql){
        try {
            database.execSQL(sql);
        }
        catch (SQLiteException e) {
            Log.e("SQL error", e.toString());
        }
    }

    public Cursor rawQuery(String sql){
        try {
            return database.rawQuery(sql, null);
        }
        catch (SQLiteException e) {
            Log.e("SQL error", e.toString());
            return null;
        }
    }

    void prepareData(){
        Log.i("SQLLite", "Start create table");
        database.execSQL("create table Barbershops(id integer PRIMARY KEY autoincrement, title text, address text, latitude real, longitude real);");
        database.execSQL("create table Staffs(id integer PRIMARY KEY autoincrement, name text, loginname text, password text, role text, avatar blob, rating integer);");
        database.execSQL("create table Services(id integer PRIMARY KEY autoincrement, title text, description text, image blob, price real);");
        database.execSQL("create table Bookings(id integer PRIMARY KEY autoincrement, userid integer, username text, userphone text, bookingAddress text, bookingDate text, bookingServices text, assignTo text, bookingNumber integer, totalCost real, status text, comment text, bookingServiceIds text, assignToId integer, bookingDateNumber integer);");

        Log.i("SQLLite", "Start insert data");
        database.beginTransaction();
        try {
            // Barbershops
            Log.i("SQLLite", "insert data for Barbershops");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Q1', 'Điện biên phủ, quận 1, HCM, Việt Nam', 10.7893, 106.69649);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Q5', 'Nguyễn văn cừ, quận 5, HCM, Việt Nam', 10.75947, 106.68399);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Q9', 'Phạm văn đồng, quận 9, HCM, Việt Nam', 10.81464, 106.67973);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Q11', '3/2, quận 11, HCM, Việt Nam', 10.75852, 106.65014);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Tân Phú', 'Độc lập, quận Tân Phú, HCM, Việt Nam', 10.79227, 106.63295);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Bình Tân', '19, quận Bình Tân, HCM, Việt Nam', 10.753050, 106.617740);");
            database.execSQL("insert into Barbershops(title, address, latitude, longitude) values ('Barber Thủ Đức', 'Tam bình, quận Thủ đức, HCM, Việt Nam', 10.85806, 106.73973);");

            // Staffs
            Log.i("SQLLite", "insert data for Staffs");
            database.insert("Staffs", null, Staff.getEntity("Ironman", "ironman", "1", "MANAGER", helper.getDrawableAsByteArray(R.raw.img_admin), 0));
            database.insert("Staffs", null, Staff.getEntity("Doctor Strange", "strange", "1", "CUTTER", helper.getDrawableAsByteArray(R.raw.img_doctor), 8));
            database.insert("Staffs", null, Staff.getEntity("Hulk", "hulk", "1", "CUTTER", helper.getDrawableAsByteArray(R.raw.img_hulk), 9));
            database.insert("Staffs", null, Staff.getEntity("Nobody", "nobody", "1", "CUTTER", helper.getDrawableAsByteArray(R.raw.img_nobody), 5));

            // Services
            Log.i("SQLLite", "insert data for Services");
            database.insert("Services", null, Service.getEntity("Combo 80k", "Combo 10 bước:\n1. Massage khai huyệt điều hòa (mới)\n2. Rửa mặt – massage tinh chất nha đam thẩm thấu\n3. Hút mụn – phun nước hoa hồng công nghệ cao\n4. Gội đầu massage bấm huyệt\n5. Massage rửa tai bọt sảng khoái (mới)\n6. Kéo khăn giãn cơ cổ - xối nước thác đổ (mới)\n7. Tư vấn kiểu tóc hợp khuôn mặt\n8. Cắt tóc tạo kiểu bởi stylist hàng đầu\n9. Cạo mặt êm ái – xả sạch tóc con\n10. Vuốt sáp – xịt gôm tạo kiểu cao cấp", 80000, helper.getDrawableAsByteArray(R.raw.img_combo)));
            database.insert("Services", null, Service.getEntity("Nhuộm tóc", "Khi nhuộm tóc, các bạn luôn săn lùng những màu tóc nhuộm đẹp, tôn da đang thịnh hành và mang lại sự trẻ trung, tươi mới cho mình. Vậy bây giờ nhuộm tóc màu nào đẹp khi xu hướng không ngừng thay đổi? Nhuộm tóc màu vàng đồng, nhuộm tóc màu vàng nâu, tóc màu vàng chanh, cách nhuộm tóc màu nâu vàng sáng… vô vàn các lựa chọn cho bạn! Hãy cùng Đẹp365 cập nhật những màu tóc đẹp làm sáng da cho nữ và cách chăm sóc tóc nhuộm nhé!", 150000, helper.getDrawableAsByteArray(R.raw.img_hair_color)));
            database.insert("Services", null, Service.getEntity("Dưỡng tóc", "Dove Phục Hồi Hư Tổn giúp xác định và phân bổ dưỡng chất theo các mức độ hư tổn khác nhau. Công nghệ định vị hư tổn thông minh giúp định vị chính xác phần tóc hư tổn để phục hồi. Ngăn Rụng Tóc Hiệu Quả. Liệu Pháp Dầu Dưỡng. Dưỡng Tóc Bồng Bềnh.", 100000, helper.getDrawableAsByteArray(R.raw.img_hair_care2)));
            database.insert("Services", null, Service.getEntity("Tạo kiểu tóc", "Kiểu tóc nam này rất phù hợp cho những bạn yêu thích phong cách lãng tử Hàn Quốc. Và nó đặc biệt hợp với những bạn có khuôn mặt hơi vuông và dài hoặc trái xoan. Bởi nó sẽ giúp khuôn mặt trở nên mềm mại hơn. Đối với những bạn tóc mỏng thì còn giúp tóc bạn trông dày dặn hơn.", 70000, helper.getDrawableAsByteArray(R.raw.img_hair_style)));
            database.insert("Services", null, Service.getEntity("Cấy tốc tự nhiên", "Cấy tóc sinh học là phương pháp cấy tóc không phẫu thuật sử dụng sợi tóc sinh học với cấu tạo đặc biệt (3 lớp như tóc tự thân, có các nút thắt ở gốc tóc) để cấy vào vùng da đầu không có tóc, tóc bị rụng, vết sẹo… nhằm phục hồi tóc một cách tự nhiên nhất sau khi cấy.", 350000, helper.getDrawableAsByteArray(R.raw.img_hair_care1)));
            database.insert("Services", null, Service.getEntity("Trị hói", "Điều trị rụng tóc, hói đầu không chữa khỏi bằng tiêm tại thương tổn là phương pháp đơn giản, an toàn, chi phí thấp trong điều trị rụng tóc thành mảng và rụng tóc lan tỏa. Đây cũng là phương pháp điều trị an toàn hiệu quả, đã phổ biến trong chuyên ngành da liễu, mang lại giá trị thẩm mĩ cho khách hàng đặc biệt là những trường hợp hói đầu ở nữ hay hói đầu khi còn trẻ. Tuy nhiên hiệu quả và độ an toàn của phương pháp này phụ thuộc vào tay nghề bác sĩ.", 1500000, helper.getDrawableAsByteArray(R.raw.img_bare_head)));
            database.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            Log.i("SQLLite error", e.toString());
        }
        finally { database.endTransaction(); }
        Log.i("SQLLite", "completed");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        database.close();
    }

    public static SQLLite db;
    public static boolean isTableExisted(SQLiteDatabase db, String tableName) {
        String mySql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        int resultSize = db.rawQuery(mySql, null).getCount();
        if (resultSize == 0) {
            return false;
        }
        else{
            return true;
        }
    }
}
