package com.example.RecordTime;

import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;

import java.util.ArrayList;
import java.util.List;

public class EveryDateInfo {
    public static List<Dates> createDateRow() {
        List<Dates> dataSet = new ArrayList<>();
        Calendar cl = new GregorianCalendar();
        int maxDate = cl.getActualMaximum(Calendar.DATE);
        int idx = 1;
        while (idx <= maxDate) {
            Dates data = new Dates();
            data.dateNum = idx;

            dataSet.add(data);
            idx = idx + 1;
        }
        return dataSet;
    }
}

class Dates {
    int dateNum;
}