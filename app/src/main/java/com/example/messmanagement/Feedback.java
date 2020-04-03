package com.example.messmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    TextView tvFeedback, tvTime, tvComment, tvSabji;
    EditText etComment;
    RatingBar rbStar;
    ImageView ivUp, ivDown;
    Button submit;
    int v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        tvFeedback = findViewById(R.id.tvFeedback);
        tvComment = findViewById(R.id.tvComment);
        tvTime = findViewById(R.id.tvTime);
        tvSabji = findViewById(R.id.tvSabji);
        etComment = findViewById(R.id.etComment);
        rbStar = findViewById(R.id.rbStar);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        submit = findViewById(R.id.submit);

        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmnt = etComment.getText().toString().trim();
                float rating = rbStar.getNumStars();
                int thumb;
                if (v == R.id.ivUp) {
                    thumb = 1;
                } else if (v == R.id.ivDown) {
                    thumb = 0;
                } else thumb = -1;
                if (thumb == -1) {
                    Toast.makeText(Feedback.this, "Select thumbs", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Feedback.this, "Submitted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivUp){
            ivDown.setColorFilter(Color.BLACK);
            ivUp.setColorFilter(Color.GREEN);
            v=view.getId();
        }
        else if (view.getId() == R.id.ivDown){
            ivUp.setColorFilter(Color.BLACK);
            ivDown.setColorFilter(Color.RED);
            v = view.getId();
        }
    }

}
