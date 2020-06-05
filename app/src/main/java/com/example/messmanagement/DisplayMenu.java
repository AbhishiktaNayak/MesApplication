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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

public class DisplayMenu extends AppCompatActivity {

    private Toolbar toolbar;
    private View nav_header;
    private NavigationView navigationView;
    private ImageView menu_icon;
    private TextView tvEmail,tvRollNo,tvHostel;
    private LinearLayout monday_card,tuesday_card,wednesday_card,thursday_card,friday_card,saturday_card,sunday_card;
    private DrawerLayout drawerLayout;
    private Animation moveDownAnim,moveUpAnim;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference ref,mDatabase;
    private String selectedHostel,hostel,roll,day,breakfast,lunch,dinner,snacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference("Menu");
        ref= FirebaseDatabase.getInstance().getReference("Students").child(firebaseUser.getUid());

        sunday_card=findViewById(R.id.sunday_card);
        monday_card=findViewById(R.id.monday_card);
        tuesday_card=findViewById(R.id.tuesday_card);
        wednesday_card=findViewById(R.id.wednesday_card);
        thursday_card=findViewById(R.id.thursday_card);
        friday_card=findViewById(R.id.friday_card);
        saturday_card=findViewById(R.id.saturday_card);

        navigationView=findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        nav_header=navigationView.getHeaderView(0);
        tvRollNo=nav_header.findViewById(R.id.tvRollNo);
        tvEmail=nav_header.findViewById(R.id.tvEmail);
        tvHostel=nav_header.findViewById(R.id.tvHostel);
        tvEmail.setText(firebaseUser.getEmail());

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

