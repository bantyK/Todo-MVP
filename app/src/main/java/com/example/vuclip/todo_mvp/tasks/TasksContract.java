package com.example.vuclip.todo_mvp.tasks;

import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.BasePresenter;
import com.example.vuclip.todo_mvp.BaseView;
import com.example.vuclip.todo_mvp.data.Task;

import java.util.List;

/**
 * Created by Banty on 31/03/18.
 */
public interface TasksContract {
    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTaks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestTask);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);

        void clearCompletedTask();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUI(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTaskCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();

    }
}
