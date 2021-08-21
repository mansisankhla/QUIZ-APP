package com.example.quizera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV,timeTV,totalQTV,correctQTV,wrongQTV,unattemptedQTV;
      Button reAttemptB,viewAnsB;
      private long timeTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        init();
        loadData();
        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reattempt();
            }
        });
        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this,AnswersActivity.class);
                startActivity(intent);

            }
        });
    }
    private void init()
    {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.timeTaken);
        totalQTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unattemptedQTV = findViewById(R.id.un_attemptedQ);
        reAttemptB = findViewById(R.id.reAttemptB);
        viewAnsB = findViewById(R.id.view_answerB);
    }
    private void loadData()
    {
        int correctQ=0, wrongQ=0,unattemptQ=0;

        for(int i=0; i < DbQuery.g_quesList.size();i++)
        {
            if(DbQuery.g_quesList.get(i).getSelectedAns() == -1)
            {
                unattemptQ ++;
            }
            else
            {
                if(DbQuery.g_quesList.get(i).getSelectedAns() == DbQuery.g_quesList.get(i).getCorrectAns())
                {
                    correctQ ++;
                }
                else
                {
                    wrongQ ++;
                }
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));
        totalQTV.setText(String.valueOf(DbQuery.g_quesList.size()));
        int finalScore = (correctQ*100)/(DbQuery.g_quesList.size());
        scoreTV.setText(String.valueOf(finalScore));
        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);

        String time = String.format("%02d : %02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))

        );
      timeTV.setText(time);
    }
   private void reattempt()
   {
       for(int i=0 ;i<  DbQuery.g_quesList.size(); i++)
       {
           DbQuery.g_quesList.get(i).setSelectedAns(-1);
           //DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);
       }
       Intent intent = new Intent(ScoreActivity.this,StartTestActivity.class);
       startActivity(intent);
       finish();
   }

}