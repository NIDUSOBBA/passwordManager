package org.example.utile;

import java.sql.Timestamp;

public class TimestampUtil {

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
