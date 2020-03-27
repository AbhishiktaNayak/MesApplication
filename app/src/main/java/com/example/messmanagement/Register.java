package com.example.messmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity{

    private EditText semail,spw,sconfirm,sname,sroll;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        Button btRegister = findViewById(R.id.btRegister);
        sroll=findViewById(R.id.sRoll);
        sname=findViewById(R.id.sName);
        semail=findViewById(R.id.sEmail);
        spw=findViewById(R.id.sPassword);
        sconfirm=findViewById(R.id.sConfirm);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name=sname.getText().toString();
                final String roll=sroll.getText().toString();
                final String email = semail.getText().toString();
                final String pw = spw.getText().toString();
                final String cpw = sconfirm.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(pw)) {
                    Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                if (pw.equals(cpw)) {

                    mAuth.createUserWithEmailAndPassword(email, pw)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Student student=new Student(
                                                name,
                                                roll,
                                                email
                                        );
                                        FirebaseDatabase.getInstance().getReference("Students")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                       if(task.isSuccessful())
                                                       {
                                                           Toast.makeText(Register.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                                           startActivity(new Intent(Register.this,Home.class));
                                                       }
                                                       else{
                                                           Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                       }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(Register.this, "Confirm password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
