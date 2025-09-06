package org.chanme.be.api.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDTO {
    private String question;
    private List<HashMap<String, String>> answer;
}
