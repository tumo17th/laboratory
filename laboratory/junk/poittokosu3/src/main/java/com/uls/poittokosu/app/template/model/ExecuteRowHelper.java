package com.uls.poittokosu.app.template.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.uls.poittokosu.app.google.model.GCATemplateRow;
import com.uls.poittokosu.app.util.PoittoUtil;

public class ExecuteRowHelper {

    /**
     * templateRowをexecuteRowにcompileする
     * 
     * @param templateMap templateから作成した行
     * @param year 対象の年
     * @param month 対象の月
     * @return 書き出しの際に利用する行
     */
    public static Multimap<Integer, ExecuteRow> compile(Multimap<Integer, ? extends TemplateRow> templateMap, int year,
            int month) {

        // templateMapが空の場合はcompile不要なので即終了
        if (templateMap == null) {
            return null;
        }

        // カレンダーを作成
        Calendar cal = PoittoUtil.getMonthCalendar(year, month);

        Multimap<Integer, ExecuteRow> retMap = ArrayListMultimap.create();

        // 日付を1ヶ月分Loop
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            // 曜日を取得
            int youbi = cal.get(Calendar.DAY_OF_WEEK);

            // 対象の曜日のTemplateListを取得
            List<TemplateRow> youbiTemplateRowList = templateMap.get(youbi).stream().collect(Collectors.toList());
            // ExecuteRowを作成
            for (TemplateRow tempRow : youbiTemplateRowList) {
                if (!isTemplateSuitableForDate(tempRow, cal.get(Calendar.DATE))) {
                    continue;
                }
                ExecuteRow execRow = createExecuteRow(cal, tempRow);
                retMap.put(cal.get(Calendar.DATE), execRow);
            }

            cal.add(Calendar.DATE, 1);
        }

