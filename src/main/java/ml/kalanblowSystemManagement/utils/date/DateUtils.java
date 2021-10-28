
package ml.kalanblowSystemManagement.utils.date;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import static java.time.Clock.systemUTC;
import static java.time.ZoneOffset.UTC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());

    /**
     * @return
     */

    public static LocalDateTime today() {

        return LocalDateTime.now(systemUTC());
    }

    /**
     * @return to day's date as dd-MM-yyyy'T'HH:mm:ss.SSS format
     */

    public static String todayAtToString() {
        log.info("Today format date is:{}", sdf.format(today()));
        return sdf.format(today());

    }

    /**
     * @param localDateTime
     * @return
     */
    public static String formattedDate(LocalDateTime localDateTime) {
        log.debug("localDatime is: {}", localDateTime);
        return localDateTime != null ? sdf.format(localDateTime) : todayAtToString();
    }

    /**
     * Calculate and return an expiration date as of {@code nowUtc} using the given period time.
     *
     * @param time
     *        time period
     * @param unit
     *        time unit of {@code time}
     * @return an expiration date
     */
    public static LocalDateTime expireNowUtc(int time, TemporalUnit unit) {
        return today().plus(time, unit);
    }

    /**
     * Returns a {@link Date} instance representing the given {@link LocalDateTime} according to
     * UTC.
     *
     * @return a date time in UTC
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(ldt -> ldt.toInstant(UTC)).map(Date::from)
                .orElse(null);
    }

    /**
     * Returns a {@link LocalDateTime} instance representing the given {@link Date} according to
     * UTC.
     *
     * @return a date time in UTC
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return Optional.ofNullable(date).map(Date::toInstant).map(i -> i.atOffset(UTC))
                .map(OffsetDateTime::toLocalDateTime).orElse(null);
    }
}
