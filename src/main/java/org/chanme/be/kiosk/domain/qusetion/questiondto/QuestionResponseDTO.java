package org.chanme.be.kiosk.domain.qusetion.questiondto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.chanme.be.kiosk.domain.qusetion.Question;

@Data
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String description;

    public static QuestionResponseDTO from(Question question) {
        return new QuestionResponseDTO(question.getId(), question.getDescription());
    }
}