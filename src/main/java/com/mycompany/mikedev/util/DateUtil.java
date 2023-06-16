package com.mycompany.mikedev.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DateUtil {
    public static ZoneId zoneId = ZoneId.of("America/Port-au-Prince");
    public static void getCurrentTimeWithTimeZone(){
        LocalTime localTime= LocalTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime=localTime.format(formatter);
    }

    public static String genererInt(int borneInf, int borneSup, int maxLength){
        Random random = new Random();
        String nb;
        nb = "" + (borneInf+random.nextInt(borneSup-borneInf));
        for (int i=0;i<(maxLength-nb.length());i++) {
            nb = "0" + nb;
        }
        return nb;
    }



    public static LocalDateTime getCurrentDate(){
        LocalDateTime localTime= LocalDateTime.now(zoneId);
        return localTime;
    }

    public static Timestamp getCurrentDatetoTimetamp(){
        LocalDateTime localTime= LocalDateTime.now(zoneId);
        return Timestamp.from(localTime.atZone(DateUtil.zoneId).toInstant());
    }

    public static Timestamp getCurrentDatetoInstant(){
        LocalDateTime localTime= LocalDateTime.now(zoneId);
        return Timestamp.from(localTime.atZone(DateUtil.zoneId).toInstant());
    }



    public static void getCurrentDateWithTimeZone(){
        LocalDateTime localTime= LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-mm-dd HH:mm:ss");
        String formattedTime=localTime.format(formatter);
    }

    public static long computeDifferenceBetweenDateInSeconds(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.SECONDS.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static long computeDifferenceBetweenDateInMinute(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.MINUTES.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static long computeDifferenceBetweenDateInHours(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.HOURS.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static long computeDifferenceBetweenDateInMonth(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.MONTHS.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static long computeDifferenceBetweenDateInYear(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.YEARS.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static long computeDifferenceBetweenDateInDay(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime fromZonedDate = startDate.atZone(zoneId);
        ZonedDateTime toZonedDate = endDate.atZone(zoneId);

        long difference = ChronoUnit.DAYS.between(fromZonedDate, toZonedDate);
        return difference;
    }

    public static String getMonthValue(int monthNumber) {
        Map<Integer, String> monthOfYear = null;
        monthOfYear = new HashMap<>();
        monthOfYear.put(1, "Janvier");
        monthOfYear.put(2, "Fevrier");
        monthOfYear.put(3, "Mars");
        monthOfYear.put(4, "Avril");
        monthOfYear.put(5, "Mai");
        monthOfYear.put(6, "Juin");
        monthOfYear.put(7, "Juillet");
        monthOfYear.put(8, "Aout");
        monthOfYear.put(9, "Septembre");
        monthOfYear.put(10, "Octobre");
        monthOfYear.put(11, "Novembre");
        monthOfYear.put(12, "Decembre");
        return monthOfYear.get(monthOfYear.containsKey(monthNumber) ? monthNumber : 1);
    }

    public static String getDateInString(LocalDateTime localDateTime, String separator) {
        String date = "".concat("" + localDateTime.getYear())
            .concat(separator + (localDateTime.getMonthValue() < 10 ? "0"+localDateTime.getMonthValue() : localDateTime.getMonthValue()))
            .concat(separator + (localDateTime.getDayOfMonth() < 10 ? "0"+localDateTime.getDayOfMonth() : localDateTime.getDayOfMonth()));
        return date;
    }


    public static String getDateAndHourInString(LocalDateTime todayDate, String separatorDate) {
        String date = "".concat("" + todayDate.getYear())
            .concat(separatorDate + (todayDate.getMonthValue() < 10 ? "0"+todayDate.getMonthValue() : todayDate.getMonthValue()))
            .concat(separatorDate + (todayDate.getDayOfMonth() < 10 ? "0"+todayDate.getDayOfMonth() : todayDate.getDayOfMonth()))
            .concat(" " + (todayDate.getHour() <  10 ? "0"+todayDate.getHour() : todayDate.getHour()))
            .concat(":" + (todayDate.getMinute() < 10 ? "0"+todayDate.getMinute() : todayDate.getMinute()));
        return date;
    }


    public static String getShortDateAndHourInString(LocalDateTime todayDate, String separatorDate) {
        String date = ""
            .concat((todayDate.getDayOfMonth() < 10 ? "0"+todayDate.getDayOfMonth() : "" + todayDate.getDayOfMonth()))
            .concat(separatorDate + (todayDate.getMonthValue() < 10 ? "0"+todayDate.getMonthValue() : todayDate.getMonthValue()))
            .concat(" " + (todayDate.getHour() < 10 ? "0"+todayDate.getHour() : todayDate.getHour()))
            .concat(":" + (todayDate.getMinute() < 10 ? "0"+todayDate.getMinute() : todayDate.getMinute()));
        return date;
    }

    public static String getDateAndHourSecondeInString(LocalDateTime todayDate, String separatorDate) {
        String date = "".concat("" + todayDate.getYear())
            .concat(separatorDate + (todayDate.getMonthValue() < 10 ? "0"+todayDate.getMonthValue() : todayDate.getMonthValue()))
            .concat(separatorDate + (todayDate.getDayOfMonth() < 10 ? "0"+todayDate.getDayOfMonth() : todayDate.getDayOfMonth()))
            .concat(" " + (todayDate.getHour() < 10 ? "0"+todayDate.getHour() : todayDate.getHour()))
            .concat(":" + (todayDate.getMinute() < 10 ? "0"+todayDate.getMinute() : todayDate.getMinute()))
            .concat(":" + (todayDate.getSecond() < 10 ? "0"+todayDate.getSecond() : todayDate.getSecond()));
        return date;
    }
}
