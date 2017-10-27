package com.uls.poittokosu.app.template.model;

/** テンプレートから読み込んだ行 */
public class TemplateRow {

    /** 曜日 */
    private int dayOfWeek;
    /** 開始時刻 */
    private double startTime;
    /** 終了時刻 */
    private double endTime;
    /** 業務コード */
    private String businessCode;
    /** 工程 */
    private String process;
    /** 製品 */
    private String product;
    /** アクティビティ */
    private String activity;
    /** 備考 */
    private String remarks;

    /** 曜日 */
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    /** 開始時刻 */
    public double getStartTime() {
        return startTime;
    }
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    /** 終了時刻 */
    public double getEndTime() {
        return endTime;
    }
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    /** 業務コード */
    public String getBusinessCode() {
        return businessCode;
    }
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    /** 工程 */
    public String getProcess() {
        return process;
    }
    public void setProcess(String process) {
        this.process = process;
    }
    /** 製品 */
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    /** アクティビティ */
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    /** 備考 */
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TemplateRow [dayOfWeek=" + dayOfWeek + ", startTime=" + startTime + ", endTime=" + endTime
                + ", businessCode=" + businessCode + ", process=" + process + ", product=" + product + ", activity="
                + activity + ", remarks=" + remarks + "]";
    }

}
