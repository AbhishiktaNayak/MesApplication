package com.example.messmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class ChooseMenu extends AppCompatActivity {

    private Button test;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);

        test= (Button) findViewById(R.id.button);
        relativeLayout= (RelativeLayout) findViewById(R.id.relative);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.test,null);

                popupWindow= new PopupWindow(container,400,400,true);
                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,500,500);

                setFinishOnTouchOutside(true);

            }
        });
    }

}
