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
}
