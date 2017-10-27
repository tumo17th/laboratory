package com.uls.poittokosu.app.google.model;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GCATemplateRowHelper {

    /**
     * GoogleCalendarのeventを、GCATemplateRowにconvertします
     * @param eventList GoogleCalendarのeventリスト
     * @return GCATemplateRowのリスト
     */
    public static Multimap<Integer, GCATemplateRow> convert(List<Event> eventList, List<String> excludeList) {
        Multimap<Integer, GCATemplateRow> retMap = ArrayListMultimap.create();

        for (Event event : eventList) {
            DateTime start = event.getStart().getDateTime();

            // 時間指定のないイベントは登録しない
            DateTime end = event.getEnd().getDateTime();
            if (start == null || end == null) {
                continue;
            }

            // excludePatternにmatchしたイベントは登録しない
            String eventName = event.getSummary();
            if (doesMatchExcludeList(eventName, excludeList)) {
                continue;
            }

            GCATemplateRow templateRow = new GCATemplateRow();
            Calendar targetCal = getTargetCalendar(start);
            templateRow.setDayOfWeek(targetCal.get(Calendar.DAY_OF_WEEK));
            templateRow.setTargetDate(targetCal.get(Calendar.DATE));
            templateRow.setStartTime(getExcelTime(start));
            templateRow.setEndTime(getExcelTime(end));
            templateRow.setRemarks(event.getSummary());

            retMap.put(templateRow.getDayOfWeek(), templateRow);
        }

        return retMap;
    }

    private static boolean doesMatchExcludeList(String eventName, List<String> excludeList) {
        if (eventName == null || StringUtils.isEmpty(eventName)) {
            return false;
        }
        for (String str : excludeList) {
            if (eventName.contains(str)) {
                return true;
            }
        }
        return false;
    }

    private static Calendar getTargetCalendar(DateTime param) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(param.getValue());
        return c;
    }

    /**
     * ExcelのCellにsetする時間を取得する
     * 【Excelの時間Cellの仕様】
     * 
     * (公式)
     * 分 / 一日（分） = ExcelTime
     * 
     * (例)
     * 3:00の場合 >> (3*60) / (24*60) = 3/24 = 0.125
     * 3:15の場合 >> (3*60+15) / (24*60) = 0.135416667
     * 
     * @param param
     * @return
     */
    private static double getExcelTime(DateTime param) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(param.getValue());
        double hourOfDay = (double)cal.get(Calendar.HOUR_OF_DAY);
        double minutes = (double)cal.get(Calendar.MINUTE);
        return (double)(hourOfDay * 60 + minutes) / (double)(24 * 60);
    }

}
