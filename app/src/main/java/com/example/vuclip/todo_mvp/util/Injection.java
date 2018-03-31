package com.example.vuclip.todo_mvp.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.data.source.TaskRepository;
import com.example.vuclip.todo_mvp.data.source.local.TaskLocalDataSource;
import com.example.vuclip.todo_mvp.data.source.local.ToDoDatabase;
import com.example.vuclip.todo_mvp.data.source.remote.TasksRemoteDataSource;

/**
 * Created by Banty on 31/03/18.
 */
public class Injection {
    // TODO: 31/03/18 provide this dependency using dagger
    public static TaskRepository provideTaskRepository(@NonNull Context context) {
        ToDoDatabase database = ToDoDatabase.getInstance(context);

        return TaskRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TaskLocalDataSource.getInstance(new AppExecutors(), database.taskDao()));
    }
}
