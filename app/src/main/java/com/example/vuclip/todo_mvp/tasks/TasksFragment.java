package com.example.vuclip.todo_mvp.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.vuclip.todo_mvp.data.Task;

import java.util.List;

/**
 * Created by Banty on 31/03/18.
 */
public class TasksFragment extends Fragment implements TasksContract.View{
    public static TasksFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTasks(List<Task> tasks) {

    }

    @Override
    public void showAddTask() {

    }

    @Override
    public void showTaskDetailsUI(String taskId) {

    }

    @Override
    public void showTaskMarkedComplete() {

    }

    @Override
    public void showTaskMarkedActive() {

    }

    @Override
    public void showCompletedTaskCleared() {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showNoTasks() {

    }

    @Override
    public void showActiveFilterLabel() {

    }

    @Override
    public void showCompletedFilterLabel() {

    }

    @Override
    public void showAllFilterLabel() {

    }

    @Override
    public void showNoActiveTasks() {

    }

    @Override
    public void showNoCompletedTasks() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showFilteringPopUpMenu() {

    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {

    }
}
