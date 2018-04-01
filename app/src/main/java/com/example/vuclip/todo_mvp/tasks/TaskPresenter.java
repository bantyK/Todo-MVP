package com.example.vuclip.todo_mvp.tasks;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.addedittask.AddEditTaskActivity;
import com.example.vuclip.todo_mvp.data.Task;
import com.example.vuclip.todo_mvp.data.source.TaskDataSource;
import com.example.vuclip.todo_mvp.data.source.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Banty on 31/03/18.
 */
class TaskPresenter implements TasksContract.Presenter {
    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private final TaskRepository mTaskRepository;

    private final TasksContract.View mTaskView;
    private boolean mFirstLoad = true;

    public TaskPresenter(TaskRepository taskRepository, TasksContract.View tasksView) {
        mTaskRepository = checkNotNull(taskRepository, "TaskRepository cannot be null");
        mTaskView = checkNotNull(tasksView, "taskView cannot be null");

        mTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTaskView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTaskView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTaskRepository.refreshTasks();
        }

        mTaskRepository.getTasks(new TaskDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                    }
                }

                if (!mTaskView.isActive())
                    return;
                if (showLoadingUI) {
                    mTaskView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTaskView.isActive())
                    return;
                mTaskView.showLoadingTasksError();
            }
        });
    }

    private void processTasks(List<Task> tasksToShow) {
        if (tasksToShow.isEmpty()) {
            processEmptyTask();
        } else {
            mTaskView.showTasks(tasksToShow);
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTaskView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTaskView.showCompletedFilterLabel();
                break;
            default:
                mTaskView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTask() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTaskView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTaskView.showNoCompletedTasks();
                break;
            default:
                mTaskView.showNoTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        mTaskView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestTask) {
        checkNotNull(requestTask);
        mTaskView.showTaskDetailsUI(requestTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask);
        mTaskRepository.completeTask(completedTask);
        mTaskView.showTaskMarkedComplete();
        loadTasks(false, false);
    }


    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask);
        mTaskRepository.activateTask(activeTask);
        mTaskView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTask() {
        mTaskRepository.clearCompletedTask();
        mTaskView.showCompletedTaskCleared();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        this.mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

}
