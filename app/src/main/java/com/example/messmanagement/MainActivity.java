package com.example.messmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Button btLogin,btSendEmail;
    private TextView tvNewUser,tvForgotPassword;
    private EditText etEmail,etPassword,etEmail2;
    private LinearLayout layout1,layout2;
    //ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        btLogin=findViewById(R.id.btLogin);
        btSendEmail=findViewById(R.id.btSendEmail);
        etEmail=findViewById(R.id.etEmail);
        etEmail2=findViewById(R.id.etEmail2);
        etPassword=findViewById(R.id.etPassword);
        tvNewUser=findViewById(R.id.tvNewuser);
        tvForgotPassword=findViewById(R.id.tvForgotPassword);
        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        //progressBar=findViewById(R.id.pBar);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=etEmail.getText().toString();
                String pw=etPassword.getText().toString();

                if(TextUtils.isEmpty(mail))
                {
                    Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(pw))
                {
                    Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(mail,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent=new Intent(MainActivity.this,Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        if(firebaseUser!=null)
        {
            Intent i = new Intent(MainActivity.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            Log.d(TAG,"onAuthStateChanged:signedOut");
        }

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
            }
        });

        btSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etEmail2.getText().toString();
                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(MainActivity.this, "Please enter valid Email Id!", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //progressBar.setVisibility(View.VISIBLE);
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "You can check your email account now.", Toast.LENGTH_LONG).show();
                                layout2.setVisibility(View.GONE);
                                //progressBar.setVisibility(View.GONE);
                                layout1.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
