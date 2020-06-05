package com.example.messmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

public class Feedback extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout layout1,layout2;
    private NavigationView navigationView;
    private ImageView menu_icon;
    private DrawerLayout drawerLayout;
    private TextView tvFeedback, tvTime, tvComment, tvFoodItem;
    private EditText etComment;
    private RatingBar rbStar;
    private FeedbackAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase,ref,menuRef;
    private Button submit;
    private View nav_header;
    private String suggestion,roll,hostel,rating,day,fooditem,meal;
    private FeedbackClass feedback;
    private String selectedHostel;
    private TextView tvEmail,tvRollNo,tvHostel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        menuRef = FirebaseDatabase.getInstance().getReference("Menu");
        ref= FirebaseDatabase.getInstance().getReference().child("Students").child(firebaseUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference("feedbacks");

        navigationView=findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nav_header=navigationView.getHeaderView(0);
        tvRollNo=nav_header.findViewById(R.id.tvRollNo);
        tvEmail=nav_header.findViewById(R.id.tvEmail);
        tvHostel=nav_header.findViewById(R.id.tvHostel);
        tvEmail.setText(firebaseUser.getEmail());

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


        tvTime = findViewById(R.id.tvTime);
        tvFoodItem=findViewById(R.id.tvFoodItem);
        etComment = findViewById(R.id.etComment);
        rbStar = findViewById(R.id.rbStar);
        submit = findViewById(R.id.submit);
        drawerLayout = findViewById(R.id.drawer_layout);

        rbStar.setNumStars(5);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFeedback();
            }
        });

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        int dayno = c.get(Calendar.DAY_OF_WEEK);
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
        if(timeOfDay >= 6 && timeOfDay < 11){
            tvTime.setText("BREAKFAST");
            meal="breakfast";
        }else if(timeOfDay >= 11 && timeOfDay < 16){
            tvTime.setText("LUNCH");
            meal="lunch";
        }else if(timeOfDay >= 16 && timeOfDay < 20){
            tvTime.setText("SNACKS");
            meal="snacks";
        }else if(timeOfDay >= 20 && timeOfDay < 23){
            tvTime.setText("DINNER");
            meal="dinner";
        }
        else{
            tvTime.setText("MESS CLOSED");
            meal="dinner";
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                roll=dataSnapshot.child("sroll").getValue(String.class);
                hostel=dataSnapshot.child("shostel").getValue(String.class);
                tvRollNo.setText(roll);
                tvHostel.setText(hostel);

                menuRef.child(hostel).child(day).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fooditem=dataSnapshot.child(meal).getValue(String.class);
                        tvFoodItem.setText(fooditem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Feedback.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void addFeedback() {
        if(validate()) {
            String id = mDatabase.push().getKey();
            feedback = new FeedbackClass(
                    id,
                    roll,
                    hostel,
                    fooditem,
                    rating,
                    suggestion
            );
            String id2 = feedback.getId();
            Log.d("TAG","acc id " + id2);

            if(id != null) {
                mDatabase.child(id).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Feedback.this, "Submitted!", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "New account added" + feedback);
                            clearedittext();
                        } else {
                            Log.d("TAG", "Error: " + task.getException());
                        }
                    }
                });
            }
        } else {
            Log.d("TAG", "Invalid");
        }
    }

    private boolean validate() {
        suggestion = etComment.getText().toString();
        rating = Float.toString(rbStar.getRating());
        if(rbStar.getRating()==0)
        {
            Toast.makeText(Feedback.this, "Please rate the item.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(suggestion))
        {
            Toast.makeText(this, "Provide suggestion/feedback", Toast.LENGTH_SHORT).show();
            etComment.requestFocus();
            return false;
        }
        return true;
    }

    private void clearedittext() {
        etComment.getText().clear();
        rbStar.setRating(0);
    }

    private void configurenavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Menu menu = navigationView.getMenu();
                int item = menuItem.getItemId();

                if (item == (R.id.mHome)) {
                    startActivity(new Intent(Feedback.this, Home.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mChooseMess)) {
                    Toast.makeText(Feedback.this, "View different mess from Home Page.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Feedback.this,Home.class));
                }
                else if (item == (R.id.mMenu)) {
                    startActivity(new Intent(Feedback.this, DisplayMenu.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mNotify)) {
                    startActivity(new Intent(Feedback.this, BasicActivity.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mFeedback)) {
                    startActivity(new Intent(Feedback.this, Feedback.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mLogout)) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Feedback.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                else if (item==(R.id.mDeleteAccount))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Feedback.this);
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
                                        Toast.makeText(Feedback.this, "Account Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Feedback.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Feedback.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

}
