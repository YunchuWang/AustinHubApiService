package com.austinhub.apiservice.utils;

import com.austinhub.apiservice.model.enums.PricingPlan;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApplicationUtils {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Timestamp calculateOrderItemExpirationTimestamp(PricingPlan plan, Date timestamp) {
        return calculateTimestamp(timestamp, Calendar.MONTH, plan.getNumberOfMonths());
    }

    public static Timestamp calculateTransactionExpirationTimestamp(int numberOfDays, Date timestamp) {
        return calculateTimestamp(timestamp, Calendar.DATE, numberOfDays);
    }

    public static Timestamp calculateTimestamp(Date timestamp, int type, int length) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(type, length);
        return new Timestamp(cal.getTime().getTime());
    }
}
