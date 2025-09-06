package org.chanme.be.api;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.GallaryDTO;
import org.chanme.be.api.dto.QuestionDTO;
import org.chanme.be.service.GallaryService;
import org.chanme.be.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallary")
@RequiredArgsConstructor
public class GallaryController {
    private final GallaryService gallaryService;
    private final QuestionService questionService;

    @GetMapping("/question")
    public ResponseEntity<QuestionDTO> getQuestion() {
        QuestionDTO question = questionService.EntityToDTO(questionService.selectQuestion(1));
        ResponseEntity<QuestionDTO> responseEntity =
                new ResponseEntity<QuestionDTO>(question, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/view")
    public ResponseEntity<List<GallaryDTO>> getGallary() {
        List<GallaryDTO> gallaries = gallaryService.getGallaries();
        return ResponseEntity.ok(gallaries);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> delete(@RequestParam Long id) {
        gallaryService.deletePhoto(id);
        ResponseEntity<String> response =
                new ResponseEntity<>(HttpStatus.OK);
        return response;
    }

    @PostMapping("/recover/{id}")
    public ResponseEntity<GallaryDTO> recover(@RequestParam Long id) {
        ResponseEntity<GallaryDTO> recovered =
                new ResponseEntity<>(gallaryService.recoverPhoto(id), HttpStatus.OK);
        return recovered;
    }
}
