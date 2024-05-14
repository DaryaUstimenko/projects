package com.example.contacts.repository;

import com.example.contacts.model.Contact;
import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataBaseContactsRepository implements ContactsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Contact> findAll() {
        String sql = "SELECT * FROM contacts";
        log.debug("In DataBase: FindAll contacts");
        return jdbcTemplate.query(sql, new ContactMapper());
    }

    @Override
    public Optional<Contact> findById(Long id) {
        String sql = "SELECT * FROM contacts WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(jdbcTemplate.query(sql,
                new ArgumentPreparedStatementSetter(new Object[]{id}),
                new RowMapperResultSetExtractor<>(new ContactMapper(), 1)));
        //Добавление условной проверки, если контакт равен нулю
        if (contact == null) {
            log.info("In DataBase: Contact with ID: {} not found", id);
            throw new ContactNotFoundException("Contact with ID: " + id + "not found");
        } else {
            log.info("In DataBase: Contact with ID: {} was found", id);
        }
        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            Long id = currentId.getAndIncrement();
            log.info("id" + id);
            contact.setId(id);
            log.info("next id" + id);
            String sql = "INSERT INTO contacts (firstName,lastName,email,phone,id) VALUES (?,?,?,?,?)";
            jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(),
                    contact.getEmail(), contact.getPhone(), contact.getId());
            log.debug("In DataBase: Contact with ID: {} was saved", contact.getId());
            return contact;
        } else {
            return update(contact);
        }

    }

    @Override
    public Contact update(Contact contact) {
        Contact existedContact = findById(contact.getId()).orElse(null);
        if (existedContact != null) {
            String sql = "UPDATE contacts SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE id = ?";
            jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(),
                    contact.getPhone(), contact.getId());
            log.debug("In DataBase: Contact: {} was updated", contact);
        }
        return contact;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        jdbcTemplate.update(sql, id);
        log.debug("In DataBase: Contact with ID: {} was deleted", id);
    }
}