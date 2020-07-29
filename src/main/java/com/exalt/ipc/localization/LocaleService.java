package com.exalt.ipc.localization;

import com.exalt.ipc.exception.SubCodeError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class LocaleService {

	public static final String ERROR = "error.";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	public String getMessage(HttpServletRequest request, int code) {
		return messageSource.getMessage(ERROR + code, null, localeResolver.resolveLocale(request));
	}

	public String getMessage(HttpServletRequest request, String additional, int code) {
		return String.format("%s '%s'", messageSource.getMessage(ERROR + code, null, localeResolver.resolveLocale(request)),
				additional);
	}

	public SubCodeError getSubError(HttpServletRequest request, int code) {
		return new SubCodeError(code, getMessage(request, code));
	}

	public SubCodeError getSubError(HttpServletRequest request, String additional, int code) {
		return new SubCodeError(code, getMessage(request, additional, code));
	}

	public List<SubCodeError> getSubErrors(HttpServletRequest request, int... codes) {
		LinkedList<SubCodeError> list = new LinkedList<>();
		Arrays.stream(codes).forEach(code -> list.add(getSubError(request, code)));
		return list;
	}
}
