package org.chanme.be.api;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.chanme.be.api.dto.MessageDTO;
import org.chanme.be.api.dto.QuestionDTO;
import org.chanme.be.domain.message.Message;
import org.chanme.be.service.MessageService;
import org.chanme.be.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final QuestionService questionService;

    @GetMapping("/question")
    public ResponseEntity<QuestionDTO> getQuestion() {
        QuestionDTO question = questionService.EntityToDTO(questionService.selectQuestion(3));
        ResponseEntity<QuestionDTO> responseEntity =
                new ResponseEntity<QuestionDTO>(question, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/receive/{id}")
    public ResponseEntity<MessageDTO> getReceive(@RequestParam Long questionId) {
        MessageDTO messageDTO = messageService.messageReceive(questionId);
        ResponseEntity<MessageDTO> response
                = new ResponseEntity<>(messageDTO, HttpStatus.OK);
        return response;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> send(MessageDTO messageDTO) {
        ResponseEntity<MessageDTO> response =
                new ResponseEntity<>(messageService.messageSend(messageDTO), HttpStatus.OK);
        return response;
    }
}

