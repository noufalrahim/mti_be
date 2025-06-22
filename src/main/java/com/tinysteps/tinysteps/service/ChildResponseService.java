package com.tinysteps.tinysteps.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tinysteps.tinysteps.model.CategoryModel;
import com.tinysteps.tinysteps.model.ChildModel;
import com.tinysteps.tinysteps.model.ChildResponseModel;
import com.tinysteps.tinysteps.model.QuestionModel;
import com.tinysteps.tinysteps.repository.CategoryRepository;
import com.tinysteps.tinysteps.repository.ChildRepository;
import com.tinysteps.tinysteps.repository.ChildResponseRespository;
import com.tinysteps.tinysteps.repository.QuestionRepository;

@Service
public class ChildResponseService {

    private final ChildResponseRespository childResponseRepository;
    private final ChildRepository childRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    public ChildResponseService(ChildResponseRespository childResponseRepository, ChildRepository childRepository, QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.childResponseRepository = childResponseRepository;
        this.childRepository = childRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ChildResponseModel> getAllChildResponse() {
        return childResponseRepository.findAll();
    }

    public ResponseEntity<Map<String, Object>> addChildResponse(ChildResponseModel childResponseModel) {

        if (childResponseModel.getChild() == null || childResponseModel.getChild().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Child information is missing or invalid", "data", Collections.emptyMap()));
        }

        Optional<ChildModel> existingChild = childRepository.findById(childResponseModel.getChild().getId());

        if (existingChild.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Child not found", "data", Collections.emptyMap()));
        }

        Optional<QuestionModel> existingQuestion = questionRepository.findById(childResponseModel.getQuestion().getId());

        if (existingQuestion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Question not found", "data", Collections.emptyMap()));
        }

        Optional<ChildResponseModel> existingChildResponse = childResponseRepository.findByChildIdAndQuestionId(
                childResponseModel.getChild().getId(),
                childResponseModel.getQuestion().getId()
        );

        if (existingChildResponse.isPresent()) {

            ChildResponseModel updatedResponse = existingChildResponse.get();
            updatedResponse.setAnsweredYes(childResponseModel.getAnsweredYes());
            updatedResponse.setQuestionAnswered(childResponseModel.getQuestionAnswered());

            childResponseRepository.save(updatedResponse);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Child response updated successfully", "data", updatedResponse));
        }

        childResponseModel.setChild(existingChild.get());
        childResponseModel.setQuestion(existingQuestion.get());

        childResponseRepository.save(childResponseModel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Child response created successfully", "data", childResponseModel));
    }

    public ResponseEntity<Map<String, Object>> getChildProgress(Long childId) {
        List<CategoryModel> categories = categoryRepository.findAll();
        List<ChildResponseModel> childResponses = childResponseRepository.findByChildId(childId);
        List<Map<String, Object>> formattedData = new ArrayList<>();

        if (childResponses.isEmpty()) {
            System.out.println("No responses found for child ID: " + childId);
            for (CategoryModel category : categories) {
                List<ChildResponseModel> answeredYes = new ArrayList<>();
                List<ChildResponseModel> answeredNo = new ArrayList<>();
                List<ChildResponseModel> notAnswered = new ArrayList<>();

                List<QuestionModel> totalQuestions = questionRepository.findByCategoryId(category.getId());
                Map<String, Object> categoryData = Map.of(
                        "category", category.getName(),
                        "progress", Map.of(
                                "totalQuestions", totalQuestions.size(),
                                "answeredYes", answeredYes,
                                "answeredNo", answeredNo,
                                "notAnswered", notAnswered
                        )
                );
                formattedData.add(categoryData);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "No responses found for this child", "data", formattedData));
        }


        for (CategoryModel category : categories) {
            List<ChildResponseModel> answeredYes = new ArrayList<>();
            List<ChildResponseModel> answeredNo = new ArrayList<>();
            List<ChildResponseModel> notAnswered = new ArrayList<>();

            List<QuestionModel> totalQuestions = questionRepository.findByCategoryId(category.getId());

            for (ChildResponseModel response : childResponses) {
                if (response.getQuestion().getCategory().getId().equals(category.getId())) {
                    if (response.getQuestionAnswered()) {
                        if (response.getAnsweredYes()) {
                            answeredYes.add(response);
                        } else {
                            answeredNo.add(response);
                        }
                    } else {
                        notAnswered.add(response);
                    }
                }
            }

            Map<String, Object> categoryData = Map.of(
                    "category", category.getName(),
                    "progress", Map.of(
                            "totalQuestions", totalQuestions.size(),
                            "answeredYes", answeredYes,
                            "answeredNo", answeredNo,
                            "notAnswered", notAnswered
                    )
            );

            formattedData.add(categoryData);
        }

        return ResponseEntity.ok(Map.of("message", "Child progress retrieved successfully", "data", formattedData));
    }

    public ResponseEntity<Map<String, Object>> updateChildResponse(Long id, ChildResponseModel updatedModel) {
        Optional<ChildResponseModel> responseOpt = childResponseRepository.findById(id);
        if (responseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Child response not found", "data", Collections.emptyMap()));
        }
    
        ChildResponseModel existing = responseOpt.get();
        if (updatedModel.getAnsweredYes() != null)
            existing.setAnsweredYes(updatedModel.getAnsweredYes());
        if (updatedModel.getQuestionAnswered() != null)
            existing.setQuestionAnswered(updatedModel.getQuestionAnswered());
    
        childResponseRepository.save(existing);
    
        return ResponseEntity.ok(Map.of("message", "Child response updated successfully", "data", existing));
    }
    
    public ResponseEntity<Map<String, Object>> deleteChildResponse(Long id) {
        Optional<ChildResponseModel> responseOpt = childResponseRepository.findById(id);
        if (responseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Child response not found", "data", Collections.emptyMap()));
        }
    
        childResponseRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Child response deleted successfully", "data", Collections.emptyMap()));
    }
    

}
