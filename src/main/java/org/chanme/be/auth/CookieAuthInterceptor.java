// src/main/java/org/chanme/be/auth/CookieAuthInterceptor.java
package org.chanme.be.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.chanme.be.util.CookieUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * 요청마다 로그인 쿠키(CHNME_UID)를 확인해
 * 회원번호(memberCode)로 사용자를 조회하고
 * 요청 attribute에 담아두는 인터셉터.
 */
@Component
public class CookieAuthInterceptor implements HandlerInterceptor {

    /** 컨트롤러 등에서 현재 로그인 사용자를 꺼낼 때 사용하는 attribute 키 */
    public static final String REQ_USER_ATTR = "LOGIN_USER";

    private final UserRepository userRepository;

    public CookieAuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String memberCode = CookieUtils.resolveMemberCode(request);

        if (memberCode != null && !memberCode.isBlank()) {
            Optional<User> userOpt = userRepository.findByMemberCode(memberCode);
            userOpt.ifPresent(user -> request.setAttribute(REQ_USER_ATTR, user));
        }

        return true;
    }

    /** 요청 attribute에서 현재 로그인 사용자 반환 (없으면 null) */
    public static User currentUser(HttpServletRequest request) {
        Object v = request.getAttribute(REQ_USER_ATTR);
        return (v instanceof User) ? (User) v : null;
    }
}