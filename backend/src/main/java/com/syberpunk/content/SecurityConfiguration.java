package com.syberpunk.content;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableConfigurationProperties(AdminProperties.class)
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, LoginAttemptService attempts) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/csrf", "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/health", "/api/categories", "/api/articles", "/api/articles/*", "/api/gallery", "/api/gallery/*").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/auth/me", "/api/auth/logout").authenticated()
            .anyRequest().denyAll())
            .requestCache(cache -> cache.disable())
            .formLogin(form -> form.loginProcessingUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    attempts.succeeded(request.getRemoteAddr());
                    log.info("Administrator signed in: {}", authentication.getName());
                    response.setStatus(204);
                })
                .failureHandler((request, response, exception) -> {
                    attempts.failed(request.getRemoteAddr());
                    problem(response, 401, "账号或密码错误");
                }))
            .logout(logout -> logout.logoutUrl("/api/auth/logout").deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(204)))
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> problem(response, 401, "请先登录管理员账号"))
                .accessDeniedHandler((request, response, exception) -> problem(response, 403, "没有操作权限或安全令牌已失效，请刷新后重试")))
            .addFilterBefore(new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
                    if ("POST".equals(request.getMethod()) && (request.getContextPath() + "/api/auth/login").equals(request.getRequestURI()) && attempts.blocked(request.getRemoteAddr())) {
                        response.setHeader("Retry-After", "300");
                        problem(response, 429, "登录失败次数过多，请 5 分钟后重试");
                        return;
                    }
                    if ("POST".equals(request.getMethod()) && (request.getContextPath() + "/api/auth/login").equals(request.getRequestURI())) {
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");
                        if (username == null || username.length() > 64 || password == null || password.getBytes(StandardCharsets.UTF_8).length > 72) {
                            attempts.failed(request.getRemoteAddr());
                            problem(response, 401, "账号或密码错误");
                            return;
                        }
                    }
                    chain.doFilter(request, response);
                }
            }, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static void problem(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/problem+json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\":" + status + ",\"detail\":\"" + message + "\"}");
    }
}
