package com.techvedu.backend.service;

import com.techvedu.backend.entity.TestEntity;
import com.techvedu.backend.repository.TestEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEntityService {

    private final TestEntityRepository repository;

    public TestEntityService(TestEntityRepository repository) {
        this.repository = repository;
    }

    public List<TestEntity> getAll() {
        return repository.findAll();
    }

    public TestEntity save(TestEntity entity) {
        return repository.save(entity);
    }
}