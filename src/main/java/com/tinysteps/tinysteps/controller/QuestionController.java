package com.tinysteps.tinysteps.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinysteps.tinysteps.model.ChildResponseModel;
import com.tinysteps.tinysteps.model.QuestionModel;
import com.tinysteps.tinysteps.repository.AgeGroupRepository;
import com.tinysteps.tinysteps.repository.CategoryRepository;
import com.tinysteps.tinysteps.repository.QuestionRepository;
import com.tinysteps.tinysteps.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/questions")
@Tag(name = "Question API", description = "Endpoints for managing questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Operation(summary = "Get all questions")
    @GetMapping("")
    public ResponseEntity<List<QuestionModel>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Create a new question")
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(
            @RequestBody(
                    description = "JSON request body to add a question",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                                "questionEnglish": "How do plants make their food?",
                                "questionMalayalam": "സസ്യങ്ങൾ അവരുടെ ആഹാരം എങ്ങനെ നിർമ്മിക്കുന്നു?",
                                "severity": 3,
                                "category": { "id": 2 },
                                "ageGroup": { "id": 1 }
                            }
                            """)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody QuestionModel question) {
        return questionService.addQuestion(question);
    }

    @Operation(summary = "Delete question by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable Long id) {
        return questionService.deleteQuestion(id);
    }

    @Operation(summary = "Get questions by category ID")
    @GetMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> getQuestionByCategory(@PathVariable Long id) {
        return questionService.getQuestionByCategory(id);
    }

    @Operation(summary = "Get question by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateQuestion(@PathVariable Long id,
    @RequestBody(
        description = "JSON request body to add a question",
        required = true,
        content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                {
                    "questionEnglish": "How do plants make their food?",
                    "questionMalayalam": "സസ്യങ്ങൾ അവരുടെ ആഹാരം എങ്ങനെ നിർമ്മിക്കുന്നു?",
                    "severity": 3,
                    "category": { "id": 2 },
                    "ageGroup": { "id": 1 }
                }
                """)
        )
)
@org.springframework.web.bind.annotation.RequestBody QuestionModel question) {
        log.info("Update Question Body: {}", id);
        return questionService.updateQuestion(id, question);
    }

}
