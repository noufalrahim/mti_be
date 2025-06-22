package com.tinysteps.tinysteps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinysteps.tinysteps.model.AgeGroupModel;
import com.tinysteps.tinysteps.repository.AgeGroupRepository;

@Service
public class AgeGroupService {

    @Autowired
    private AgeGroupRepository ageGroupRepository;

    public List<AgeGroupModel> getAllAgeGroup() {
        return ageGroupRepository.findAll();
    }

    public Optional<AgeGroupModel> getAgeGroupById(Long id) {
        return ageGroupRepository.findById(id);
    }

    public String addAgeGroup(AgeGroupModel ageGroup) {
        if (ageGroupRepository.existsByStartAgeLessThanAndEndAgeGreaterThan(
                ageGroup.getEndAge(), ageGroup.getStartAge())) {
            return "Age group already exists!";
        }

        ageGroupRepository.save(ageGroup);
        return "Age group added successfully!";
    }

    public String updateAgeGroup(Long id, AgeGroupModel updatedGroup) {
        Optional<AgeGroupModel> existingGroup = ageGroupRepository.findById(id);
        if (existingGroup.isPresent()) {
            AgeGroupModel ageGroup = existingGroup.get();
            ageGroup.setStartAge(updatedGroup.getStartAge());
            ageGroup.setEndAge(updatedGroup.getEndAge());
            ageGroupRepository.save(ageGroup);
            return "Age group updated successfully!";
        } else {
            return "Age group not found!";
        }
    }

    public String deleteAgeGroup(Long id) {
        if (ageGroupRepository.existsById(id)) {
            ageGroupRepository.deleteById(id);
            return "Age group deleted successfully!";
        } else {
            return "Age group not found!";
        }
    }
}
