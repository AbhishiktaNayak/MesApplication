package com.example.messmanagement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddMenu extends AppCompatActivity {

    private Spinner shostel,sWeekday;
    private EditText etBreakfast,etLunch,etSnacks,etDinner;
    private Button addBtn;
    private String hostel,day,breakfast,lunch,dinner,snacks;
    private LinearLayout linear_layout_2,parentLayout;
    private CardView optionsCard;
    private ImageView dropdown;
    private Animation moveDownAnim,moveUpAnim;
    private List<TextView> icons = new ArrayList<>();
    private Typeface font;
    private DatabaseReference mDatabase,databaseReference;
    private MenuClass menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        shostel=findViewById(R.id.shostel);
        sWeekday=findViewById(R.id.sWeekday);
        etBreakfast=findViewById(R.id.breakfast_edit_text);
        etLunch=findViewById(R.id.lunch_edit_text);
        etDinner=findViewById(R.id.dinner_edit_text);
        etSnacks=findViewById(R.id.snacks_edit_text);
        addBtn=findViewById(R.id.addbtn);

        parentLayout=findViewById(R.id.parent_layout);
        linear_layout_2=findViewById(R.id.linear_layout_2);
        optionsCard = findViewById(R.id.admin_options_card);
        dropdown=findViewById(R.id.dropdown);
        moveDownAnim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        moveUpAnim = AnimationUtils.loadAnimation(this, R.anim.move_up);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear_layout_2.getVisibility() == View.VISIBLE) {
                    linear_layout_2.startAnimation(moveUpAnim);
                    linear_layout_2.setVisibility(View.GONE);
                } else {
                    linear_layout_2.setVisibility(View.VISIBLE);
                    linear_layout_2.startAnimation(moveDownAnim);
                }
            }
        });

        font = Typeface.createFromAsset(getAssets(), "solid.otf");
        for (View view: getViewsByTag(parentLayout, "icon")) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }

        RelativeLayout rlHome = findViewById(R.id.relative_home);
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMenu.this,AdminHome.class));
            }
        });

        RelativeLayout rlAddUser = findViewById(R.id.relative_add_user);
        rlAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMenu.this,AdminUser.class));
            }
        });

        RelativeLayout rlAddMenu = findViewById(R.id.relative_menu);
        rlAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMenu.this,AddMenu.class));
            }
        });

        RelativeLayout rlFeedback = findViewById(R.id.relative_feedback);
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMenu.this,ManageFeedback.class));
            }
        });

        RelativeLayout rlLogout = findViewById(R.id.relative_logout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMenu.this,Start.class));
            }
        });


        ArrayAdapter<String> adapter=new ArrayAdapter<>(AddMenu.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.hostel_list));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        shostel.setAdapter(adapter);

        ArrayAdapter<String> adapter2=new ArrayAdapter<>(AddMenu.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.weekdays));
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        sWeekday.setAdapter(adapter2);

        shostel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hostel = shostel.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sWeekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = sWeekday.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    databaseReference=FirebaseDatabase.getInstance().getReference("Menu").child(hostel);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(day)){
                                addMenu();
                                Toast.makeText(AddMenu.this, " Item Updated!", Toast.LENGTH_SHORT).show();
                                clearedittext();
                            }
                            else {
                                addMenu();
                                Toast.makeText(AddMenu.this, "Item Added!", Toast.LENGTH_SHORT).show();
                                clearedittext();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddMenu.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }



    private void addMenu(){
        menus = new MenuClass(
                breakfast,
                lunch,
                snacks,
                dinner
        );
        mDatabase= FirebaseDatabase.getInstance().getReference("Menu").child(hostel);
        mDatabase.child(day).setValue(menus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG","Item Added successfully");
                } else {
                    Toast.makeText(AddMenu.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate() {
        breakfast=etBreakfast.getText().toString();
        lunch=etLunch.getText().toString();
        snacks=etSnacks.getText().toString();
        dinner=etDinner.getText().toString();

        if (hostel.equals("Select hostel")) {
            Toast.makeText(AddMenu.this, "Please select hostel", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (day.equals("Select week day")) {
            Toast.makeText(AddMenu.this, "Please select day", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(breakfast))
        {
            etBreakfast.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(lunch))
        {
            etLunch.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(snacks))
        {
            etSnacks.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(dinner))
        {
            etDinner.requestFocus();
            return false;
        }
        return true;
    }

    private void clearedittext() {
        sWeekday.setSelection(0);
        shostel.setSelection(0);
        etDinner.getText().clear();
        etSnacks.getText().clear();
        etLunch.getText().clear();
        etBreakfast.getText().clear();
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
