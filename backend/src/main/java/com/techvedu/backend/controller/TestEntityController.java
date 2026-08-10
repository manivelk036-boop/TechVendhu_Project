package com.techvedu.backend.controller;

import com.techvedu.backend.entity.TestEntity;
import com.techvedu.backend.service.TestEntityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-entities")
public class TestEntityController {

    private final TestEntityService service;

    public TestEntityController(TestEntityService service) {
        this.service = service;
    }

    @GetMapping
    public List<TestEntity> getAll() {
        return service.getAll();
    }

    @PostMapping
    public TestEntity create(@RequestBody TestEntity entity) {
        return service.save(entity);
    }
}