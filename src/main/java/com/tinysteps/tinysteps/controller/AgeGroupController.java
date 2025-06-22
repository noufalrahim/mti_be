package com.tinysteps.tinysteps.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tinysteps.tinysteps.model.AgeGroupModel;
import com.tinysteps.tinysteps.service.AgeGroupService;

@RestController
@RequestMapping("/api/agegroup")
public class AgeGroupController {

    @Autowired
    private AgeGroupService ageGroupService;

    @GetMapping("")
    public List<AgeGroupModel> getAllAgeGroups() {
        return ageGroupService.getAllAgeGroup();
    }

    @GetMapping("/{id}")
    public Optional<AgeGroupModel> getAgeGroupById(@PathVariable Long id) {
        return ageGroupService.getAgeGroupById(id);
    }

    @PostMapping("/add")
    public String addAgeGroup(@RequestBody AgeGroupModel ageGroup) {
        return ageGroupService.addAgeGroup(ageGroup);
    }

    @PutMapping("/update/{id}")
    public String updateAgeGroup(@PathVariable Long id, @RequestBody AgeGroupModel ageGroup) {
        return ageGroupService.updateAgeGroup(id, ageGroup);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAgeGroup(@PathVariable Long id) {
        return ageGroupService.deleteAgeGroup(id);
    }
}
