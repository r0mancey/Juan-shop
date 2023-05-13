package com.example.juan.filter;

import com.alibaba.fastjson.JSON;
import com.example.juan.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//todo:用户端拦截

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取当前请求URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);

        //2、定义不需要处理的请求
        String[] urls = new String[] {
                "/admin/login",
                "/admin/logout",
                "/backend/**"
        };

        //3、判断该请求是否需要处理
        boolean check = check(urls, requestURI);

        //4、无需处理则直接放行
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //5、否则验证是否登录
        Long adminId = (Long) request.getSession().getAttribute("admin");  //登录时，在controller将Id存入了Session
        if (adminId != null){
            log.info("用户已登录");
            filterChain.doFilter(request,response);
            return;
        }

        //6、未登录则返回未登录结果，通过输出流向客户端页面返回数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查当前请求是否是不需要处理的请求
     * @param urls 不需要处理的路径
     * @param requestURI 请求路径
     * @return 不需要处理返回true，否则返回false
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
