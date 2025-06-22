package com.tinysteps.tinysteps.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinysteps.tinysteps.model.ChildModel;
import com.tinysteps.tinysteps.service.ChildService;

@RestController
@RequestMapping("/api/child")
public class ChildController {

    @Autowired
    private ChildService childService;

    @GetMapping("")
    public ResponseEntity<List<ChildModel>> getAllChildren() {
        return childService.getAllChild();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getChildrenByUser(@PathVariable Long id) {
        return childService.getAllChildrenOfUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getChildById(@PathVariable Long id) {
        return childService.getChildById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addChild(@RequestBody ChildModel child) {
        return childService.addChild(child);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateChild(@PathVariable Long id, @RequestBody ChildModel child) {
        return childService.updateChild(id, child);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteChild(@PathVariable Long id) {
        return childService.deleteChild(id);
    }
}
