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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private static final String TAG ="AdminLogin" ;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private EditText aEmail,aPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth= FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        btnLogin=findViewById(R.id.btLogin);
        aEmail=findViewById(R.id.aEmail);
        aPassword=findViewById(R.id.aPassword);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=aEmail.getText().toString();
                String pw=aPassword.getText().toString();

                if(TextUtils.isEmpty(mail))
                {
                    Toast.makeText(AdminLogin.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(pw))
                {
                    Toast.makeText(AdminLogin.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(mail,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(AdminLogin.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent=new Intent(AdminLogin.this,AdminHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        if(firebaseUser!=null)
        {
            Intent i = new Intent(AdminLogin.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            Log.d(TAG,"onAuthStateChanged:signedOut");
        }
    }
}
