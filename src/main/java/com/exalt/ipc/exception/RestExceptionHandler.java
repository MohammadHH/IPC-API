package com.exalt.ipc.exception;

import com.exalt.ipc.localization.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
	@Autowired
	LocaleService localeService;


	@ExceptionHandler(ApiException.class)
	public final ResponseEntity handleApiException(ApiException ex, HttpServletRequest request) {
		System.out.println("inside handleApiException()");
		return new ResponseEntity(new ApiError(ex.getStatus().value(), ex.getStatus().getReasonPhrase(),
				localeService.getMessage(request, ex.getMainCode()), localeService.getSubErrors(request, ex.getSubCodes()),
				Arrays.asList(ex.getStackTrace()).stream().limit(5).collect(Collectors.toList())), ex.getStatus());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity handleBadInput(HttpMessageNotReadableException ex, HttpServletRequest request) {

		return ResponseEntity.badRequest().body(
				new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
						localeService.getMessage(request, 300000), Collections.emptyList(),
						Arrays.asList(ex.getStackTrace()).stream().limit(5).collect(Collectors.toList())));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity handleUnsupportedInput(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
		return new ResponseEntity(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(),
						localeService.getMessage(request, 200000),
						Arrays.asList(localeService.getSubError(request, "" + ex.getContentType(), 200200)),
						Arrays.asList(ex.getStackTrace()).stream().limit(5).collect(Collectors.toList())),
				HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<com.exalt.ipc.exception.FieldError> fieldErrors = new LinkedList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.add(new com.exalt.ipc.exception.FieldError(error.getField(), error.getDefaultMessage()));
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			fieldErrors.add(new com.exalt.ipc.exception.FieldError(error.getObjectName(), error.getDefaultMessage()));
		}
		return new ResponseEntity(new ApiValidationError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
				HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), localeService.getMessage(request, 100000), fieldErrors,
				Arrays.asList(ex.getStackTrace()).stream().limit(5).collect(Collectors.toList())),
				HttpStatus.UNPROCESSABLE_ENTITY);

	}

}
