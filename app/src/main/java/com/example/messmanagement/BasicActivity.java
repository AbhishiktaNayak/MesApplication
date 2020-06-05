package com.example.messmanagement;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.messmanagement.data.AlarmReminderContract;
import com.example.messmanagement.data.AlarmReminderDbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BasicActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference().child("Students").child(firebaseUser.getUid());

        navigationView=findViewById(R.id.navigation);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
                Toast.makeText(BasicActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(BasicActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void configurenavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Menu menu = navigationView.getMenu();
                int item = menuItem.getItemId();

                if (item == (R.id.mHome)) {
                    startActivity(new Intent(BasicActivity.this, Home.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mChooseMess)) {
                    Toast.makeText(BasicActivity.this, "View different mess from Home Page.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BasicActivity.this,Home.class));
                }
                else if (item == (R.id.mMenu)) {
                    startActivity(new Intent(BasicActivity.this, DisplayMenu.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mNotify)) {
                    startActivity(new Intent(BasicActivity.this, BasicActivity.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mFeedback)) {
                    startActivity(new Intent(BasicActivity.this, Feedback.class));
                    drawerLayout.closeDrawers();
                }
                else if (item == (R.id.mLogout)) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(BasicActivity.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                else if (item==(R.id.mDeleteAccount))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(BasicActivity.this);
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
                                        Toast.makeText(BasicActivity.this, "Account Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(BasicActivity.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(BasicActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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