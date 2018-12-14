package com.dot2dotz.app.Utils;

import com.dot2dotz.app.Fragments.HomeFragment;

public class MySingleton
{
    static HomeFragment homeFragment;
    private static MySingleton _instance;

    private MySingleton()
    {

    }

    public static MySingleton getInstance()
    {
        if (_instance == null)
        {
            _instance = new MySingleton();
        }
        return _instance;
    }

    public static HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public static void setHomeFragment(HomeFragment homeFragment) {
        MySingleton.homeFragment = homeFragment;
    }

    public static MySingleton get_instance() {
        return _instance;
    }

    public static void set_instance(MySingleton _instance) {
        MySingleton._instance = _instance;
    }
}