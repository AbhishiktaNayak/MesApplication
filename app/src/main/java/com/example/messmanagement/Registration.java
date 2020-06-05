package com.example.messmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity{

    private EditText semail,spw,sconfirm,sname,sroll;
    private Spinner shostel;
    private String hostel;
    private FirebaseAuth mAuth;
    private String name,roll,pw,cpw,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        Button btRegister = findViewById(R.id.btRegister);
        sroll=findViewById(R.id.sRoll);
        sname=findViewById(R.id.sName);
        shostel=findViewById(R.id.shostel);
        semail=findViewById(R.id.semail);
        spw=findViewById(R.id.sPassword);
        sconfirm=findViewById(R.id.sConfirm);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(Registration.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.hostel_list));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        shostel.setAdapter(adapter);

        shostel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hostel = shostel.getItemAtPosition(position).toString();
                Toast.makeText(Registration.this, "Selected Hostel at: "+hostel, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    mAuth.createUserWithEmailAndPassword(email, pw)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Student student = new Student(
                                                name,
                                                roll,
                                                hostel,
                                                email
                                        );
                                        FirebaseDatabase.getInstance().getReference("Students")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Registration.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registration.this, Home.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }

        });


    }

    private boolean validate() {
        name = sname.getText().toString();
        roll = sroll.getText().toString();
        email = semail.getText().toString();
        pw = spw.getText().toString();
        cpw = sconfirm.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(Registration.this, "Please enter name", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (TextUtils.isEmpty(roll)) {
                Toast.makeText(Registration.this, "Please enter roll number", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (hostel.equals("Select hostel")) {
                Toast.makeText(Registration.this, "Please select hostel", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (TextUtils.isEmpty(email)) {
                Toast.makeText(Registration.this, "Please enter email", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (TextUtils.isEmpty(pw)) {
                Toast.makeText(Registration.this, "Please enter password", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(!cpw.equals(pw))
            {
                Toast.makeText(this, "Confirm pw doesn't match!", Toast.LENGTH_SHORT).show();
                return false;
            }
        return true;
    }


}
