package com.example.messmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminUser extends AppCompatActivity {

    private EditText etFullname,etUser,etPw,etConfirmpw,etUser2,etPw2,etConfirmpw2;
    private AdminAccountClass account;
    private Animation moveDownAnim, moveUpAnim;
    private ImageView dropdown;
    private LinearLayout linear_layout_2,parentLayout;
    private DatabaseReference mDatabase,mD;
    private Drawable editTextBlue,editTextRed;
    private String name,username,password,confirmpw,username2,password2,confirmpw2;
    private List<TextView> icons = new ArrayList<>();
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        mD=FirebaseDatabase.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("AdminUsers");

        etFullname=findViewById(R.id.name_edit_text);
        etUser=findViewById(R.id.username_edit_text);
        etPw=findViewById(R.id.password_edit_text);
        etConfirmpw=findViewById(R.id.confirmpw_edit_text);
        etUser2=findViewById(R.id.username_edit_text2);
        etPw2=findViewById(R.id.password_edit_text2);
        etConfirmpw2=findViewById(R.id.confirmpw_edit_text2);
        dropdown=findViewById(R.id.dropdown);
        Button btAdd = findViewById(R.id.addUserbtn);
        Button btDelete = findViewById(R.id.btnDelete);

        parentLayout=findViewById(R.id.parent_layout);
        linear_layout_2=findViewById(R.id.linear_layout_2);
        moveDownAnim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        moveUpAnim = AnimationUtils.loadAnimation(this, R.anim.move_up);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear_layout_2.getVisibility() == View.GONE) {
                    linear_layout_2.setVisibility(View.VISIBLE);
                    linear_layout_2.startAnimation(moveDownAnim);
                } else {
                   linear_layout_2.startAnimation(moveUpAnim);
                   linear_layout_2.setVisibility(View.GONE);
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
                startActivity(new Intent(AdminUser.this,AdminHome.class));
            }
        });

        RelativeLayout rlAddUser = findViewById(R.id.relative_add_user);
        rlAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUser.this,AdminUser.class));
            }
        });

        RelativeLayout rlAddMenu = findViewById(R.id.relative_menu);
        rlAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUser.this,AddMenu.class));
            }
        });

        RelativeLayout rlFeedback = findViewById(R.id.relative_feedback);
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUser.this,ManageFeedback.class));
            }
        });

        RelativeLayout rlLogout = findViewById(R.id.relative_logout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUser.this,Start.class));
            }
        });


        editTextBlue = ContextCompat.getDrawable(this, R.drawable.edittext_highlight_blue);
        editTextRed = ContextCompat.getDrawable(this, R.drawable.edittext_highlight_red);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccount();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccountForDelete();
            }
        });

    }


    private void checkAccount() {
        if(validateAdd()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(checkIfUsernameExists(username,dataSnapshot)){
                        Toast.makeText(AdminUser.this, "Username Already exists! Try another username.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addAccount();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AdminUser.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkIfUsernameExists(String username,DataSnapshot dataSnapshot){
        Log.d("TAG","checkIfUsernameExists: checking if "+username+" already exists!");

        account =new AdminAccountClass();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            account.setUsername(ds.getValue(AdminAccountClass.class).getUsername());

            if(account.getUsername().equals(username)){
                Log.d("TAG","checkIfUsernameExists: FOUND A MATCH "+account.getUsername());
                return true;
            }
        }
        return false;
    }

    private void addAccount() {
        if(validateAdd()) {
            String id = mDatabase.push().getKey();
            account = new AdminAccountClass(
                    id,
                    name,
                    username
            );
            account.setPassword(password);
            String id2 = account.getId();
            Log.d("TAG","acc id " + id2);

            if(id != null) {
                mDatabase.child(id).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminUser.this, "Added", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "New account added" + account);
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

    private void checkAccountForDelete() {
        if(validateDelete()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(checkIfUsernameExistsDelete(username2,password2,dataSnapshot)){
                        deleteAccount();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AdminUser.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteAccount() {
        Query query=mD.child("AdminUsers").orderByChild("username").equalTo(username2);
        Log.d("TAG","query successful");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                    Toast.makeText(AdminUser.this, "User Deleted!", Toast.LENGTH_SHORT).show();
                    clearedittext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG","onCancelled",databaseError.toException());
            }
        });

    }

    private boolean checkIfUsernameExistsDelete(String username2,String password2,DataSnapshot dataSnapshot){
        Log.d("TAG","checkIfUsernameExists: checking if "+username2+" already exists!");

        account =new AdminAccountClass();

        for(DataSnapshot ds:dataSnapshot.getChildren()){
            account.setUsername(ds.getValue(AdminAccountClass.class).getUsername());
            account.setPassword(ds.getValue(AdminAccountClass.class).getPassword());

            if(account.getUsername().equals(username2) && account.getPassword().equals(password2)){
                Log.d("TAG","checkIfUsernameExists: FOUND A MATCH "+account.getUsername()+" "+account.getPassword());
                return true;
            }
            else if(account.getUsername().equals(username2) && !account.getPassword().equals(password2)){
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this, "Incorrect username", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean validateAdd() {
        setEditTextBackground(editTextBlue);
        name = etFullname.getText().toString();
        username = etUser.getText().toString();
        password = etPw.getText().toString();
        confirmpw =etConfirmpw.getText().toString();

        if (TextUtils.isEmpty(name)) {
            setEditTextBackground(editTextRed, etFullname);
            etFullname.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            setEditTextBackground(editTextRed, etUser);
            etUser.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            setEditTextBackground(editTextRed, etPw);
            etPw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmpw)) {
            setEditTextBackground(editTextRed, etConfirmpw);
            etConfirmpw.requestFocus();
            return false;
        }
        if(!password.equals(confirmpw)){
            Toast.makeText(this, "Confirm Password does not match.", Toast.LENGTH_SHORT).show();
            etConfirmpw.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateDelete() {
        setEditTextBackground(editTextBlue);
        username2 = etUser2.getText().toString();
        password2 = etPw2.getText().toString();
        confirmpw2 = etConfirmpw2.getText().toString();

        if (TextUtils.isEmpty(username2)) {
            setEditTextBackground(editTextRed, etUser2);
            etUser2.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password2)) {
            setEditTextBackground(editTextRed, etPw2);
            etPw2.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmpw2)) {
            setEditTextBackground(editTextRed, etConfirmpw2);
            etConfirmpw2.requestFocus();
            return false;
        }
        if(!password2.equals(confirmpw2)){
            Toast.makeText(this, "Confirm Password does not match.", Toast.LENGTH_SHORT).show();
            etConfirmpw2.requestFocus();
            return false;
        }
        return true;
    }

    private void setEditTextBackground(Drawable drawable) {
        etFullname.setBackground(drawable);
        etUser.setBackground(drawable);
        etPw.setBackground(drawable);
        etConfirmpw.setBackground(drawable);
        etUser2.setBackground(drawable);
        etPw2.setBackground(drawable);
        etConfirmpw2.setBackground(drawable);
    }

    private void setEditTextBackground(Drawable drawable, EditText editText) {
        editText.setBackground(drawable);
    }

    private void clearedittext() {
        etUser.getText().clear();
        etPw.getText().clear();
        etConfirmpw.getText().clear();
        etFullname.getText().clear();
        etUser2.getText().clear();
        etPw2.getText().clear();
        etConfirmpw2.getText().clear();
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
