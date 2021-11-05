package ml.kalanblowSystemManagement.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ml.kalanblowSystemManagement.dto.response.Response;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /*
     * @SuppressWarnings("unchecked")
     * 
     * @ExceptionHandler(KalanblowSystemManagementException.EntityNotFoundException.class) public
     * final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) { Response
     * response = Response.notFound(); response.addErrorMsgToResponse(ex.getMessage(), ex); return
     * new ResponseEntity(response, HttpStatus.NOT_FOUND); }
     * 
     * @SuppressWarnings("unchecked")
     * 
     * @ExceptionHandler(KalanblowSystemManagementException.DuplicateEntityException.class) public
     * final ResponseEntity handleNotFountExceptions1(Exception ex, WebRequest request) { Response
     * response = Response.duplicateEntity(); response.addErrorMsgToResponse(ex.getMessage(), ex);
     * return new ResponseEntity(response, HttpStatus.CONFLICT); }
     * 
     * 
     * @ResponseStatus(HttpStatus.CONFLICT)
     * 
     * @ExceptionHandler({DataIntegrityViolationException.class,
     * ObjectOptimisticLockingFailureException.class }) public ModelAndView handleConflict(
     * HttpServletRequest request, ExceptionHandler e) {
     * 
     * ModelAndView result= new ModelAndView("error/409"); result.addObject("url",
     * request.getRequestURL()); return result;
     * 
     * }
     */
}
