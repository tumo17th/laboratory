package com.uls.poittokosu.app.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.calendar.model.Event;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.uls.poittokosu.app.google.GoogleCalendarAPIService;
import com.uls.poittokosu.app.google.model.GCATemplateRow;
import com.uls.poittokosu.app.google.model.GCATemplateRowHelper;
import com.uls.poittokosu.app.template.consts.PoittoConst;
import com.uls.poittokosu.app.template.model.ExecuteRow;
import com.uls.poittokosu.app.template.model.ExecuteRowHelper;
import com.uls.poittokosu.app.template.model.TemplateRow;
import com.uls.poittokosu.app.template.model.TemplateRowHelper;
import com.uls.poittokosu.config.PoittoConfig;

@Service
public class KosuTemplateServiceImpl implements KosuTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(KosuTemplateServiceImpl.class);

    @Autowired
    private PoittoConfig config;

    @Autowired
    private GoogleCalendarAPIService calendarService;

    @Override
    public void doService(int year, int month) throws Exception {
        printConfig(config);

        // ファイル名を作成
        String inputFileName = config.createInputFileName();
        String outputFileName = config.createOutputFileName(year, month);

        // 作成対象月のカレンダーを作成
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);

        try (FileInputStream in = new FileInputStream(inputFileName);
                FileOutputStream out = new FileOutputStream(outputFileName);
                Workbook wb = WorkbookFactory.create(in)) {

            // 1.TemplateSheetの読み込み
            Sheet templateSheet = wb.getSheet(PoittoConst.TEMPLATE_SHEET_NAME);
            Multimap<Integer, TemplateRow> templateMap = readTemplate(templateSheet);

            // 2.Google Calendar API の呼び出し
            Multimap<Integer, GCATemplateRow> calendarTemplateMap = readCalendar(year, month);

            // 3.TemplateRowをExecuteRowにcompile
            Multimap<Integer, ExecuteRow> execTempMap = ExecuteRowHelper.compile(templateMap, year, month);
            Multimap<Integer, ExecuteRow> execGCATempMap = ExecuteRowHelper.compile(calendarTemplateMap, year, month);
            Multimap<Integer, ExecuteRow> executeMap = ExecuteRowHelper.merge(execTempMap, execGCATempMap, year, month);

            // 4.Template内容の書き出し
            Sheet outputSheet = wb.getSheet(PoittoConst.KOSU_SHEET_NAME);
            writeTemplate(outputSheet, executeMap, cal);

            // 5.TemplateSheetの削除
            wb.removeSheetAt(wb.getSheetIndex(PoittoConst.TEMPLATE_SHEET_NAME));
            wb.setActiveSheet(wb.getSheetIndex(PoittoConst.KOSU_SHEET_NAME));

            // Workbook書き出し
            wb.write(out);

        } catch (FileNotFoundException fnfe) {
            logger.error("Input file [{}] is not found", inputFileName, fnfe);
            throw fnfe;
        } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
            logger.error("Error has occurred.", e);
            throw e;
        }
    }

    // Privates ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Multimap<Integer, TemplateRow> readTemplate(Sheet template) {
        Multimap<Integer, TemplateRow> templateMap = ArrayListMultimap.create();

        // RowでLoop
        for (int i = PoittoConst.ROW_START_NUM; i < template.getLastRowNum(); i++) {
            Row row = template.getRow(i);

            // 空の場合はSkip
            Cell firstCell = row.getCell(PoittoConst.CELL_START_NUM);
            if (firstCell == null || StringUtils.isEmpty(firstCell.toString())) {
                continue;
            }

            // TemplateRowの作成
            TemplateRow templateRow = TemplateRowHelper.createKosuTemplateRow(row);
            templateMap.put(templateRow.getDayOfWeek(), templateRow);
        }

        // 読み込んだTemplate内容をSysOutに出力
        printTemplateContents(templateMap);

        return templateMap;
    }

    private Multimap<Integer, GCATemplateRow> readCalendar(int year, int month) throws IOException {
        if (!config.isUseGoogleCalendarAPI()) {
            return null;
        }
        List<Event> eventList = calendarService.fetchEventList(year, month);
        return GCATemplateRowHelper.convert(eventList, config.getExcludePatterns());
    }

    private static void writeTemplate(Sheet sheet, Multimap<Integer, ExecuteRow> executeMap, Calendar cal) {
        int rowNum = PoittoConst.ROW_START_NUM;

        // Calendarを1ヶ月分Loop
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            int youbi = cal.get(Calendar.DAY_OF_WEEK);

            // 土日はSkip
            if (youbi == Calendar.SUNDAY || youbi == Calendar.SATURDAY) {
                cal.add(Calendar.DATE, 1);
                continue;
            }

            // 週の区切りで空行を作成（ただし1行目は空行にしない）
            if (rowNum != PoittoConst.ROW_START_NUM && youbi == Calendar.MONDAY)
                rowNum++;

            // 対象の曜日のTemplateListを取得
            List<ExecuteRow> executeRowList = executeMap.get(cal.get(Calendar.DATE)).stream()
                    .sorted(new Comparator<ExecuteRow>() {
                        @Override
                        public int compare(ExecuteRow o1, ExecuteRow o2) {
                            return o1.getStartTime() - o2.getStartTime() <= 0 ? -1 : 1;
                        }
                    }).collect(Collectors.toList());

            // Templateが空の場合はSkip
            if (executeRowList == null || executeRowList.isEmpty()) {
                cal.add(Calendar.DATE, 1);
                continue;
            }

            // 各曜日のTemplateをLoop
            for (ExecuteRow executeRow : executeRowList) {
                Row row = sheet.getRow(rowNum);
                TemplateRowHelper.copyTemplateValue(row, executeRow, cal);
                rowNum++;
            }

            cal.add(Calendar.DATE, 1);
        }
    }

    private static void printConfig(PoittoConfig config) {
        logger.info(">>>>> [Poitto Config] >>>>>");
        logger.info("Config = " + config);
        logger.info("<<<<< [Poitto Config] <<<<<");
    }

    private static void printTemplateContents(Multimap<Integer, TemplateRow> templateMap) {
        logger.info(">>>>> [Template Content] >>>>>");
        for (Entry<Integer, TemplateRow> entry : templateMap.entries()) {
            logger.info("MultiMap:: {} -> {}", entry.getKey(), entry.getValue());
        }
        logger.info("<<<<< [Template Content] <<<<<");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
