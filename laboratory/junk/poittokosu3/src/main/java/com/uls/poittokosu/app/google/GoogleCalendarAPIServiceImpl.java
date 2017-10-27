package com.uls.poittokosu.app.google;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

@Service
public class GoogleCalendarAPIServiceImpl implements GoogleCalendarAPIService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarAPIServiceImpl.class);

    /** Application name. */
    private static final String APPLICATION_NAME = "Poitto Kosu";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
            ".credentials/google-calenar-api-poittokosu");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/google-calendar-api-poittokosu
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public List<Event> fetchEventList(int year, int month) throws IOException {
        com.google.api.services.calendar.Calendar service = getCalendarService();

        DateTime from = getFirstDateOfMonth(year, month);
        DateTime to = getLastDateOfMonth(year, month);
        List<Event> eventList = service.events().list("primary").setMaxResults(100).setTimeMin(from).setTimeMax(to)
                .setOrderBy("startTime").setSingleEvents(true).execute().getItems();

        printEvents(eventList);
        return eventList;
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new BufferedInputStream(new FileInputStream("config/client_secret.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        logger.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    private static com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    private DateTime getFirstDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1, 0, 0);
        return new DateTime(cal.getTimeInMillis());
    }

    private DateTime getLastDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1, 0, 0);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new DateTime(cal.getTimeInMillis());
    }

    private void printEvents(List<Event> eventList) {
        SimpleDateFormat SDF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm");
        if (eventList.size() == 0) {
            logger.info("No events found.");
        } else {
            logger.info(">>>>> [Google Calendar Events] >>>>>");
            for (Event event : eventList) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null || end == null) {
                    continue;
                }
                Date startDate = new Date(start.getValue());
                Date endDate = new Date(end.getValue());
                logger.info("{} ({} - {})", event.getSummary(), SDF_FULL.format(startDate), SDF_TIME.format(endDate));
            }
            logger.info("<<<<< [Google Calendar Events ↑↑↑↑↑] <<<<<");
        }
    }
}
