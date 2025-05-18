package com.quiz.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.quiz.main.model.User;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private HttpSession session;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Comment out this interceptor as we're using Spring Security's built-in mechanisms now
        /*
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                User currentUser = (User) session.getAttribute("currentUser");
                if (currentUser == null || !"ROLE_ADMIN".equals(currentUser.getRole())) {
                    response.sendRedirect("/login");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/admin/**");
        */
    }
}
