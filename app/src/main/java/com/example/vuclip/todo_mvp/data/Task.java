package com.example.vuclip.todo_mvp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Created by Banty on 31/03/18.
 * Immutable model class for a task
 */

@Entity(tableName = "tasks")
public final class Task {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "description")
    private final String mDescription;

    @ColumnInfo(name = "completed")
    private final boolean mCompleted;

    /**
     * Contructor to create a new active task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description) {
        this(title, description, UUID.randomUUID().toString(), false);
    }

    /**
     * Constructor to create an active Task if the Task already has an id (copy of another
     * Task).
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @NonNull String id) {
        this(title, description, id, false);
    }

    /**
     * Use this constructor to create a new completed Task.
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     */
    public Task(@Nullable String title, @Nullable String description, @Nullable String id, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
                Strings.isNullOrEmpty(mDescription);
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
