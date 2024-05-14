package com.example.contacts.service;

import com.example.contacts.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactsService {
    List<Contact> findAll();

    Optional<Contact> findById(Long id);

    Contact save(Contact contact);

    Contact update(Contact contact);

    void deleteById(Long id);

}
