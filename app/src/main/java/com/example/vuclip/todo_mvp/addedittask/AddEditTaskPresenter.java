package com.example.vuclip.todo_mvp.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vuclip.todo_mvp.data.Task;
import com.example.vuclip.todo_mvp.data.source.TaskDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Banty on 01/04/18.
 *
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 */
public class AddEditTaskPresenter implements AddEditTaskContract.Presenter,
        TaskDataSource.GetTaskCallback {


    private final String mTaskId;
    private final TaskDataSource mTaskRepository;
    private final AddEditTaskContract.View mAddTaskView;
    private boolean mIsDataMissing;

    public AddEditTaskPresenter(@Nullable String taskId, @NonNull TaskDataSource taskRepository,
                                @NonNull AddEditTaskContract.View addTaskView, boolean shouldLoadDataFromRepo) {

        mTaskId = taskId;
        mTaskRepository = checkNotNull(taskRepository);
        mAddTaskView = checkNotNull(addTaskView);
        mIsDataMissing = shouldLoadDataFromRepo;

        mAddTaskView.setPresenter(this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    private void createTask(String title, String description) {
        Task task = new Task(title, description);
        if (task.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTaskRepository.saveTask(task);
            mAddTaskView.showTaskList();
        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mTaskRepository.saveTask(new Task(title, description, mTaskId));
        mAddTaskView.showTaskList();
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but the task is new.");
        }
        mTaskRepository.getTask(mTaskId, this);
    }


    @Override
    public void start() {
        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    public boolean isNewTask() {
        return mTaskId == null;
    }

    @Override
    public void onTaskLoaded(Task task) {
        if(mAddTaskView.isActive()) {
            mAddTaskView.setTitle(task.getTitle());
            mAddTaskView.setDescription(task.getDescription());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if(mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError();
        }
    }
}
