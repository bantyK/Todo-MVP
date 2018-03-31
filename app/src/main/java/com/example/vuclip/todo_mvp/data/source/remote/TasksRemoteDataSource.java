package com.example.vuclip.todo_mvp.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.data.Task;
import com.example.vuclip.todo_mvp.data.source.TaskDataSource;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Banty on 31/03/18.
 * <p>
 * Mock implementation of Remote Data source. Adds a latency of 5 sec simulating a network call.
 */
public class TasksRemoteDataSource implements TaskDataSource {

    private static TasksRemoteDataSource INSTANCE;

    public static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Task> TASK_SERVICE_DATA;

    static {
        TASK_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASK_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TasksRemoteDataSource();

        return INSTANCE;
    }

    //Prevent direct instantiation
    private TasksRemoteDataSource() {
    }


    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        //Simulate network by delaying the execution
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTasksLoaded(Lists.newArrayList(TASK_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        final Task task = TASK_SERVICE_DATA.get(taskId);

        //Simulate network call
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(task);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASK_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASK_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        //Not required for Remote data source
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        TASK_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        //Not required for Remote data source
    }

    @Override
    public void clearCompletedTask() {
        Iterator<Map.Entry<String, Task>> iterator = TASK_SERVICE_DATA.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> entry = iterator.next();
            if (entry.getValue().isCompleted()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        //Not required for Remote data storage
    }

    @Override
    public void deleteAllTasks() {
        TASK_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASK_SERVICE_DATA.remove(taskId);
    }
}
