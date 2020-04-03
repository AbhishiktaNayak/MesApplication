package com.example.messmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private View nav_header;
    private NavigationView navigationView;
    private TextView tvEmail,tvRollNo;
    private ImageView menu_icon;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef,ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        rootRef=FirebaseDatabase.getInstance().getReference();
        ref=rootRef.child("Students").child(firebaseUser.getUid());


        navigationView=findViewById(R.id.navigation);
        progressBar=findViewById(R.id.progressBar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nav_header=navigationView.getHeaderView(0);
        tvRollNo=nav_header.findViewById(R.id.tvRollNo);
        tvEmail=nav_header.findViewById(R.id.tvEmail);
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

        if(firebaseUser==null)
        {
            Intent i = new Intent(Home.this, MainActivity.class);
            finish();
            startActivity(i);
        }

    }

    private void configurenavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Menu menu = navigationView.getMenu();
                int item = menuItem.getItemId();

                if (item == (R.id.mHome)) {
                    startActivity(new Intent(Home.this, Home.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mChooseMess)) {
                    popupwindow();
                }
                else if (item == (R.id.mMenu)) {
                    startActivity(new Intent(Home.this, Empty.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mNotify)) {
                    startActivity(new Intent(Home.this, Empty.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mFeedback)) {
                    startActivity(new Intent(Home.this, Feedback.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mLogout)) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Home.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                else if (item == (R.id.mAdmin)) {
                    startActivity(new Intent(Home.this, AdminHome.class));
                    drawerLayout.closeDrawers();
                }
                else if (item==(R.id.mDeleteAccount))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.VISIBLE);
                            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()){
                                        Toast.makeText(Home.this, "Account Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Home.this,MainActivity.class);
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

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String roll=dataSnapshot.child("sroll").getValue(String.class).toString();
                tvRollNo.setText(roll);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void popupwindow() {
       // startActivity(new Intent(Home.this,Empty.class));
       // drawerLayout.closeDrawers();
    }
}
