package com.example.contacts.service;

import com.example.contacts.model.Contact;
import com.example.contacts.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository repository;

    @Override
    public List<Contact> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Contact save(Contact contact) {
        return repository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        return repository.update(contact);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
