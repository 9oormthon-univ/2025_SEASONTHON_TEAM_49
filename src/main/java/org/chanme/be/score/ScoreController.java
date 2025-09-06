package org.chanme.be.score;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * 문제 풀이 타이머를 시작합니다.
     * 프론트엔드에서는 사용자가 '시작' 버튼을 누르는 순간 이 API를 호출해야 합니다.
     * 서버는 이 요청을 받은 시간을 세션에 'questionStartTime'으로 기록합니다.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startTimer(HttpServletRequest request) {
        // 사용자 개인의 세션을 가져오거나 새로 생성합니다.
        HttpSession session = request.getSession(true);
        // 현재 시간을 'questionStartTime'으로 세션에 저장합니다.
        session.setAttribute("questionStartTime", System.currentTimeMillis());

        return ResponseEntity.ok("Timer has started successfully.");
    }


    /**
     * 문제 풀이 타이머를 종료합니다.
     * 프론트엔드에서는 사용자가 '완료' 버튼을 누르는 순간 이 API를 호출해야 합니다.
     * 서버는 이 요청을 받은 시간을 세션에 'questionEndTime'으로 기록합니다.
     */
    @PostMapping("/end")
    public ResponseEntity<String> endTimer(HttpServletRequest request) {
        // 사용자 개인의 세션을 가져오거나 새로 생성합니다.
        HttpSession session = request.getSession(true);
        // 현재 시간을 'questionStartTime'으로 세션에 저장합니다.
        session.setAttribute("questionEndTime", System.currentTimeMillis());

        return ResponseEntity.ok("Timer has en successfully.");
    }



    /**
     * 정답을 맞힌 후, 점수를 계산하고 DB에 기록하고 결과를 반환한다.
     */
    @PostMapping
    public ResponseEntity<ScoreDTO.Response> recordScore(
            @RequestBody ScoreDTO.Request scoreRequest,
            HttpServletRequest request
    ) {
        ScoreDTO.Response response = scoreService.calculateAndSaveScore(scoreRequest, request);
        return ResponseEntity.ok(response);
    }
}
