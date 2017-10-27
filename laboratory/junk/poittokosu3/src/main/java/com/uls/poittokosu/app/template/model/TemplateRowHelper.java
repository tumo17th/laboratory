package com.uls.poittokosu.app.template.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.uls.poittokosu.app.template.consts.PoittoConst;
import com.uls.poittokosu.app.template.enums.PattoKosuColumns;

public class TemplateRowHelper {

    private static Map<String, Integer> dayOfWeekMapping = new HashMap<>();
    static {
        dayOfWeekMapping.put("日", Calendar.SUNDAY);
        dayOfWeekMapping.put("月", Calendar.MONDAY);
        dayOfWeekMapping.put("火", Calendar.TUESDAY);
        dayOfWeekMapping.put("水", Calendar.WEDNESDAY);
        dayOfWeekMapping.put("木", Calendar.THURSDAY);
        dayOfWeekMapping.put("金", Calendar.FRIDAY);
        dayOfWeekMapping.put("土", Calendar.SATURDAY);
    }

    /**
     * TemplateシートのRow一行を読み込んで、TemplateRowモデルを作成する
     * @param row TemplateシートのRow一行
     * @return TemplateRowモデル
     */
    public static TemplateRow createKosuTemplateRow(Row row) {
        TemplateRow templateRow = new TemplateRow();

        // CellでLoop
        for (int i = PoittoConst.CELL_START_NUM; i <= PoittoConst.CELL_END_NUM; i++) {
            Cell cell = row.getCell(i);

            // 空の場合はSkip
            if (cell == null || StringUtils.isEmpty(cell.toString())) {
                continue;
            }

            // TemplateRowを作成
            switch (PattoKosuColumns.get(i)) {
            case DAY_OF_WEEK:
                templateRow.setDayOfWeek(dayOfWeekMapping.get(cell.toString()));
                break;
            case START_TIME:
                templateRow.setStartTime(cell.getNumericCellValue());
                break;
            case END_TIME:
                templateRow.setEndTime(cell.getNumericCellValue());
                break;
            case BUSINESS_CODE:
                templateRow.setBusinessCode(cell.toString());
                break;
            case PROCESS:
                templateRow.setProcess(cell.toString());
                break;
            case PRODUCT:
                templateRow.setProduct(cell.toString());
                break;
            case ACTIVITY:
                templateRow.setActivity(cell.toString());
                break;
            case REMARKS:
                templateRow.setRemarks(cell.toString());
                break;
            default:
                break;
            }
        }

        return templateRow;
    }

    /**
     * 指定してRowに、ExecuteRowの値をコピーする
     * @param row 書き出し先のRow
     * @param executeRow 読み込み元のExecuteRow
     * @param cal 日付（TemplateRowの曜日の部分に日付を入力する）
     */
    public static void copyTemplateValue(Row row, ExecuteRow executeRow, Calendar cal) {
        for (int i = PoittoConst.CELL_START_NUM; i <= PoittoConst.CELL_END_NUM; i++) {
            Cell cell = row.getCell(i);
            switch (PattoKosuColumns.get(i)) {
            case DAY_OF_WEEK:
                cell.setCellValue(cal);
                break;
            case START_TIME:
                cell.setCellValue(executeRow.getStartTime());
                break;
            case END_TIME:
                cell.setCellValue(executeRow.getEndTime());
                break;
            case BUSINESS_CODE:
                cell.setCellValue(executeRow.getBusinessCode());
                break;
            case PROCESS:
                cell.setCellValue(executeRow.getProcess());
                break;
            case PRODUCT:
                cell.setCellValue(executeRow.getProduct());
                break;
            case ACTIVITY:
                cell.setCellValue(executeRow.getActivity());
                break;
            case REMARKS:
                cell.setCellValue(executeRow.getRemarks());
                break;
            default:
            }
        }
    }

}
