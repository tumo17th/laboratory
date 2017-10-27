package com.uls.poittokosu.app.google.model;

import com.uls.poittokosu.app.template.model.TemplateRow;

/** GoogleCalendarAPIから読み込んだテンプレート行 */
public class GCATemplateRow extends TemplateRow {

    /** 日付(GoogleCalendarAPI用 >> 毎週ではなく特定の日付のみ値を設定する時に利用する) */
    private Integer targetDate;

    /** 日付(GoogleCalendarAPI用 >> 毎週ではなく特定の日付のみ値を設定する時に利用する) */
    public Integer getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(Integer targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public String toString() {
        return "GCATemplateRow [targetDate=" + targetDate + ", getDayOfWeek()=" + getDayOfWeek() + ", getStartTime()="
                + getStartTime() + ", getEndTime()=" + getEndTime() + ", getBusinessCode()=" + getBusinessCode()
                + ", getProcess()=" + getProcess() + ", getProduct()=" + getProduct() + ", getActivity()="
                + getActivity() + ", getRemarks()=" + getRemarks() + ", toString()=" + super.toString()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
    }

}
