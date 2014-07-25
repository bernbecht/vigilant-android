package com.br.utils;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Berhell on 25/07/14.
 */
public class DatePicker {

    public static String getActualDate(){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
        return df.format(c.getTime());

    }
}
