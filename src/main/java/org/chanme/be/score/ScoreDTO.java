package org.chanme.be.score;

import lombok.Data;
import lombok.Getter;

public class ScoreDTO {
    // 프론트엔드에서 백엔드로 보낼 요청 DTO
    @Data
    public static class Request {
        private Long questionId;
        private QuestionType questionType;
    }

    // 백엔드에서 프론트엔드로 보낼 응답 DTO
    @Getter
    public static class Response {
        private Long scoreId;
        private int score;
        private long elapsedTimeInSeconds;

        public Response(Long scoreId, int score, long elapsedTimeInSeconds) {
            this.scoreId = scoreId;
            this.score = score;
            this.elapsedTimeInSeconds = elapsedTimeInSeconds;
        }
    }
}
