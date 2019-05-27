package org.openbpm.security.filter;

import org.openbpm.security.IgnoreChecker;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class SecurityRequestCsrfMatcher extends IgnoreChecker implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {

        boolean isIngoreUrl = isIgnores(request.getRequestURI());

        if (isIngoreUrl) return true;

        return false;
    }

}
