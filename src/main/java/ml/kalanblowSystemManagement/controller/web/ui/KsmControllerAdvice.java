
package ml.kalanblowSystemManagement.controller.web.ui;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
//import ml.kalanblowSystemManagement.utils.LocalDateTimeEditor;

@Slf4j
@ControllerAdvice
public class KsmControllerAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final Throwable throwable, ModelAndView modelAndView) {

        modelAndView = new ModelAndView("error/error");
        log.error("Exception during execution of kalanblow application", throwable);
        StringBuffer sb = new StringBuffer();
        sb.append("Exception during execution of Spring Security application!  ");
        sb.append((throwable != null && throwable.getMessage() != null ? throwable.getMessage()
                : "Unknown error"));
        if (throwable != null && throwable.getCause() != null) {
            sb.append("\n\nroot cause: ").append(throwable.getCause());
        }
        modelAndView.addObject("error", sb.toString());

        return modelAndView;
    }

    @Value("${application.version}")
    private String version;

    @ModelAttribute("version")
    public String getVersion() {
        return version;
    }
}
