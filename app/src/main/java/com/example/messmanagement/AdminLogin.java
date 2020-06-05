package com.example.messmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AdminLogin extends AppCompatActivity {

    private static final String TAG ="AdminLogin" ;
    private AdminAccountClass account;
    private String username,pw;
    private DatabaseReference mD,mDatabase;
    private EditText aUsername,aPassword;
    private Button btnLogin;
    private Toolbar adminToolbar;
    private TextView admin_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mD= FirebaseDatabase.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("AdminUsers");

        btnLogin = findViewById(R.id.btnLogin);
        aUsername = findViewById(R.id.aUsername);
        aPassword = findViewById(R.id.aPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(checkIfUsernameExists(username,pw,dataSnapshot)){
                                Toast.makeText(AdminLogin.this, "Username exists!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminLogin.this,AdminHome.class));
                                finish();
                            }
                            else {
                                Toast.makeText(AdminLogin.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminLogin.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkIfUsernameExists(String username, String pw, DataSnapshot dataSnapshot) {
        Log.d("TAG","checkIfUsernameExists: checking if "+username+" already exists!");

        account =new AdminAccountClass();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            account.setUsername(ds.getValue(AdminAccountClass.class).getUsername());
            account.setPassword(ds.getValue(AdminAccountClass.class).getPassword());

            if(account.getUsername().equals(username) && account.getPassword().equals(pw)){
                Log.d("TAG","checkIfUsernameExists: FOUND A MATCH "+account.getUsername());
                return true;
            }
        }
        return false;
    }

    private boolean validate() {
        username=aUsername.getText().toString().trim();
        pw=aPassword.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            aUsername.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(pw))
        {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
            aPassword.requestFocus();
            return false;
        }
        return true;
    }


}
