package org.chanme.be.api;

import jakarta.servlet.http.HttpServletResponse;
import org.chanme.be.api.dto.LoginReq;
import org.chanme.be.api.dto.SignupReq;
import org.chanme.be.api.dto.UserView;
import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.chanme.be.service.UserService;
import org.chanme.be.util.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;
    private final UserRepository repo; // 중복/조회에 사용

    public AuthController(UserService users, UserRepository repo) {
        this.users = users;
        this.repo = repo;
    }

    private static UserView view(User u) {
        return new UserView(u.getId(), u.getName(), u.getMemberCode(), u.getBirthDate());
    }

    /** 회원가입 + 자동로그인 */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReq req, HttpServletResponse res) {
        try {
            User saved = users.signup(req.name(), req.phone(), req.birthDate());
            CookieUtils.addLoginCookie(res, saved.getMemberCode());
            return ResponseEntity.created(URI.create("/api/auth/me")).body(view(saved));
        } catch (IllegalArgumentException e) {
            // 전화번호 중복
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    /** 회원번호로 로그인(쿠키 발급) */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req, HttpServletResponse res) {
        Optional<User> found = repo.findByMemberCode(req.memberCode());
        if (found.isEmpty()) return ResponseEntity.status(404).body(Map.of("error","회원번호를 찾을 수 없어요"));
        CookieUtils.addLoginCookie(res, found.get().getMemberCode());
        return ResponseEntity.ok(view(found.get()));
    }

    /** 로그아웃(쿠키 삭제) */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse res) {
        CookieUtils.clearLoginCookie(res);
        return ResponseEntity.ok(Map.of("message", "로그아웃했어요"));
    }
}
