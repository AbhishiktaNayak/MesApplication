package com.example.messmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdminHome extends AppCompatActivity {

    private Toolbar adminToolbar;
    private TextView admin_name;
    private RelativeLayout rlFeedback,rlAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminToolbar=findViewById(R.id.adminToolbar);
        admin_name= findViewById(R.id.admin_name);

        rlAddUser=findViewById(R.id.relative_add_user);
        rlAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AdminUser.class));
            }
        });


    }
}
