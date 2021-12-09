
package ml.kalanblowSystemManagement.utils.date;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class FrenchLocalDateFormater implements Formatter<LocalDate> {

    public static final String FRENCH_PATTER = "dd/MM/yyyy";
    public static final String NORMAL_PATTERN = "dd/MM/yyyy";

    @Override
    public String print(LocalDate object, Locale locale) {

        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {

        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    public static String getPattern(Locale locale) {

        return isFrench(locale) ? FRENCH_PATTER : NORMAL_PATTERN;
    }

    private static boolean isFrench(Locale locale) {

        return Locale.FRANCE.getCountry().equals(locale.getCountry());
    }

}
