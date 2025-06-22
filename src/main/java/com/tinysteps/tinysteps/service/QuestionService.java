package com.tinysteps.tinysteps.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tinysteps.tinysteps.model.AgeGroupModel;
import com.tinysteps.tinysteps.model.CategoryModel;
import com.tinysteps.tinysteps.model.QuestionModel;
import com.tinysteps.tinysteps.repository.AgeGroupRepository;
import com.tinysteps.tinysteps.repository.CategoryRepository;
import com.tinysteps.tinysteps.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final AgeGroupRepository ageGroupRepository;
    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);
    public QuestionService(QuestionRepository questionRepository,
                           CategoryRepository categoryRepository,
                           AgeGroupRepository ageGroupRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.ageGroupRepository = ageGroupRepository;
    }

    public ResponseEntity<String> addQuestion(QuestionModel question) {
        boolean questionExist = questionRepository.findByQuestionEnglish(question.getQuestionEnglish()).isPresent();
        if (questionExist) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Question already exists");
        }

        questionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully!");
    }

    public ResponseEntity<List<QuestionModel>> getAllQuestions() {
        List<QuestionModel> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

    public ResponseEntity<Map<String, Object>> deleteQuestion(Long id) {
        Optional<QuestionModel> existingQuestion = questionRepository.findById(id);
        if (existingQuestion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No question found", "data", Collections.emptyMap()));
        }

        questionRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Question deleted successfully", "data", Collections.emptyMap()));
    }

    public ResponseEntity<Map<String, Object>> getQuestionByCategory(Long categoryId) {
        Optional<CategoryModel> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No category found", "data", Collections.emptyMap()));
        }

        List<QuestionModel> questions = questionRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(Map.of("message", "Questions fetched successfully", "data", questions));
    }

    public ResponseEntity<Map<String, Object>> getQuestionById(Long id) {
        Optional<QuestionModel> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Question not found", "data", Collections.emptyMap()));
        }

        return ResponseEntity.ok(Map.of("message", "Question fetched", "data", question.get()));
    }

    public ResponseEntity<Map<String, Object>> updateQuestion(Long id, QuestionModel questionModel) {

        Optional<QuestionModel> responseOpt = questionRepository.findById(id);
        if (responseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Question not found", "data", Collections.emptyMap()));
        }

        QuestionModel existing = responseOpt.get();

        if (questionModel.getQuestionEnglish() != null) {
            existing.setQuestionEnglish(questionModel.getQuestionEnglish());
        }
        if (questionModel.getQuestionMalayalam() != null) {
            existing.setQuestionMalayalam(questionModel.getQuestionMalayalam());
        }
        if (questionModel.getSeverity() != null) {
            existing.setSeverity(questionModel.getSeverity());
        }

        if (questionModel.getCategory() != null && questionModel.getCategory().getId() != null &&
            questionModel.getAgeGroup() != null && questionModel.getAgeGroup().getId() != null) {

            Optional<CategoryModel> categoryResp = categoryRepository.findById(questionModel.getCategory().getId());
            Optional<AgeGroupModel> ageGroupResp = ageGroupRepository.findById(questionModel.getAgeGroup().getId());

            if (categoryResp.isEmpty() || ageGroupResp.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid category or age group ID", "data", Collections.emptyMap()));
            }

            existing.setCategory(categoryResp.get());
            existing.setAgeGroup(ageGroupResp.get());
        }

        questionRepository.save(existing);

        return ResponseEntity.ok(Map.of("message", "Question updated successfully", "data", existing));
    }
}
