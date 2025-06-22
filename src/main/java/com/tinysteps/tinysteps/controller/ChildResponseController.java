package com.tinysteps.tinysteps.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinysteps.tinysteps.model.ChildResponseModel;
import com.tinysteps.tinysteps.service.ChildResponseService;

@RestController
@RequestMapping("/api/childresponses")
public class ChildResponseController {

    @Autowired
    private ChildResponseService childResponseService;

    @GetMapping("")
    public List<ChildResponseModel> getAllChildResponses() {
        return childResponseService.getAllChildResponse();
    }

    @GetMapping("/{childId}")
    public ResponseEntity<Map<String, Object>> getChildProgress(@PathVariable Long childId) {
        return childResponseService.getChildProgress(childId);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addChildResponse(@RequestBody ChildResponseModel childResponseModel) {
        return childResponseService.addChildResponse(childResponseModel);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateChildResponse(@PathVariable Long id,
                                                                   @RequestBody ChildResponseModel childResponseModel) {
        return childResponseService.updateChildResponse(id, childResponseModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteChildResponse(@PathVariable Long id) {
        return childResponseService.deleteChildResponse(id);
    }
}
