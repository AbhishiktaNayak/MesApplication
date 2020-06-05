package com.example.messmanagement;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private List<FeedbackClass> feedbacks;
    private List<FeedbackClass> deletedFeedbacks = new ArrayList<>();
    private Context mContext;
    private CheckBox checkBox;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
         void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //font = Typeface.createFromAsset(parent.getContext().getAssets(), "solid.otf");
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_feedback_table_row, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final View view = holder.mView;

        final LinearLayout baseLayout = view.findViewById(R.id.base_layout);
        final TextView rollno = view.findViewById(R.id.roll_number);
        final TextView hostel = view.findViewById(R.id.hostel);
        final TextView rating = view.findViewById(R.id.ratings);
        final TextView suggestion = view.findViewById(R.id.suggestions);
        final TextView foodItem = view.findViewById(R.id.food_item);
        checkBox = view.findViewById(R.id.check_box);

        baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    deletedFeedbacks.add(feedbacks.get(position));
                }
            }
        });

        FeedbackClass feedbackClass=feedbacks.get(position);

        rollno.setText(feedbackClass.getRoll());
        hostel.setText(feedbackClass.getHostel());
        rating.setText(feedbackClass.getRatings());
        suggestion.setText(feedbackClass.getSuggestion());
        foodItem.setText(feedbackClass.getFoodItem());

    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public FeedbackAdapter(Context context, OnItemClickListener onItemClickListener, List<FeedbackClass> feedbacks) {
        mContext = context;
        this.feedbacks = feedbacks;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public ViewHolder(View view){
            super(view);
            mView=view;
        }
    }

    public void changeItems(List<FeedbackClass> feedbacks) {
        this.feedbacks = feedbacks;
        notifyDataSetChanged();
    }

    public List<FeedbackClass> getDeletedFeedbacks() {
        return deletedFeedbacks;
    }

    public void clearDeleted() {
        checkBox.setChecked(false);
    }
}
