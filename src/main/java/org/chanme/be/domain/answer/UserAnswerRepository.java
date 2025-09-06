package org.chanme.be.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

}
