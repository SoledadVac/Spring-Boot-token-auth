package com.example.demo.authorize.filter;

import com.woasis.esbp.battery.admin.authorize.user.JwtUser;
import com.woasis.esbp.battery.admin.authorize.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午11:23
 * \* Description: 验证token
 * \
 */

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");

        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        if(!httpServletRequest.getMethod().equals("OPTIONS")){
            String authHeader = httpServletRequest.getHeader(this.tokenHeader);
            if (authHeader != null && authHeader.startsWith(tokenHead)) {
                final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
                String useraccount = jwtUtil.getUserAccountFromToken(authToken);
                if(logger.isInfoEnabled()){
                    logger.info("JwtAuthenticationTokenFilter_doFilterInternal checking authentication " + useraccount);
                }
                if (useraccount != null && SecurityContextHolder.getContext().getAuthentication() == null) {//token校验通过

                    JwtUser userDetails = (JwtUser)this.userDetailsService.loadUserByUsername(useraccount);//根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步

                    if (jwtUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                httpServletRequest));
                        if(logger.isInfoEnabled()){
                            logger.info("JwtAuthenticationTokenFilter_doFilterInternal  用户验证token,验证用户： " + useraccount + ", setting security context");
                        }
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }


    }
}
