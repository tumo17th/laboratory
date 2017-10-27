package com.uls.poittokosu.app.template.model;

/** 書き出しの際に利用する行 */
public class ExecuteRow {

    /** 日付(GoogleCalendarAPI用 >> 毎週ではなく特定の日付のみ値を設定する時に利用する) */
    private Integer targetDate;
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

    public ExecuteRow() {
    }
    public ExecuteRow(Integer targetDate, double startTime, double endTime, String businessCode, String process,
            String product, String activity, String remarks) {
        this.targetDate = targetDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.businessCode = businessCode;
        this.process = process;
        this.product = product;
        this.activity = activity;
        this.remarks = remarks;
    }

    /** 日付(GoogleCalendarAPI用 >> 毎週ではなく特定の日付のみ値を設定する時に利用する) */
    public Integer getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(Integer targetDate) {
        this.targetDate = targetDate;
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
        return "ExecuteRow [targetDate=" + targetDate + ", startTime=" + startTime + ", endTime=" + endTime
                + ", businessCode=" + businessCode + ", process=" + process + ", product=" + product + ", activity="
                + activity + ", remarks=" + remarks + "]";
    }

    public ExecuteRow copy() {
        return new ExecuteRow(this.getTargetDate(), this.getStartTime(), this.getEndTime(), this.getBusinessCode(),
                this.getProcess(), this.getProduct(), this.getActivity(), this.getRemarks());
    }

}
