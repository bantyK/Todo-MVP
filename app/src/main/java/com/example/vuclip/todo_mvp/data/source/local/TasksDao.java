package com.example.vuclip.todo_mvp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.vuclip.todo_mvp.data.Task;

import java.util.List;

/**
 * Created by Banty on 31/03/18.
 */
@Dao
public interface TasksDao {

    /**
     * @return all tasks.
     */
    @Query("SELECT * FROM Tasks")
    List<Task> getTasks();

    /**
     * Select a task by its id.
     */
    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    Task getTaskById(String taskId);

    /**
     * Insert a task in the database. If the task already exists, replace it
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    /**
     * Update the complete status of a task
     */
    @Query("UPDATE Tasks SEt completed = :completed WHERE entryid = :taskId")
    void updateCompleted(String taskId, boolean completed);

    /**
     * Delete a task by id.
     */
    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
    void deleteTaskById(String taskId);

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Tasks")
    void deleteTasks();

    /**
     * Delete all completed tasks
     */

    @Query("DELETE FROM Tasks WHERE completed = 1")
    int deleteCompletedTasks();

    /**
     *  Update a task
     *  @return the number of task updated, should always be 1.
     */
    @Update
    int updateTask(Task task);
}
