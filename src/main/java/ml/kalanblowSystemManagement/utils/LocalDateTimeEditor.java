/*
 * package ml.kalanblowSystemManagement.utils;
 * 
 * import java.beans.PropertyEditorSupport; import java.time.LocalDateTime; import
 * java.time.format.DateTimeFormatter;
 * 
 * public class LocalDateTimeEditor extends PropertyEditorSupport {
 * 
 * private static final DateTimeFormatter DATE_TIME_FORMATTER =
 * DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
 * 
 * // Converts a String to a LocalDateTime (when submitting form)
 * 
 * @Override public void setAsText(String text) {
 * 
 * LocalDateTime localDateTime = LocalDateTime.parse(text, DATE_TIME_FORMATTER);
 * this.setValue(localDateTime); }
 * 
 * // Converts a LocalDateTime to a String (when displaying form)
 * 
 * @Override public String getAsText() {
 * 
 * String time = ((LocalDateTime) getValue()).format(DATE_TIME_FORMATTER); return time; }
 * 
 * }
 */
