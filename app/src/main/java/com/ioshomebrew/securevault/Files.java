package com.ioshomebrew.securevault;

import android.util.Log;

/**
 * Created by ioshomebrew on 5/15/17.
 */

public class Files {
    private boolean isFoulder;
    private String name;
    private String path;

    public Files(boolean isFoulder, String name, String path)
    {
        this.isFoulder = isFoulder;
        this.name = name;
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    public boolean isFoulder()
    {
        return isFoulder;
    }

    public String getName()
    {
        return name;
    }

    public void setisFoulder(boolean isFoulder)
    {
        this.isFoulder = isFoulder;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
}
