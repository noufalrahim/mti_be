package com.tinysteps.tinysteps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinysteps.tinysteps.model.ChildResponseModel;
import com.tinysteps.tinysteps.service.ChildResponseService;


@RestController
@RequestMapping("/api/childresponses")
public class ChildResponseController {

    @Autowired
    private ChildResponseService childresponseService;

    @GetMapping("")
    public List<ChildResponseModel> getAllChildResponse() {
        return childresponseService.getAllChildResponse();
    }

    @PostMapping("/add")
    public String addChildResponse(@RequestBody ChildResponseModel childresponseModel) {
        return childresponseService.addChildResponse(childresponseModel);
    }
}
