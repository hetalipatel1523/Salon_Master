package org.salonmaster.salonmaster.Controller;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;

    public CalendarActivity(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                '}';
    }
}
