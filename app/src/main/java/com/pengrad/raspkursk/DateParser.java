package com.pengrad.raspkursk;

/**
 * stas
 * 7/21/15
 */
public class DateParser {

    public static String timeNoSecs(String datetime) {
        switch (datetime.length()) {
            case 5: // 22:33
                return datetime;
            case 8: // 22:33:41
                return datetime.substring(0, 5);
            case 16: // 2015-01-01 12:55
                return datetime.substring(11);
            case 19: // 2015-01-01 12:55:44
                return datetime.substring(11, 16);
            default:
                return datetime;
        }
    }
}
