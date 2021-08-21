package com.example.quizera;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.view.View.OnClickListener;

import static com.example.quizera.DbQuery.ANSWERED;
import static com.example.quizera.DbQuery.UNANSWERED;
import static com.example.quizera.DbQuery.g_quesList;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<QuestionModel> questionLists;

    public QuestionAdapter(List<QuestionModel> questionLists) {
        this.questionLists = questionLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder viewHolder, int i) {
        viewHolder.setdata(i);

    }

    @Override
    public int getItemCount() {
        return questionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final  Button OptionD,OptionA,OptionC,OptionB;
        private Button prevSelectedB;
        private final TextView ques;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.tv_question);
            OptionA = itemView.findViewById(R.id.optionA);
            OptionB = itemView.findViewById(R.id.optionB);
            OptionC = itemView.findViewById(R.id.optionC);
            OptionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;

        }


        private void setdata(final  int pos) {
            ques.setText(questionLists.get(pos).getQuestion());
            OptionA.setText(questionLists.get(pos).getOptionA());
            OptionB.setText(questionLists.get(pos).getOptionB());
            OptionC.setText(questionLists.get(pos).getOptionC());
            OptionD.setText(questionLists.get(pos).getOptionD());

            setOption(OptionA, 1, pos);
            setOption(OptionB, 2, pos);
            setOption(OptionC, 3, pos);
            setOption(OptionD, 4, pos);

            OptionA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(OptionA, 1, pos);
                }
            });
            OptionB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(OptionB, 2, pos);
                }
            });
            OptionC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(OptionC, 3, pos);
                }
            });
            OptionD.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(OptionD, 4, pos);
                }
            });


        }
        private void selectOption(Button btn, int option_num,int quesID)
        {
            if(prevSelectedB==null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
                changestatus(quesID , ANSWERED);
                prevSelectedB = btn;
            }
            else
            {
                if(prevSelectedB.getId() == btn.getId())
                {
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);
                    changestatus(quesID , UNANSWERED);
                    prevSelectedB = null;
                }
                else
                {
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
                    changestatus(quesID , ANSWERED);
                    prevSelectedB = btn;
                }
            }
        } private void changestatus(int id,int status)
        {
            g_quesList.get(id).setStatus(status);
        }
        private void setOption(Button btn, int option_num, int quesID)
        {
            if(DbQuery.g_quesList.get(quesID).getSelectedAns()==option_num)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }
    }
}

