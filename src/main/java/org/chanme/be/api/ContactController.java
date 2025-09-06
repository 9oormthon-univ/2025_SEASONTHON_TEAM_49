package org.chanme.be.api;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.ContactDTO;
import org.chanme.be.api.dto.QuestionDTO;
import org.chanme.be.service.ContactService;
import org.chanme.be.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final QuestionService questionService;

    @GetMapping("/question")
    public ResponseEntity<QuestionDTO> getQuestion() {
        QuestionDTO question = questionService.EntityToDTO(questionService.selectQuestion(2));
        ResponseEntity<QuestionDTO> responseEntity =
                new ResponseEntity<QuestionDTO>(question, HttpStatus.OK);
        return responseEntity;
    }

    // add(내용 일치) -> update(대상 일치) -> delete(대상 일치)
    @PostMapping("/add")
    public ResponseEntity<?> addContact(@RequestBody ContactDTO contact) {
        ContactDTO inserted = contactService.insertContact(contact);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public String updateContact(@RequestBody ContactDTO contact) {
        ContactDTO updated = contactService.updateContact(contact);
        return updated.toString();
    }

    @DeleteMapping("/delete")
    public String deleteContact(@RequestBody ContactDTO contact) {
        contactService.deleteContact(contact);
        return "deleted";
    }
}
