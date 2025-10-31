package co.za.tlhalefosebaeng.leagueoracle.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppDates {
    // The provided date should be in the format yyyy-mm-dd,hh:mm to simplify data extraction
    public static LocalDateTime convertDate(String date) {
        // Avoid null pointer exception
        if(date == null) return null;

        // Extract the date and time details from the given string
        String[] dateTimeDetails = date.split(",");
        String[] dateDetails = dateTimeDetails[0].split("-");
        String[] timeDetails = dateTimeDetails[1].split(":");

        // Set the local date using the extracted date details
        LocalDate localDate = LocalDate.of(
                Integer.parseInt(dateDetails[0]),
                Integer.parseInt(dateDetails[1]),
                Integer.parseInt(dateDetails[2])
        );

        // Set the local time using the extracted time details
        LocalTime localTime = LocalTime.of(
                Integer.parseInt(timeDetails[0]),
                Integer.parseInt(timeDetails[1])
        );

        // Compile local date and time and return it
        return LocalDateTime.of(localDate, localTime);
    }

    // The returned date is in the format yyyy-mm-dd,hh:mm but the fixture response formats the
    // date and time fields so that it can be easier for clients to read
    public static String retrieveDate(LocalDate localDate, LocalTime localTime) {
        // Convert the local date and time to string and return them in the correct format
        return localDate.toString() + "," + localTime.toString();
    }
}
