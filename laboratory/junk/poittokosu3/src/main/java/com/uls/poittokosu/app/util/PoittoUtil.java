package com.uls.poittokosu.app.util;

import java.util.Calendar;

public class PoittoUtil {

    /** 指定した年度、月のカレンダーを取得 */
    public static Calendar getMonthCalendar(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal;
    }

}
