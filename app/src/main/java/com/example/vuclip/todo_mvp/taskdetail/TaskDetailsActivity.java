package com.example.vuclip.todo_mvp.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vuclip.todo_mvp.R;

/**
 * Created by Banty on 01/04/18.
 */
public class TaskDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_activity);
    }
}
