package com.uls.poittokosu.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PoittoConfig {

    @Value("${poitto.yourName}")
    private String yourName;

    @Value("${poitto.inputFileName}")
    private String inputFileName;

    @Value("${poitto.inputFolderName}")
    private String inputFolderName;

    @Value("${poitto.outputFileName}")
    private String outputFileName;

    @Value("${poitto.outputFolderName}")
    private String outputFolderName;

    @Value("${poitto.useGoogleCalendarAPI}")
    private boolean useGoogleCalendarAPI;

    @Value("${poitto.googleCalendar.excludePattern}")
    private String excludePattern;

    public String getYourName() {
        return yourName;
    }
    public void setYourName(String yourName) {
        this.yourName = yourName;
    }
    public boolean isUseGoogleCalendarAPI() {
        return useGoogleCalendarAPI;
    }
    public void setUseGoogleCalendarAPI(boolean useGoogleCalendarAPI) {
        this.useGoogleCalendarAPI = useGoogleCalendarAPI;
    }
    public String getExcludePattern() {
        return excludePattern;
    }
    public void setExcludePattern(String excludePattern) {
        this.excludePattern = excludePattern;
    }

    // Publics /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** 入力元ファイル名（Full Path）を取得 */
    public String createInputFileName() {
        return inputFolderName + File.separator + inputFileName;
    }

    /** 出力先ファイル名（Full Path）を取得 */
    public String createOutputFileName(int year, int month) {
        return outputFolderName + File.separator + outputFileName.replace("{name}", yourName)
                .replace("{year}", String.valueOf(year)).replace("{month}", String.format("%02d", month));
    }

    /** 除外する予定の名前一覧（パターンマッチ）を取得 */
    public List<String> getExcludePatterns() {
        List<String> ret = new ArrayList<>();
        if (excludePattern == null) {
            return ret;
        }
        for (String str : excludePattern.split(",")) {
            ret.add(str);
        }
        return ret;
    }

    @Override
    public String toString() {
        return "PoittoConfig [yourName=" + yourName + ", inputFileName=" + inputFileName + ", inputFolderName="
                + inputFolderName + ", outputFileName=" + outputFileName + ", outputFolderName=" + outputFolderName
                + ", useGoogleCalendarAPI=" + useGoogleCalendarAPI + ", excludePattern=" + excludePattern + "]";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
