package com.exalt.ipc.exception;

import com.exalt.ipc.localization.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
//@Component
public class ExceptionHandlerImpl extends ResponseEntityExceptionHandler {
    @Autowired
    ExceptionResponse exceptionResponse;
    @Autowired
    LocaleService localeService;

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity handleCustomException(CustomException ex, WebRequest request) throws Exception {
        System.out.println("handling the exception " + request.getHeader("Accept-Language"));
        System.out.println("localeService " + localeService.getMessage("error.7070"));
        return new ResponseEntity(new ExceptionResponse(ex), HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(MultipartException.class)
    public final ResponseEntity handleMultipartException(MultipartException ex, WebRequest request) throws Exception {
        CustomException customException = new CustomException("7012", request, HttpStatus.BAD_REQUEST, localeService, 7012, 7013);
        customException.setMessage(ex.getLocalizedMessage());
        customException.setTrace(ex.getStackTrace());
        return new ResponseEntity(new ExceptionResponse(customException), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity handleAccessDeniedException(AccessDeniedException ex, WebRequest request) throws Exception {
        CustomException customException = new CustomException("7002", request, HttpStatus.UNAUTHORIZED, localeService, 7003, 7004);
        return new ResponseEntity(new ExceptionResponse(customException), HttpStatus.FORBIDDEN);
    }

    /**
     * Customize the response for MethodArgumentNotValidException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        List<Map<String, String>> subErrors = new LinkedList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            Map map = new HashMap();
            map.put("fieldName", error.getField());
            map.put("errorMessage", error.getDefaultMessage());
            subErrors.add(map);
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
            Map map = new HashMap();
            map.put("fieldName", error.getObjectName());
            map.put("errorMessage", error.getDefaultMessage());
            subErrors.add(map);
        }
        CustomException customException = new CustomException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        customException.setSubErrors(subErrors);
        return new ResponseEntity(new ExceptionResponse(customException), HttpStatus.BAD_REQUEST);

    }


}
