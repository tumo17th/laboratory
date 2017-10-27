package com.uls.poittokosu.app.template.enums;

/** ぱっと工数のカラム定義 */
public enum PattoKosuColumns {

    /** 曜日 */
    DAY_OF_WEEK(1, "曜日"),
    /** 開始時刻 */
    START_TIME(2, "開始時刻"),
    /** 終了時刻 */
    END_TIME(3, "終了時刻"),
    /** 業務コード */
    BUSINESS_CODE(4, "業務コード"),
    /** 工程 */
    PROCESS(5, "工程"),
    /** 製品 */
    PRODUCT(6, "製品"),
    /** アクティビティ */
    ACTIVITY(7, "アクティビティ"),
    /** 備考 */
    REMARKS(8, "備考");

    private int columnNum;
    private String columnText;

    private PattoKosuColumns(int columnNum, String columnText) {
        this.columnNum = columnNum;
        this.columnText = columnText;
    }

    public static PattoKosuColumns get(int columnNum) {
        return PattoKosuColumns.values()[columnNum - 1];
    }

    public int num() {
        return columnNum;
    }
    public String text() {
        return columnText;
    }
}
