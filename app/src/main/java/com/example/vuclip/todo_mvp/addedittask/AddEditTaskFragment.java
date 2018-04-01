package com.example.vuclip.todo_mvp.addedittask;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Banty on 01/04/18.
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View{
    public static final String ARGUMENT_EDIT_TASK_ID = "ARGUMENT_EDIT_TASK_ID";

    public static AddEditTaskFragment newInstance() {

        Bundle args = new Bundle();

        AddEditTaskFragment fragment = new AddEditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showEmptyTaskError() {

    }

    @Override
    public void showTaskList() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {

    }
}
