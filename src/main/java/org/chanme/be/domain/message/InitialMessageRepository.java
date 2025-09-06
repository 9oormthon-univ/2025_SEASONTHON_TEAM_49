package org.chanme.be.domain.message;

import org.chanme.be.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitialMessageRepository extends JpaRepository<InitialMessage, Long> {
    public InitialMessage findByQuestion(Question question);
}
