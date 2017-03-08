package com.balaev.gradproj.util;

public class ValidationUtils {
    private ValidationUtils() {
    }

    private static final String ONLY_DIGITS = "[0-9]+";
    private static final String STATION_NAME = "^[a-zA-Zа-я]+[- ]*[a-zA-Zа-я0-9]+$";
    private static final String LOGIN_PASSWORD = "^[a-zA-Z0-9]*$";
    private static final String DATE_CHECK = "(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
    private static final String FULL_NAME = "^[a-zA-Z]+ [a-zA-Z]+$";
    private static final String NAME = "^[a-zA-Z]+$";
    private static final String DATETIME = "^(((0[1-9]|[12]\\d|3[01])[\\/\\.-](0[13578]|1[02])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))|((0[1-9]|[12]\\d|30)[\\/\\.-](0[13456789]|1[012])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))|((0[1-9]|1\\d|2[0-8])[\\/\\.-](02)[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))|((29)[\\/\\.-](02)[\\/\\.-]((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))\\s(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])))$";
    private static final String ROUTE_NAME = "^[0-9a-zA-Z]+$";
    private static final String STATION_IN_ROUTE = "^[2-5]$";


    public static boolean checkNumber(String str) {
        return str.matches(ONLY_DIGITS);
    }

    public static boolean checkNumberOfStationsInRoute(String str) {
        return str != null && !str.isEmpty() && str.matches(STATION_IN_ROUTE);
    }

    public static boolean checkNumberSeats(Integer seats) {
        return (seats <= 15 && seats > 0);
    }

    public static boolean checkLogin(String login) {
        return login != null && !login.isEmpty() && login.matches(LOGIN_PASSWORD);
    }

    public static boolean checkPassword(String password) {
        return password != null && !password.isEmpty() && password.matches(LOGIN_PASSWORD);
    }

    public static boolean checkStationName(String name) {
        return name != null && !name.isEmpty() && name.matches(STATION_NAME);
    }

    public static boolean checkDate(String date) {
        return date != null && !date.isEmpty() && date.matches(DATE_CHECK);
    }

    public static boolean checkDatetime(String datetime) {
        return datetime != null && !datetime.isEmpty() && datetime.matches(DATETIME);
    }

    public static boolean checkName(String name) {
        return name != null && !name.isEmpty() && name.matches(NAME);
    }

    public static boolean checkRouteName(String name) {
        return name != null && !name.isEmpty() && name.matches(ROUTE_NAME);
    }

    public static boolean checkFullName(String name) {
        return name != null && !name.isEmpty() && name.matches(FULL_NAME);
    }
}
