package com.example.barbershop;

import android.app.Activity;
import android.os.Bundle;

import com.example.barbershop.entity.Staff;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.barbershop.databinding.ActivityLoginBinding;

public class LoginActivity extends Activity {
    EditText txtusername, txtpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtusername = (EditText) findViewById(R.id.txtusername);
        txtpassword = (EditText) findViewById(R.id.txtpassword);

        ImageButton btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtusername.getText().toString().isEmpty() || txtpassword.getText().toString().isEmpty()){
                    helper.ShowMessage(LoginActivity.this, "Vui lòng điền tên đăng nhập và mật khẩu!");
                    return;
                }

                Staff s = Staff.Login(txtusername.getText().toString(), txtpassword.getText().toString());
                if(s == null) helper.ShowMessage(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu!");
                else{
                    Staff.curUser = s;
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Staff.curUser != null) MainActivity.main.switchFragment("Home");
    }
}