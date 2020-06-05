package com.example.messmanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private View nav_header;
    private NavigationView navigationView;
    private ImageView menu_icon;
    private TextView tvEmail,tvRollNo,tvHostel,breakfast_text,lunch_text,dinner_text,snacks_text;
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef,ref,mDatabase;
    private String selectedHostel,hostel,day,roll,breakfast,lunch,snacks,dinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        ref= rootRef.child("Students").child(firebaseUser.getUid());

        navigationView=findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nav_header=navigationView.getHeaderView(0);
        tvRollNo=nav_header.findViewById(R.id.tvRollNo);
        tvEmail=nav_header.findViewById(R.id.tvEmail);
        tvHostel=nav_header.findViewById(R.id.tvHostel);
        breakfast_text=findViewById(R.id.breakfast_text);
        lunch_text=findViewById(R.id.lunch_text);
        snacks_text=findViewById(R.id.snacks_text);
        dinner_text=findViewById(R.id.dinner_text);


        drawerLayout = findViewById(R.id.drawer_layout);
        configurenavigation();

        menu_icon=findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }

            }
        });

        Calendar calendar= Calendar.getInstance();
        int dayno = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayno)
        {
            case 1: day="SUNDAY"; break;
            case 2: day="MONDAY"; break;
            case 3: day="TUESDAY"; break;
            case 4: day="WEDNESDAY"; break;
            case 5: day="THURSDAY"; break;
            case 6: day="FRIDAY"; break;
            case 7: day="SATURDAY"; break;
        }

        tvEmail.setText(firebaseUser.getEmail());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                roll=dataSnapshot.child("sroll").getValue(String.class);
                hostel=dataSnapshot.child("shostel").getValue(String.class);
                tvRollNo.setText(roll);
                tvHostel.setText(hostel);
                itemDisplay(hostel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void itemDisplay(String hostel) {
        mDatabase=FirebaseDatabase.getInstance().getReference("Menu").child(hostel).child(day);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                breakfast=dataSnapshot.child("breakfast").getValue(String.class);
                lunch=dataSnapshot.child("lunch").getValue(String.class);
                snacks=dataSnapshot.child("snacks").getValue(String.class);
                dinner=dataSnapshot.child("dinner").getValue(String.class);

                breakfast_text.setText(breakfast);
                lunch_text.setText(lunch);
                snacks_text.setText(snacks);
                dinner_text.setText(dinner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configurenavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Menu menu = navigationView.getMenu();
                int item = menuItem.getItemId();

                if (item == (R.id.mHome)) {
                    startActivity(new Intent(Home.this, Home.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mChooseMess)) {
                    popupwindow();
                }
                else if (item == (R.id.mMenu)) {
                    startActivity(new Intent(Home.this, DisplayMenu.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mNotify)) {
                    startActivity(new Intent(Home.this, BasicActivity.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mFeedback)) {
                    startActivity(new Intent(Home.this, Feedback.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mLogout)) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Home.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                else if (item==(R.id.mDeleteAccount))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.removeValue();
                            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(Home.this, "Account Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Home.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Home.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                    dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                return false;
            }
        });
    }

    private void popupwindow() {
        LayoutInflater inflater = (LayoutInflater) Home.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_choose_hostel, null);
        final PopupWindow window = new PopupWindow(layout,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);

        View container = window.getContentView().getRootView();
        if (container != null) {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.8f;
            if (wm != null) {
                wm.updateViewLayout(container, p);
            }
        }
        chooseMess(layout,window);

        TextView tvClose = layout.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        drawerLayout.closeDrawers();
    }

    private void chooseMess(View layout, final PopupWindow window) {
        final Spinner hostelSpinner=layout.findViewById(R.id.hostelSpinner);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(Home.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.hostel_list));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        hostelSpinner.setAdapter(adapter);

        hostelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHostel = hostelSpinner.getItemAtPosition(position).toString();
                Toast.makeText(Home.this, "Selected Hostel at: "+selectedHostel, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btSubmit=layout.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedHostel.equals("Select hostel")){
                    Toast.makeText(Home.this, "Please select hostel", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateHome();
                    window.dismiss();
                }
            }
        });
    }

    private void updateHome() {
        mDatabase=FirebaseDatabase.getInstance().getReference("Menu").child(selectedHostel).child(day);
        itemDisplay(selectedHostel);
    }

}
