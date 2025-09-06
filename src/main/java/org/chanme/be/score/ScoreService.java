package org.chanme.be.score;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.chanme.be.auth.CookieAuthInterceptor; // 인터셉터 import
import org.chanme.be.domain.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreDTO.Response calculateAndSaveScore(ScoreDTO.Request scoreRequest, HttpServletRequest request) {
        // 1. 인터셉터가 request에 담아준 현재 로그인 사용자 정보를 가져옵니다.
        User currentUser = CookieAuthInterceptor.currentUser(request);
        if (currentUser == null) {
            // 인터셉터가 사용자를 찾지 못했거나, 비로그인 상태의 요청인 경우
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다. (인증 실패)");
        }

        // 2. 세션에서 문제 풀이 시작 시간을 가져옵니다. (이 부분은 기존과 동일)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("questionStartTime") == null) {
            throw new IllegalStateException("문제 풀이 시작 기록이 없습니다. 점수를 계산할 수 없습니다.");
        }

        // 3. 걸린 시간과 점수를 계산합니다. (기존과 동일)
        long startTime = (long) session.getAttribute("questionStartTime");
        long endTime = System.currentTimeMillis();
        long elapsedTimeInSeconds = (endTime - startTime) / 1000;
        int calculatedScore = calculateScore(elapsedTimeInSeconds);

        // 4. Score 엔티티를 생성하고, 조회된 User 정보를 설정합니다.
        Score score = new Score();
        score.setUser(currentUser); // ⭐ 핵심: 현재 로그인 사용자를 설정
        score.setQuestionType(scoreRequest.getQuestionType());
        score.setQuestionId(scoreRequest.getQuestionId());
        score.setValue(calculatedScore);
        score.setElapsedTimeInSeconds(elapsedTimeInSeconds);
        scoreRepository.save(score);

        // 5. 사용이 끝난 세션 정보를 정리합니다.
        session.removeAttribute("questionStartTime");

        // 6. 결과를 DTO로 변환하여 반환합니다.
        return new ScoreDTO.Response(score.getId(), score.getValue(), score.getElapsedTimeInSeconds());
    }

    private int calculateScore(long elapsedTimeInSeconds) {
        if (elapsedTimeInSeconds <= 60) return 100;
        long timeOver = elapsedTimeInSeconds - 60;
        long penaltyCount = (timeOver + 29) / 30;
        int penaltyPoints = (int) (penaltyCount * 5);
        int score = 100 - penaltyPoints;
        return Math.max(20, score);
    }
}
