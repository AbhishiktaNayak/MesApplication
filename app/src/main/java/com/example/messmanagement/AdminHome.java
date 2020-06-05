package com.example.messmanagement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        LinearLayout parentLayout = findViewById(R.id.parent_layout);
        Typeface font = Typeface.createFromAsset(getAssets(), "solid.otf");
        for (View view: getViewsByTag(parentLayout, "icon")) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }

        RelativeLayout rlHome = findViewById(R.id.relative_home);
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AdminHome.class));
            }
        });

        RelativeLayout rlAddUser = findViewById(R.id.relative_add_user);
        rlAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AdminUser.class));
            }
        });

        RelativeLayout rlAddMenu = findViewById(R.id.relative_menu);
        rlAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AddMenu.class));
            }
        });

        RelativeLayout rlFeedback = findViewById(R.id.relative_feedback);
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,ManageFeedback.class));
            }
        });

        RelativeLayout rlLogout = findViewById(R.id.relative_logout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,Start.class));
            }
        });


    }

    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }
}
