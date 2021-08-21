package com.example.quizera;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import static com.example.quizera.DbQuery.ANSWERED;
import static com.example.quizera.DbQuery.NOT_VISITED;
import static com.example.quizera.DbQuery.UNANSWERED;

public class QuestionGridAdapter extends BaseAdapter {
    private int numofques;
    private Context context;

    public QuestionGridAdapter(Context context,int numofques) {
        this.numofques = numofques;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numofques;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View myview;
        if(view ==null)
        {
            myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_grid_item,parent,false);
        }
        else
        {
            myview = view;
        }

        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof QuestionsActivity)
                     ((QuestionsActivity) context).goToQuestion(position);
            }
        });


        TextView quesTV= myview.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(position+1));
        switch(DbQuery.g_quesList.get(position).getStatus())
        {
            case NOT_VISITED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.grey)));
                break;
            case ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.green)));
                break;
            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.red)));
                break;
        }

        return myview;
    }
}
