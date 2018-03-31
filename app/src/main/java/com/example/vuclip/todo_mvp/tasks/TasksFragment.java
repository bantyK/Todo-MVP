package com.example.vuclip.todo_mvp.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Banty on 31/03/18.
 */
public class TasksFragment extends Fragment {
    public static TasksFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
