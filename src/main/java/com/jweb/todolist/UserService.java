package com.jweb.todolist;

import java.util.HashMap;

public class UserService
{
    HashMap<String, User> users = new HashMap<>();

    public User addUser(String id, String password, String name)
    {
        User user = new User(id, password, name);
        // TODO: Check for duplicate login ID
        users.put(id, user);
        return user;
    }

    public User updateUser(User user)
    {
        if (users.containsKey(user.id()))
        {
            users.put(user.id(), user);
            return user;
        }
        else
            return null;
    }

    public User removeUser(String id)
    {
        return users.remove(id);
    }

    public User getUser(String id)
    {
        return users.get(id);
    }

}