        return retMap;
    }

    /**
     * テンプレート行とGCA行をmergeする
     * 両者が重複した場合はGCA行を優先する
     * 
     * @param execTempMap templateから作成した行
     * @param execGCAMap googleCalendarAPIから作成した行
     * @param year 対象の年
     * @param month 対象の月
     * @return 書き出しの際に利用する行
     */
    public static Multimap<Integer, ExecuteRow> merge(Multimap<Integer, ExecuteRow> execTempMap,
            Multimap<Integer, ExecuteRow> execGCAMap, int year, int month) {

        // calendarMapが空の場合はmerge不要なので即終了
        if (execGCAMap == null) {
            return execTempMap;
        }

        // GoogleCalendarの予定を全て投入
        Multimap<Integer, ExecuteRow> retMap = ArrayListMultimap.create();
        retMap.putAll(execGCAMap);

        Calendar cal = PoittoUtil.getMonthCalendar(year, month);

        // merge開始 >> GoogleCalendarの予定がない場所に、Templateの予定を投入していく
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            Collection<ExecuteRow> execTempRows = execTempMap.get(i);
            Collection<ExecuteRow> execGCARows = execGCAMap.get(i);

            double blankStartTime = getMinStartTime(execTempRows);
            double blankEndTime = 0.0;

            // GoogleCalendarの予定でLoopしてmerge
            for (ExecuteRow execGCA : execGCARows) {
                blankEndTime = execGCA.getStartTime();
                mergeTemplateExecRows(execTempRows, blankStartTime, blankEndTime, retMap);
                blankStartTime = execGCA.getEndTime();
            }

            // GoogleCalendarの最後の予定と、一日の終わりの間でmerge
            blankEndTime = getMaxEndTime(execTempRows);
            if (blankStartTime < blankEndTime) {
                mergeTemplateExecRows(execTempRows, blankStartTime, blankEndTime, retMap);
            }

            cal.add(Calendar.DATE, 1);
        }

        return retMap;
    }

    // Privates ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** 一日の開始時間を取得（テンプレートの最初の予定の開始時間。defaultは9時） */
    private static double getMinStartTime(Collection<ExecuteRow> execTempRows) {
        double ret = 9 / 24;
        for (ExecuteRow er : execTempRows) {
            if (er.getStartTime() < ret) {
                ret = er.getStartTime();
            }
        }
        return ret;
    }

    /** 一日の終了時間を取得（テンプレートの最後の予定の終了時間。defaultは18時） */
    private static double getMaxEndTime(Collection<ExecuteRow> execTempRows) {
        double ret = 18 / 24;
        for (ExecuteRow er : execTempRows) {
            if (ret < er.getEndTime()) {
                ret = er.getEndTime();
            }
        }
        return ret;
    }

    /** 指定されたstartTimeとendTimeの間の時間に含まれるExecuteRowを判定し、投入する */
    private static void mergeTemplateExecRows(Collection<ExecuteRow> execTempRows, double blankStartTime,
            double blankEndTime, Multimap<Integer, ExecuteRow> retMap) {
        for (ExecuteRow execRow : execTempRows) {
            mergeRow(execRow, blankStartTime, blankEndTime, retMap);
        }
    }

    /**
     * 指定されたstartTimeとendTimeの間の時間に含まれるExecuteRowを判定し、投入する
     * 1.全く含まれない場合は何もしない
     * 2.完全に含まれる場合はそのままput
     * 3.一部が含まれる場合は時間を変更してput
     * 
     * @param execRow 最終的な書き出し行
     * @param blankStartTime 指定の開始時間
     * @param blankEndTime 指定の終了時間
     * @param retMap 最終的な書き出し行マップ
     */
    private static void mergeRow(ExecuteRow execRow, double blankStartTime, double blankEndTime,
            Multimap<Integer, ExecuteRow> retMap) {

        // blankStartTimeとblankEndTimeが同じ場合は何もせず終了
        if (blankStartTime == blankEndTime) {
            return;
        }

        // execRowがtargetの時間に含まれない場合は何もせずに終了
        if (execRow.getEndTime() <= blankStartTime || blankEndTime <= execRow.getStartTime()) {
            return;
        }

        // execRowがtargetの時間に完全に含まれる場合はそのままputする
        if (blankStartTime <= execRow.getStartTime() && execRow.getEndTime() <= blankEndTime) {
            retMap.put(execRow.getTargetDate(), execRow);
        }

        // execRowがtargetの時間に一部含まれる場合は、startTimeやEndTimeを修正してput
        // start時間がはみ出す場合
        if (execRow.getStartTime() < blankStartTime && execRow.getEndTime() <= blankEndTime) {
            ExecuteRow copyRow = execRow.copy();
            copyRow.setStartTime(blankStartTime);
            retMap.put(copyRow.getTargetDate(), copyRow);
        }
        // end時間がはみ出す場合
        if (blankStartTime <= execRow.getStartTime() && blankEndTime < execRow.getEndTime()) {
            ExecuteRow copyRow = execRow.copy();
            copyRow.setEndTime(blankEndTime);
            retMap.put(copyRow.getTargetDate(), copyRow);
        }
        // start時間もend時間もはみ出す場合
        if (execRow.getStartTime() < blankStartTime && blankEndTime < execRow.getEndTime()) {
            ExecuteRow copyRow = execRow.copy();
            copyRow.setStartTime(blankStartTime);
            copyRow.setEndTime(blankEndTime);
            retMap.put(copyRow.getTargetDate(), copyRow);
        }
    }

    /** 引数の日付に対してTemplateを作成すべきかどうかを判定 */
    private static boolean isTemplateSuitableForDate(TemplateRow tempRow, int date) {
        // 通常のTemplateRowの場合は曜日判定なので常にtrue
        if (!(tempRow instanceof GCATemplateRow)) {
            return true;
        }
        // GCATemplateRowの場合はtargetDateまでチェック
        GCATemplateRow gcaTempRow = (GCATemplateRow)tempRow;
        if (date == gcaTempRow.getTargetDate()) {
            return true;
        }
        return false;
    }

    /** TemplateRowの値を、ExecuteRowにコピーする */
    private static ExecuteRow createExecuteRow(Calendar cal, TemplateRow tempRow) {
        ExecuteRow execRow = new ExecuteRow();
        execRow.setTargetDate(cal.get(Calendar.DATE));
        execRow.setStartTime(tempRow.getStartTime());
        execRow.setEndTime(tempRow.getEndTime());
        execRow.setBusinessCode(tempRow.getBusinessCode());
        execRow.setProcess(tempRow.getProcess());
        execRow.setProduct(tempRow.getProduct());
        execRow.setActivity(tempRow.getActivity());
        execRow.setRemarks(tempRow.getRemarks());
        return execRow;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
