package com.example.quizera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class StartTestActivity extends AppCompatActivity {

    private TextView catName,totalQ,Ttime;
    ImageView backB;
    Button startTestB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        DbQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess()
            {
                setData();

            }

            @Override
            public void onFailure() {

            }
        });

    }

    private void init()
    {
        catName = findViewById(R.id.st_cat_name);
        totalQ = findViewById(R.id.st_total_ques);
        startTestB = findViewById(R.id.start_testB);
        backB= findViewById(R.id.st_backB);
        Ttime= findViewById(R.id.st_time);


        backB.setOnClickListener(view -> StartTestActivity.this.finish());
        startTestB.setOnClickListener(view -> {
            Intent intent = new Intent(StartTestActivity.this,QuestionsActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void setData()
    {
        catName.setText(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        totalQ.setText(String.valueOf(DbQuery.g_quesList.size()));
       Ttime.setText(String.valueOf(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getTime())+":00 min");
    }
}