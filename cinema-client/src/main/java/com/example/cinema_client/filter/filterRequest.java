package com.example.cinema_client.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.Role;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@Component
@Order(1)
public class filterRequest  implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(filterRequest.class);
    

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing filter :{}", this);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session=req.getSession();
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
 
        if(req.getRequestURI().contains("/admin")) {
        	if(jwtResponseDTO!=null) {
	        	for(Role role: jwtResponseDTO.getRoles()) {
	        		if(role.equals("ROLE_ADMIN")) {
	        			chain.doFilter(request, response);
	        			return;
	        		}
	        	}
        	}
    		res.sendRedirect("/");
    		return;
        }
        LOG.info("Starting Transaction for req :{}", req.getRequestURI());
        chain.doFilter(request, response);
        LOG.info("Committing Transaction for req :{}", req.getRequestURI());
    }

    @Override
    public void destroy() {
        LOG.warn("Destructing filter :{}", this);
    }
}