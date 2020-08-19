package vn.inspiron.mcontract.modules.Common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public final class Util {

    public static Date calculateDateFromNow(int timeInSeconds) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, timeInSeconds);
        date = calendar.getTime();

        return date;
    }

    public static String getFileExtension(String filename) {
        String extension = "";
        try {
            extension = filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }

    public static String randomFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString();
        if (!extension.equals("")) {
            newFilename += "." + extension;
        }
        return newFilename;
    }

}