        drawerLayout = findViewById(R.id.drawer_layout);
        configurenavigation();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                roll=dataSnapshot.child("sroll").getValue(String.class);
                hostel=dataSnapshot.child("shostel").getValue(String.class);
                tvRollNo.setText(roll);
                tvHostel.setText(hostel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayMenu.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        moveDownAnim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        moveUpAnim = AnimationUtils.loadAnimation(this, R.anim.move_up);

        RelativeLayout rlMonday=findViewById(R.id.relative_monday);
        rlMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textM = findViewById(R.id.breakfast_textM);
                TextView lunch_textM=findViewById(R.id.lunch_textM);
                TextView snacks_textM=findViewById(R.id.snacks_textM);
                TextView dinner_textM=findViewById(R.id.dinner_textM);
                day="MONDAY";
                animation(monday_card);
                setMenu(day,breakfast_textM,lunch_textM,snacks_textM,dinner_textM);
        }});

        RelativeLayout rlTuesday=findViewById(R.id.relative_tuesday);
        rlTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textT = findViewById(R.id.breakfast_textT);
                TextView lunch_textT=findViewById(R.id.lunch_textT);
                TextView snacks_textT=findViewById(R.id.snacks_textT);
                TextView dinner_textT=findViewById(R.id.dinner_textT);
                day="TUESDAY";
                animation(tuesday_card);
                setMenu(day,breakfast_textT,lunch_textT,snacks_textT,dinner_textT);
            }});

        RelativeLayout rlWednesday=findViewById(R.id.relative_wednesday);
        rlWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textW = findViewById(R.id.breakfast_textW);
                TextView lunch_textW=findViewById(R.id.lunch_textW);
                TextView snacks_textW=findViewById(R.id.snacks_textW);
                TextView dinner_textW=findViewById(R.id.dinner_textW);
                day="WEDNESDAY";
                animation(wednesday_card);
                setMenu(day,breakfast_textW,lunch_textW,snacks_textW,dinner_textW);
            }});

        RelativeLayout rlThursday=findViewById(R.id.relative_thursday);
        rlThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textTh = findViewById(R.id.breakfast_textTh);
                TextView lunch_textTh=findViewById(R.id.lunch_textTh);
                TextView snacks_textTh=findViewById(R.id.snacks_textTh);
                TextView dinner_textTh=findViewById(R.id.dinner_textTh);
                day="THURSDAY";
                animation(thursday_card);
                setMenu(day,breakfast_textTh,lunch_textTh,snacks_textTh,dinner_textTh);
            }});

        RelativeLayout rlFriday=findViewById(R.id.relative_friday);
        rlFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textF = findViewById(R.id.breakfast_textF);
                TextView lunch_textF=findViewById(R.id.lunch_textF);
                TextView snacks_textF=findViewById(R.id.snacks_textF);
                TextView dinner_textF=findViewById(R.id.dinner_textF);
                day="FRIDAY";
                animation(friday_card);
                setMenu(day,breakfast_textF,lunch_textF,snacks_textF,dinner_textF);
            }});

        RelativeLayout rlSaturday=findViewById(R.id.relative_saturday);
        rlSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textS = findViewById(R.id.breakfast_textS);
                TextView lunch_textS=findViewById(R.id.lunch_textS);
                TextView snacks_textS=findViewById(R.id.snacks_textS);
                TextView dinner_textS=findViewById(R.id.dinner_textS);
                day="SATURDAY";
                animation(saturday_card);
                setMenu(day,breakfast_textS,lunch_textS,snacks_textS,dinner_textS);
            }});

        RelativeLayout rlSunday=findViewById(R.id.relative_sunday);
        rlSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView breakfast_textSn = findViewById(R.id.breakfast_textSn);
                TextView lunch_textSn=findViewById(R.id.lunch_textSn);
                TextView snacks_textSn=findViewById(R.id.snacks_textSn);
                TextView dinner_textSn=findViewById(R.id.dinner_textSn);
                day="SUNDAY";
                animation(sunday_card);
                setMenu(day,breakfast_textSn,lunch_textSn,snacks_textSn,dinner_textSn);
            }});


    }


    private void animation(View view) {
        if(view.getVisibility()==View.GONE){
            view.startAnimation(moveDownAnim);
            view.setVisibility(View.VISIBLE);
        } else{
            view.startAnimation(moveUpAnim);
            view.setVisibility(View.GONE);
        }
    }

    private void setMenu(String day, final TextView view, final TextView view2, final TextView view3, final TextView view4) {
        mDatabase=FirebaseDatabase.getInstance().getReference("Menu").child(hostel);

        mDatabase.child(day).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                breakfast=dataSnapshot.child("breakfast").getValue(String.class);
                lunch=dataSnapshot.child("lunch").getValue(String.class);
                snacks=dataSnapshot.child("snacks").getValue(String.class);
                dinner=dataSnapshot.child("dinner").getValue(String.class);

                view.setText(breakfast);
                view2.setText(lunch);
                view3.setText(snacks);
                view4.setText(dinner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayMenu.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(DisplayMenu.this, Home.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mChooseMess)) {
                    popupwindow();
                }
                else if (item == (R.id.mMenu)) {
                    startActivity(new Intent(DisplayMenu.this, DisplayMenu.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mNotify)) {
                    startActivity(new Intent(DisplayMenu.this, BasicActivity.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mFeedback)) {
                    startActivity(new Intent(DisplayMenu.this, Feedback.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mLogout)) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(DisplayMenu.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                else if (item==(R.id.mDeleteAccount))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DisplayMenu.this);
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
                                        Toast.makeText(DisplayMenu.this, "Account Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DisplayMenu.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(DisplayMenu.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        LayoutInflater inflater = (LayoutInflater) DisplayMenu.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ArrayAdapter<String> adapter=new ArrayAdapter<>(DisplayMenu.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.hostel_list));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        hostelSpinner.setAdapter(adapter);

        hostelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHostel = hostelSpinner.getItemAtPosition(position).toString();
                Toast.makeText(DisplayMenu.this, "Selected Hostel at: "+selectedHostel, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DisplayMenu.this, "Please select hostel", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateMenu();
                    window.dismiss();
                }
            }
        });
    }

    private void updateMenu() {
        hostel=selectedHostel;
        monday_card.startAnimation(moveUpAnim);
        monday_card.setVisibility(View.GONE);
        tuesday_card.startAnimation(moveUpAnim);
        tuesday_card.setVisibility(View.GONE);
        wednesday_card.startAnimation(moveUpAnim);
        wednesday_card.setVisibility(View.GONE);
        thursday_card.startAnimation(moveUpAnim);
        thursday_card.setVisibility(View.GONE);
        friday_card.startAnimation(moveUpAnim);
        friday_card.setVisibility(View.GONE);
        saturday_card.startAnimation(moveUpAnim);
        saturday_card.setVisibility(View.GONE);
        sunday_card.startAnimation(moveUpAnim);
        sunday_card.setVisibility(View.GONE);
    }



}
