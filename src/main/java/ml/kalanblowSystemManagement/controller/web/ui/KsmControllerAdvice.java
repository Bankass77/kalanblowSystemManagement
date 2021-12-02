
package ml.kalanblowSystemManagement.controller.web.ui;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import lombok.extern.slf4j.Slf4j;
//import ml.kalanblowSystemManagement.utils.LocalDateTimeEditor;
import ml.kalanblowsystemmanagement.dto.response.Response;
import ml.kalanblowsystemmanagement.exception.KalanblowSystemManagementException;

@Slf4j
@ControllerAdvice
public class KsmControllerAdvice  extends ResponseEntityExceptionHandler{

    @ExceptionHandler(KalanblowSystemManagementException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final Exception ex, ModelAndView modelAndView) {

        modelAndView = new ModelAndView("error/404");
        log.error("Exception during execution of kalanblow application", ex);
        StringBuffer sb = new StringBuffer();
        sb.append("Exception during execution of Spring Security application!  ");
        sb.append((ex != null && ex.getMessage() != null ? ex.getMessage()
                : "Unknown error"));
        if (ex != null && ex.getCause() != null) {
            sb.append("\n\nroot cause: ").append(ex.getCause());
        }
        modelAndView.addObject("error", sb.toString());

        return modelAndView;
    }
    @SuppressWarnings("unchecked")
    @ExceptionHandler(KalanblowSystemManagementException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(KalanblowSystemManagementException.DuplicateEntityException.class)
    public final ResponseEntity handleNotFountExceptions1(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class,ObjectOptimisticLockingFailureException.class
    })
    public ModelAndView handleConflict( HttpServletRequest request, ExceptionHandler e) {
     
        ModelAndView result= new ModelAndView("error/409");
        result.addObject("url", request.getRequestURL());
        return result;
        
    }
    @Value("${application.version}")
    private String version;

    @ModelAttribute("version")
    public String getVersion() {
        return version;
    }
    @InitBinder //<.>
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(false); //<.>
        binder.registerCustomEditor(String.class, stringtrimmer); //<.>
    }
}
