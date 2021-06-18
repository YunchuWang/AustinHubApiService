package com.austinhub.apiservice.utils;

import com.austinhub.apiservice.model.enums.PricingPlan;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApplicationUtils {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Timestamp calculateOrderItemExpirationTimestamp(PricingPlan plan, Date createdTimestamp) {
        return calculateTimestamp(createdTimestamp, Calendar.MONTH, plan.getNumberOfMonths());
    }

    public static Timestamp calculateTransactionExpirationTimestamp(int numberOfDays, Date createdTimestamp) {
        return calculateTimestamp(createdTimestamp, Calendar.DATE, numberOfDays);
    }

    public static Timestamp calculateTimestamp(Date createdTimestamp, int type, int length) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdTimestamp);
        cal.add(type, length);
        return new Timestamp(cal.getTime().getTime());
    }

    public static java.sql.Timestamp parseTimestamp(String timestamp) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
