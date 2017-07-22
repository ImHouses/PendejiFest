package mx.unam.ciencias.jcasas.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import mx.unam.ciencias.jcasas.pendejifest.R;

/**
 * Created by juanh on 5/11/2017.
 */

public class Date {

    private GregorianCalendar calendar;
    private static HashMap<Integer, Integer> months;
    private static HashMap<Integer, Integer> days;

    public Date(int year, int month, int day, int hour, int minute) {
        this.calendar = new GregorianCalendar(year, month, day, hour, minute);
        months = new HashMap<>();
        days = new HashMap<>();
        /* Filling the hash with the months. */
        months.put(1, R.string.month_1);
        months.put(2, R.string.month_2);
        months.put(3, R.string.month_3);
        months.put(4, R.string.month_4);
        months.put(5, R.string.month_5);
        months.put(6, R.string.month_6);
        months.put(7, R.string.month_7);
        months.put(8, R.string.month_8);
        months.put(9, R.string.month_9);
        months.put(10, R.string.month_10);
        months.put(11, R.string.month_11);
        months.put(12, R.string.month_12);
        /* Filling the hash with the days. */
        days.put(1, R.string.day_1);
        days.put(2, R.string.day_2);
        days.put(3, R.string.day_3);
        days.put(4, R.string.day_4);
        days.put(5, R.string.day_5);
        days.put(6, R.string.day_6);
        days.put(7, R.string.day_7);
    }
    public Date(int year, int month, int day) {
        this.calendar = new GregorianCalendar(year, month, day);
        months = new HashMap<>();
        days = new HashMap<>();
        /* Filling the hash with the months. */
        months.put(1, R.string.month_1);
        months.put(2, R.string.month_2);
        months.put(3, R.string.month_3);
        months.put(4, R.string.month_4);
        months.put(5, R.string.month_5);
        months.put(6, R.string.month_6);
        months.put(7, R.string.month_7);
        months.put(8, R.string.month_8);
        months.put(9, R.string.month_9);
        months.put(10, R.string.month_10);
        months.put(11, R.string.month_11);
        months.put(12, R.string.month_12);
        /* Filling the hash with the days. */
        days.put(1, R.string.day_1);
        days.put(2, R.string.day_2);
        days.put(3, R.string.day_3);
        days.put(4, R.string.day_4);
        days.put(5, R.string.day_5);
        days.put(6, R.string.day_6);
        days.put(7, R.string.day_7);
    }



    /**
     * Return an array where:
     * - The data of the index 0 is the string res of the day in this date.
     * - The data of the index 1 is the number of the day of month in this date.
     * - The data of the index 2 is the string resource of the month in this date.
     * - The data of the index 3 is the number of the year in this date.
     * @return An array with the date info for the events.
     */
    public int[] getDate() {
        return new int[]{
                days.get(calendar.get(Calendar.DAY_OF_WEEK)),
                calendar.get(Calendar.DAY_OF_MONTH),
                months.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)};
    }

}
