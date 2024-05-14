package com.example.newsservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EntityService<E, ID> {

    Page<E> findAll(Pageable pageable);

    E findById(ID id);

    E save(E entity);

    E update(ID id, E entity);

    void deleteById(ID id);

}
