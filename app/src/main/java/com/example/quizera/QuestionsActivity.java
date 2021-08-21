package com.example.quizera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static com.example.quizera.DbQuery.NOT_VISITED;
import static com.example.quizera.DbQuery.UNANSWERED;
import static com.example.quizera.DbQuery.g_catList;
import static com.example.quizera.DbQuery.g_quesList;
import static com.example.quizera.DbQuery.g_selected_cat_index;


public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionsView;
    private TextView tvQuesID, catNameTV, qa_catName, timerTV;
    private ImageButton prevQuesB, nextQuesB, drawerCloseB;
    private Button submitB, clearSelB;
    QuestionAdapter questionAdapter;
    private ImageView quesListB;
    private int quesID;
    private DrawerLayout drawer;
    private CountDownTimer timer;
    private  long timeLeft;
    private GridView quesListGV;
    private QuestionGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);

        init();

        questionAdapter = new QuestionAdapter(g_quesList);
        questionsView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this,g_quesList.size());
        quesListGV.setAdapter(gridAdapter);

        setSnapHelper();
        setClickListeners();
        startTimer();

    }

    private void init() {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        drawerCloseB = findViewById(R.id.drawerCloseB);
        clearSelB = findViewById(R.id.clear_selB);
        submitB = findViewById(R.id.submitB);
        prevQuesB = findViewById(R.id.prev_quesB);
        catNameTV = findViewById(R.id.qa_catName);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);
        qa_catName = findViewById(R.id.qa_catName);
        drawer = findViewById(R.id.drawer_layout);
        quesListGV = findViewById(R.id.ques_list_gv);
        quesID = 0;

        tvQuesID.setText("1/" + DbQuery.g_quesList.size());
        qa_catName.setText(g_catList.get(g_selected_cat_index).getName());
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());
        g_quesList.get(0).setStatus(UNANSWERED);

    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if(g_quesList.get(quesID).getStatus() == NOT_VISITED)
                    g_quesList.get(quesID).setStatus(UNANSWERED);

                tvQuesID.setText(String.valueOf((quesID + 1)) + "/" + String.valueOf(g_quesList.size()));


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void setClickListeners() {
        prevQuesB.setOnClickListener(v -> {

            if (quesID > 0) {
                questionsView.smoothScrollToPosition(quesID - 1);
            }

        });

        nextQuesB.setOnClickListener(v -> {

            if (quesID < g_quesList.size() - 1) {
                questionsView.smoothScrollToPosition(quesID + 1);
            }

        });
        clearSelB.setOnClickListener(v -> {
            g_quesList.get(quesID).setSelectedAns(-1);
            questionAdapter.notifyDataSetChanged();

        });
        quesListB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!drawer.isDrawerOpen(GravityCompat.END)) {
                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        drawerCloseB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

            }
        });
        submitB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTest();
            }
        });
    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        Button cancelB = view.findViewById(R.id.cancelB);
        Button confirmB = view.findViewById(R.id.confirmB);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = g_catList.get(g_selected_cat_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN",totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        });
        alertDialog.show();

    }
    public void goToQuestion(int position)
    {
        questionsView.smoothScrollToPosition(position);
        if(drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
    }


    private void startTimer() {
        long totalTime = g_catList.get(g_selected_cat_index).getTime() * 60 * 1000;
         timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                       timeLeft = remainingTime;
                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))

                );
                timerTV.setText(time);
            }

            @Override
            public void onFinish() {
              Intent intent =new Intent(QuestionsActivity.this,ScoreActivity.class);
              startActivity(intent);
              QuestionsActivity.this.finish();
            }
        };
        timer.start();
    }



}