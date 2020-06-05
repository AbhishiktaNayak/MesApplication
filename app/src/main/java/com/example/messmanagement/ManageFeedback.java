package com.example.messmanagement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageFeedback extends AppCompatActivity {

    private FeedbackAdapter adapter;
    private DatabaseReference mDatabase;
    private LinearLayout deletelayout,linear_layout_2,parentLayout;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private boolean editing = false;
    private String editingId = "-1";
    private TextView searchText,prevButton,nextButton,showingEntriesText;
    private List<FeedbackClass> all = new ArrayList<>(), feedbacks = new ArrayList<>();
    private static final List<Integer> numberEntriesList = Arrays.asList(10,25,50,100);
    private int numberEntries = 10, showingPage = 1, totalEntries = 0;
    private ScrollView baseScrollView;
    private Animation moveDownAnim,moveUpAnim;
    private CardView optionsCard;
    private ImageView dropdown;
    private List<TextView> icons = new ArrayList<>();
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedback);

        mDatabase= FirebaseDatabase.getInstance().getReference("feedbacks");
        mDatabase.addValueEventListener(eventListener);

        baseScrollView = findViewById(R.id.base_scroll_view);
        recyclerView = findViewById(R.id.feedback_recycler_view);
        deletelayout=findViewById(R.id.delete);
        spinner=findViewById(R.id.entries_spinner);
        searchText = findViewById(R.id.search_edit_text);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        showingEntriesText = findViewById(R.id.showing_entries);

        parentLayout=findViewById(R.id.parent_layout);
        linear_layout_2=findViewById(R.id.linear_layout_2);
        optionsCard = findViewById(R.id.admin_options_card);
        dropdown=findViewById(R.id.dropdown);
        moveDownAnim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        moveUpAnim = AnimationUtils.loadAnimation(this, R.anim.move_up);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear_layout_2.getVisibility() == View.VISIBLE) {
                    linear_layout_2.startAnimation(moveUpAnim);
                    linear_layout_2.setVisibility(View.GONE);
                } else {
                    linear_layout_2.setVisibility(View.VISIBLE);
                    linear_layout_2.startAnimation(moveDownAnim);
                }
            }
        });

        font = Typeface.createFromAsset(getAssets(), "solid.otf");
        for (View view: getViewsByTag(parentLayout, "icon")) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FeedbackClass feedbackClass = new FeedbackClass();
        feedbacks.add(feedbackClass);

       adapter = new FeedbackAdapter(this, new FeedbackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Log.d("TAG", "Edit Item Clicked at: " + position);
            }
        }, feedbacks);
       recyclerView.setAdapter(adapter);

       deletelayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               List<FeedbackClass> deletedFeedbacks = adapter.getDeletedFeedbacks();
               Log.d("TAG", "Delete clicked");
               delete(deletedFeedbacks);
           }
       });

       prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });

       nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

       spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, numberEntriesList));

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               numberEntries = numberEntriesList.get(i);
               Log.d("TAG", "Number of entries changed to: " + numberEntries);
               int entriesShown = Math.min(totalEntries, numberEntries);
               List<FeedbackClass> feedbackTemp = feedbacks.subList(0, entriesShown);
               adapter.changeItems(feedbackTemp);
               showingPage = 1;
               changeEntriesText(showingPage, totalEntries);
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {
                numberEntries = 10;
            }
       });
       searchText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               search(charSequence.toString());
           }
           @Override
           public void afterTextChanged(Editable editable) {
           }
       });

        RelativeLayout rlHome = findViewById(R.id.relative_home);
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFeedback.this,AdminHome.class));
            }
        });

        RelativeLayout rlAddUser = findViewById(R.id.relative_add_user);
        rlAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFeedback.this,AdminUser.class));
            }
        });

        RelativeLayout rlAddMenu = findViewById(R.id.relative_menu);
        rlAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFeedback.this,AddMenu.class));
            }
        });

        RelativeLayout rlFeedback = findViewById(R.id.relative_feedback);
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFeedback.this,ManageFeedback.class));
            }
        });

        RelativeLayout rlLogout = findViewById(R.id.relative_logout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFeedback.this,Start.class));
            }
        });

    }

    private void delete(List<FeedbackClass> feedbacks) {
        for (FeedbackClass feedbackClass : feedbacks) {
            adapter.clearDeleted();
            mDatabase.child(feedbackClass.getId()).removeValue();
        }
        Log.d("TAG", "Deleted");
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void search(String key) {
        if (key == null || key.isEmpty()) {
            feedbacks.clear();
            feedbacks.addAll(all);
        } else {
            List<FeedbackClass> tempFeedbacks = new ArrayList<>();
            for (FeedbackClass feedbackClass : all) {
                if (feedbackClass.getRoll().contains(key)) {
                    tempFeedbacks.add(feedbackClass);
                }
            }
            feedbacks = tempFeedbacks;
        }
        adapter.changeItems(feedbacks);
        totalEntries = feedbacks.size();
        int entries = Math.min(numberEntries, totalEntries);
        List<FeedbackClass> temp = feedbacks.subList(0,entries);
        adapter.changeItems(temp);
        changeEntriesText(showingPage, totalEntries);
    }

    private void prev() {
        if(showingPage > 1) {
            --showingPage;
            int start = (showingPage-1)*numberEntries;
            int end = Math.min(start + numberEntries, totalEntries);
            List<FeedbackClass> feedbacksTemp = feedbacks.subList(start, end);
            adapter.changeItems(feedbacksTemp);
            changeEntriesText(showingPage, totalEntries);
        }
    }

    private void next() {
        int pages = totalEntries / numberEntries;
        if(showingPage < (pages + 1)) {
            ++showingPage;
            int start = (showingPage-1)*numberEntries;
            int end = Math.min(start + numberEntries, totalEntries);
            List<FeedbackClass> feedbacksTemp = feedbacks.subList(start, end);
            adapter.changeItems(feedbacksTemp);
            changeEntriesText(showingPage, totalEntries);
        }
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            all.clear();
            feedbacks.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Log.d("TAG", "All exams: ");
                try {
                    FeedbackClass feedbackClass = snapshot.getValue(FeedbackClass.class);
                    Log.d("TAG", feedbackClass.toString());
                    all.add(feedbackClass);
                    feedbacks.add(feedbackClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            totalEntries = feedbacks.size();
            int entries = (totalEntries <= numberEntries) ? totalEntries : numberEntries;
            List<FeedbackClass> feedbacksTemp = feedbacks.subList(0, entries);
            adapter.changeItems(feedbacksTemp);
            changeEntriesText(showingPage, totalEntries);
//            showingEntriesText.setText("Showing " + (showingPage-1) * numberEntries + " to " + ((showingPage-1) * numberEntries + entries) + " of " + totalEntries + " entries");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("TAG", "Database Error: " + databaseError.toString());
        }
    };

    private void changeEntriesText(int startPage, int entries) {
        int start = (startPage-1) * numberEntries;
        int end = Math.min(entries, start + numberEntries);
        showingEntriesText.setText("Showing " + start + " to " + end + " of " + entries);
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
