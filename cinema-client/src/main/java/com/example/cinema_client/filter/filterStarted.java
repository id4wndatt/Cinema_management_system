package com.example.cinema_client.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class filterStarted implements Filter{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession();

	    if (session.isNew()) {
	        // New session? OK, redirect to encoded URL with jsessionid in it (and implicitly also set cookie).
	        res.sendRedirect(res.encodeRedirectURL(req.getRequestURI()));
	        return;
	    } else if (session.getAttribute("verified") == null) {
	        // Session has not been verified yet? OK, mark it verified so that we don't need to repeat this.
	        session.setAttribute("verified", true);
	        if (req.isRequestedSessionIdFromCookie()) {
	            // Supports cookies? OK, redirect to unencoded URL to get rid of jsessionid in URL.
	            res.sendRedirect(req.getRequestURI().split(";")[0]);
	            return;
	        }
	    }

	    chain.doFilter(request, response);
	}
}
