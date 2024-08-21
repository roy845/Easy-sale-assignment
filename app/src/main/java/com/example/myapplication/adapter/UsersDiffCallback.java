package com.example.myapplication.adapter;

import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.models.User;

import java.util.List;

public class UsersDiffCallback extends DiffUtil.Callback {

    private final List<User> oldList;
    private final List<User> newList;

    public UsersDiffCallback(List<User> oldList, List<User> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == ((newList.get(newItemPosition).getId()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare the contents of the items
        User oldUser = oldList.get(oldItemPosition);
        User newUser = newList.get(newItemPosition);
        return oldUser.equals(newUser);
    }
}

