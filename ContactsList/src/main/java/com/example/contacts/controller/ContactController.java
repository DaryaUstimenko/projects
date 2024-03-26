package com.example.contacts.controller;

import com.example.contacts.model.Contact;
import com.example.contacts.service.ContactsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("task", new Contact());
        return "create_edit";
    }

    @PostMapping("/contact/create")
    public String createContact(@ModelAttribute Contact contact) {
        contactsService.save(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/edit/{id}") ///???
    public String showEditForm(@PathVariable Long id, Model model) {
        Contact contact = contactsService.findById(id);
        if (contact != null) {
            model.addAttribute("contact", contact);
            return "create_edit";
        }
        return "redirect:/";
    }

    @PostMapping("/contact/edit")
    public String editContact(@ModelAttribute Contact contact) {
        contactsService.update(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactsService.deleteById(id);
        return "redirect:/";
    }
}
