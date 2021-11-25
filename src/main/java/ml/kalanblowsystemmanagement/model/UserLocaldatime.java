
package ml.kalanblowsystemmanagement.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

public class UserLocaldatime {

    public LocalDateTime getLocaldatetimeUsingparseMethod(String localdatetime) {

        return LocalDateTime.parse(localdatetime);
    }

    LocalDateTime getEndOfDayFromLocaldatetimeDirectly(LocalDateTime localDateTime) {
        LocalDateTime endOfDate =
                localDateTime.with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay());
        return endOfDate;

    }

    LocalDateTime ofEpochSecond(int epochSecond, ZoneOffset zoneOffset) {
        return LocalDateTime.ofEpochSecond(epochSecond, epochSecond, zoneOffset);
    }
}
