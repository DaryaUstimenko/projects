package com.example.newsservice.service;

import com.example.newsservice.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEntityService<E, ID, R extends JpaRepository<E, ID>> implements EntityService<E, ID>  {

    protected final R repository;

    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public E findById(ID id) {
        log.info("Delete entity with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Entity with ID {0} not found!", id)
                ));
    }

    @Override
    public E save(E entity) {
        log.info("Save entity: {}", entity);

        return repository.save(entity);
    }

    @Override
    @Transactional
    public E update(ID id, E entity) {
        log.info("Update entity with ID: {}", id);

        var updatedEntity = updateFields(findById(id), entity);

        log.info("Updated entity: {}", updatedEntity);

        return repository.save(updatedEntity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    protected abstract E updateFields(E oldEntity, E newEntity);
}
