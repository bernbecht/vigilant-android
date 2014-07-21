package com.br.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.br.vigilant.R;
import com.parse.ParseObject;

import java.io.File;
import java.io.Serializable;

public class UserParseStore implements Serializable {

    private ParseObject user =null;


    public static ParseObject getParseUser(){
        final File suspend_f=new File(SerializationTest.cacheDir, "test");

        FileOutputStream   fos  = null;
        ObjectOutputStream oos  = null;
        boolean            keep = true;

        try {
            fos = new FileOutputStream(suspend_f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            keep = false;
        } finally {
            try {
                if (oos != null)   oos.close();
                if (fos != null)   fos.close();
                if (keep == false) suspend_f.delete();
            } catch (Exception e) { /* do nothing */ }
        }

        return keep;
    }




}

