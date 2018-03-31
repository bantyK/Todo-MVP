package com.example.vuclip.todo_mvp.tasks;

import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.data.Task;
import com.example.vuclip.todo_mvp.data.source.TaskDataSource;
import com.example.vuclip.todo_mvp.data.source.TaskRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Banty on 31/03/18.
 */
class TaskPresenter implements TasksContract.Presenter {
    private TasksFilterType mCurrentFiltering;

    private final TaskRepository mTaskRepository;

    private final TasksContract.View mTaskView;

    public TaskPresenter(TaskRepository taskRepository, TasksFragment tasksFragment) {
        this.mTaskRepository = taskRepository;
        mTaskView = tasksFragment;
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadTaks(boolean forceUpdate) {

    }

    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestTask) {

    }

    @Override
    public void completeTask(@NonNull Task completedTask) {

    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

    }

    @Override
    public void clearCompletedTask() {

    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        this.mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }


    @Override
    public void start() {

    }
}
