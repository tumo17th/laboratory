package com.uls.poittokosu.app.google;

import java.io.IOException;
import java.util.List;

import com.google.api.services.calendar.model.Event;

public interface GoogleCalendarAPIService {

    /**
     * Google Calendar APIにアクセスして、Eventのリストを取得します
     * @param year
     * @param month
     * @return
     * @throws IOException
     */
    List<Event> fetchEventList(int year, int month) throws IOException;

}
