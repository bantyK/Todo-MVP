package com.example.vuclip.todo_mvp.data.source;

import android.support.annotation.NonNull;

import com.example.vuclip.todo_mvp.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Banty on 31/03/18.
 * Concrete implementation of load tasks from the data sources into a cache
 */
public class TaskRepository implements TaskDataSource {

    private static TaskRepository INSTANCE = null;

    private final TaskDataSource mTaskRemoteDataSource;

    private final TaskDataSource mTaskLocalDataSource;

    Map<String, Task> mCachedTasks;

    boolean mCacheIsDirty = false;

    private TaskRepository(@NonNull TaskDataSource mTaskRemoteDataSource,
                           @NonNull TaskDataSource mTaskLocalDataSource) {
        this.mTaskRemoteDataSource = mTaskRemoteDataSource;
        this.mTaskLocalDataSource = mTaskLocalDataSource;
    }

    /**
     * Singleton object for the TaskRepository class.
     */
    public static TaskRepository getInstance(TaskDataSource tasksRemoteDataSource,
                                             TaskDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TaskRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets tasks from cache, local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        //Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }
        if (mCacheIsDirty) {
            //If the cache us dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network
            mTaskLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                //Data not available , query network
                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }

    }

    private void getTasksFromRemoteDataSource(final LoadTasksCallback callback) {
        mTaskRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTaskLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTaskLocalDataSource.saveTask(task);
        }
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mTaskLocalDataSource.saveTask(task);
        }
    }

    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        //check cache first
        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        //task not available in cache, check local storage
        mTaskLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }

            //Task not available in local storage also, query from network
            @Override
            public void onDataNotAvailable() {
                mTaskRemoteDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        if (mCachedTasks == null) {
                            mCachedTasks = new LinkedHashMap<>();
                        }
                        mCachedTasks.put(task.getId(), task);
                        callback.onTaskLoaded(task);
                    }

                    //task not available in network also, give up.
                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTaskRemoteDataSource.saveTask(task);
        mTaskLocalDataSource.saveTask(task);

        //Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTaskLocalDataSource.completeTask(task);
        mTaskRemoteDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        //Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);

    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    private Task getTaskWithId(String taskId) {
        checkNotNull(taskId);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(taskId);
        }
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTaskRemoteDataSource.activateTask(task);
        mTaskLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        //Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTask() {
        mTaskRemoteDataSource.clearCompletedTask();
        mTaskLocalDataSource.clearCompletedTask();

        //Do in memory cache update to keep the app UI up to date.
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }

        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTaskRemoteDataSource.deleteAllTasks();
        mTaskLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTaskLocalDataSource.deleteTask(checkNotNull(taskId));
        mTaskRemoteDataSource.deleteTask(checkNotNull(taskId));

        mCachedTasks.remove(taskId);
    }
}
