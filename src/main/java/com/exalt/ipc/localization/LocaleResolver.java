package com.exalt.ipc.localization;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class LocaleResolver extends AcceptHeaderLocaleResolver {

    private static final List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("ar"));

//    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        System.out.println("language " + language);
        if (language == null || language.isEmpty()) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);
        Locale locale = Locale.lookup(list, LOCALES);
        System.out.println("Locale " + locale);
        return locale;
    }

    public Locale resolveLocale(WebRequest request) {
        String language = request.getHeader("Accept-Language");
        System.out.println("language " + language);
        if (language == null || language.isEmpty()) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);
        Locale locale = Locale.lookup(list, LOCALES);
        System.out.println("Locale " + locale);
        return locale;
    }

    @Override
    public void setDefaultLocale(@Nullable Locale defaultLocale) {
        super.setDefaultLocale(defaultLocale);
    }

}
