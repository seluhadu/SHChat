package com.seluhadu.shchat.utils;

import android.os.Environment;

public class FilePath {
    public String root = Environment.getExternalStorageDirectory().getPath();
   public String PICTURES = root + "/Pictures";
   public String CAMERA = root + "/DCIM/camera";
    public String STORIES = root + "/Stories";

}
