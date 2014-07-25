package com.br.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.br.vigilant.AddReportActivity;
import com.parse.ParseObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Berhell on 25/07/14.
 */
public class CacheObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public String startTime;
    public String endTime;
    public String day;
    public List<ParseObject> categories;

    String file_path = Environment.getExternalStorageDirectory().toString();
    String file_name = "categories";

    public boolean classEnabled;


    public CacheObject(String title, String startTime, boolean enable, List<ParseObject> c) {
        this.title = title;
        this.startTime = startTime;
        this.classEnabled = enable;
        this.categories = c;
    }

    public boolean saveObject(CacheObject obj) {
        final File suspend_f = new File(AddReportActivity.cacheDir, file_name);

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        boolean keep = true;

        try {
            fos = new FileOutputStream(suspend_f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (Exception e) {
            keep = false;
            Log.d("tag", "saving error: " + e);
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
                if (keep == false) suspend_f.delete();
            } catch (Exception e) {
                Log.d("tag", "saving error: " + e);
            }
        }

        return keep;
    }

    public CacheObject getObject(Context c) {
        final File suspend_f = new File(AddReportActivity.cacheDir, file_name);

        CacheObject simpleClass = null;
        FileInputStream fis = null;
        ObjectInputStream is = null;

        try {
            fis = new FileInputStream(suspend_f);
            is = new ObjectInputStream(fis);
            simpleClass = (CacheObject) is.readObject();
            is.close();
        } catch (Exception e) {
            String val = e.getMessage();
            Log.d("tag", "loading error: " +val);
        } finally {
            try {
                if (fis != null) fis.close();
                if (is != null) is.close();
            } catch (Exception e) {
                Log.d("tag", "loading error: " +e);
            }
        }

        return simpleClass;
    }
}