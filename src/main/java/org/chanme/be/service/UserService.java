package org.chanme.be.service;

import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.chanme.be.util.MemberCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;
    private final MemberCodeGenerator gen;

    public UserService(UserRepository repo, MemberCodeGenerator gen) {
        this.repo = repo;
        this.gen = gen;
    }

    public User signup(String name, String phone, LocalDate birthDate) {
        // 1) 전화번호 중복 체크
        if (repo.existsByPhone(phone)) {
            throw new IllegalArgumentException("이미 가입된 전화번호입니다. 로그인 해주세요");
        }
        
        String chosen = null;

        // 동물을 여러 번 뽑으면서 최대 10회 시도
        for (int attempt = 0; attempt < 10 && chosen == null; attempt++) {
            String animal = gen.pickAnimal();
            String[] cands = gen.candidates(name, animal, birthDate);

            for (String cand : cands) {
                if (!repo.existsByMemberCode(cand)) {
                    chosen = cand;
                    break;
                }
            }
        }

        if (chosen == null) {
            throw new IllegalStateException("회원번호 생성 실패 (충돌 과다)");
        }

        User u = User.builder()
                .name(gen.normalizeName(name))
                .phone(phone)
                .birthDate(birthDate)
                .memberCode(chosen)
                .build();

        return repo.save(u);
    }
}