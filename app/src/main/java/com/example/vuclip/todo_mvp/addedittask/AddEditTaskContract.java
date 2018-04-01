package com.example.vuclip.todo_mvp.addedittask;

import com.example.vuclip.todo_mvp.BasePresenter;
import com.example.vuclip.todo_mvp.BaseView;
import com.example.vuclip.todo_mvp.tasks.TasksContract;

/**
 * Created by Banty on 01/04/18.
 */
public interface AddEditTaskContract {
    interface View extends BaseView<Presenter>{

        void showEmptyTaskError();

        void showTaskList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}
