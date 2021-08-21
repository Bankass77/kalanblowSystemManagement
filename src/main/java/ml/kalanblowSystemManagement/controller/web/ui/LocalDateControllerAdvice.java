
package ml.kalanblowSystemManagement.controller.web.ui;

import java.beans.PropertyEditorSupport;
import java.text.Format;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;



@ControllerAdvice
public class LocalDateControllerAdvice {


    @Value("${application.version}")
    private String version;

    @ModelAttribute("version")
    public String getVersion() {
    return version;
    }
    private static class Editor<T> extends PropertyEditorSupport {

        private final Function<String, T> parser;
        private final Format format;

        public Editor(Function<String, T> parser, Format format) {

            this.parser = parser;
            this.format = format;
        }

        public void setAsText(String text) {

            setValue(this.parser.apply(text));
        }

        @SuppressWarnings("unchecked")
        public String getAsText() {

            return format.format((T) getValue());
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        webDataBinder.registerCustomEditor(Instant.class,
                new Editor<>(Instant::parse, DateTimeFormatter.ISO_INSTANT.toFormat()));

        webDataBinder.registerCustomEditor(LocalDate.class,
                new Editor<>(
                        text -> LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").toFormat()));

        webDataBinder.registerCustomEditor(LocalDateTime.class,
                new Editor<>(
                        text -> LocalDateTime.parse(text,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").toFormat()));

        webDataBinder.registerCustomEditor(LocalTime.class,
                new Editor<>(text -> LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME),
                        DateTimeFormatter.ISO_LOCAL_TIME.toFormat()));

        webDataBinder.registerCustomEditor(OffsetDateTime.class,
                new Editor<>(
                        text -> OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.toFormat()));

        webDataBinder.registerCustomEditor(OffsetTime.class,
                new Editor<>(text -> OffsetTime.parse(text, DateTimeFormatter.ISO_OFFSET_TIME),
                        DateTimeFormatter.ISO_OFFSET_TIME.toFormat()));

        webDataBinder.registerCustomEditor(ZonedDateTime.class,
                new Editor<>(
                        text -> ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME),
                        DateTimeFormatter.ISO_ZONED_DATE_TIME.toFormat()));
        

        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(false); //<.>
        webDataBinder.registerCustomEditor(String.class, stringtrimmer); //<.>
    }

    

}
