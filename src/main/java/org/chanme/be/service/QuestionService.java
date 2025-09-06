package org.chanme.be.service;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.QuestionDTO;
import org.chanme.be.domain.question.Question;
import org.chanme.be.domain.question.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionDTO EntityToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO().builder()
                .question(question.getQuestion())
                .answer(question.getAnswers())
                .build();
        return questionDTO;
    }

    public Question selectQuestion(int kindId) {
        List<Question> questions = questionRepository.findByKindId(kindId);
        int random = new Random().nextInt(questions.size());
        return questions.get(random);
    }
}
