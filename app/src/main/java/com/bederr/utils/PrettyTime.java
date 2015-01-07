package com.bederr.utils;

import android.content.Context;

import pe.bederr.com.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jnolascob on 03/08/2014.
 */
public class PrettyTime {
    private Context context;

    public PrettyTime(Context context) {
        this.context = context;
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getAge(final Date birthdate) {
        Date current = Calendar.getInstance().getTime();
        if (birthdate == null) {
            return "";
        }
        if (current == null) {
            return "";
        } else {
            final Calendar calend = new GregorianCalendar();
            calend.set(Calendar.HOUR_OF_DAY, 0);
            calend.set(Calendar.MINUTE, 0);
            calend.set(Calendar.SECOND, 0);
            calend.set(Calendar.MILLISECOND, 0);

            calend.setTimeInMillis(current.getTime() - birthdate.getTime());

            float result = 0;
            result = calend.get(Calendar.YEAR) - 1970;
            result += (float) calend.get(Calendar.MONTH) / (float) 12;
            return String.valueOf(result);
        }

    }

    public String getTimeAgo(Date date) {

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {
            timeAgo = context.getResources().getString(R.string.date_util_term_less) + " " +  context.getResources().getString(R.string.date_util_term_a) + " " + context.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim == 1) {
            return "1 " + context.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + context.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_about) + " "+context.getResources().getString(R.string.date_util_term_an)+ " " + context.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 60)) + " " + context.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + context.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " + context.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_about) + " "+context.getResources().getString(R.string.date_util_term_a)+ " " + context.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " + context.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_about) + " "+context.getResources().getString(R.string.date_util_term_a)+ " " + context.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_over) + " "+context.getResources().getString(R.string.date_util_term_a)+ " " + context.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_almost) + " 2 " + context.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo = context.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 525600)) + " " + context.getResources().getString(R.string.date_util_unit_years);
        }

        return timeAgo + " " + context.getResources().getString(R.string.date_util_suffix);
    }

    private static int getTimeDistanceInMinutes(long time) {
        int timeDistance;
        timeDistance = (int) (currentDate().getTime() - time);
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
}
