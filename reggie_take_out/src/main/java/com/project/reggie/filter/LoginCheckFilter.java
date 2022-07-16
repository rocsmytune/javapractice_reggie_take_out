package com.project.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.project.reggie.common.R;
import com.sun.tools.javac.comp.Check;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * check if user login successfully
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*") //which paths need to be filtered
@Slf4j
public class LoginCheckFilter implements Filter {
    //path matcher
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // get url of current request
        String requestURI = request.getRequestURI();

        //define unnecessary request paths
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        // judge if this request need to be handled
        boolean check = check(requestURI, urls);

        //if not leave out
        if (check) {
            log.info("Current request : {} is in white list", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //judge login status, if login success, then leave out
        if (request.getSession().getAttribute("employee") != null) {
            log.info("User Login, ID: {} ", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        log.info("User not login!");
        //if not, return unsuccessful login result page
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /***
     * path match check
     * @param requestURI
     * @param  urls
     * @return
     */
    public boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
