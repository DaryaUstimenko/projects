package com.example.contacts.controller;

import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.model.Contact;
import com.example.contacts.service.ContactsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequiredArgsConstructor
public class ContactController {
    private final ContactsService contactsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contacts", contactsService.findAll());
        return "index";
    }

    @GetMapping("/contact/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "create_edit";
    }

    //Добавление валидации данных контакта
    @PostMapping("/contact/create")
    public String createContact(@Valid @ModelAttribute Contact contact, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("contact", contact);
            return "create_edit";
        }
        contactsService.save(contact);
        return "redirect:/";
    }

    //Использование Optional.ifPresentOrElse (контакт найден или не найден)
    @GetMapping("/contact/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        contactsService.findById(id).ifPresentOrElse(
                contact -> {
                    model.addAttribute("contact", contact);
                    showForm.set("create_edit");
                },
                () -> showForm.set("redirect:/")
        );
        return showForm.get();
    }

    @PostMapping("/contact/edit")
    public String editContact(@Valid @ModelAttribute Contact contact, BindingResult result) {
        if (result.hasErrors()) {
            return "create_edit";
        }
        contactsService.update(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactsService.deleteById(id);
        return "redirect:/";
    }

    //Обработка исключений: использование метода @ExceptionHandler
    //Возвращение состояния HttpStatus.NOT_FOUND
    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(ContactNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    }
}
