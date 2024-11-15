package com.example.booking.web.controller.v2;

import com.example.booking.aop.AuthorizeAction;
import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;
import com.example.booking.exception.AlreadyExistsException;
import com.example.booking.mapper.UserMapper;
import com.example.booking.security.AppUserPrincipal;
import com.example.booking.service.UserService;
import com.example.booking.service.KafkaEventService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerV2 {

    private final UserService userService;

    private final UserMapper userMapper;

    private final KafkaEventService kafkaEventService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() != null) {
            AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
            User user = userService.findById(principal.getId());
            model.addAttribute("user", user);
            return "user/profile";
        } else {
            return "redirect:/api/v1/user/create";
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllUsersPage(@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                  Model model) {
        PaginationRequest paginationRequest = new PaginationRequest(pageSize, pageNumber);
        model.addAttribute("paginationRequest", paginationRequest);

        Page<User> users = userService.findAll(paginationRequest.pageRequest());

        model.addAttribute("users", users);

        return "user/all_users";
    }


    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String getById(@PathVariable UUID id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping(value = "/name/{username}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String getUserByName(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UpsertUserRequest());
        return "user/create_user";
    }

    @PostMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    public String createUser(@ModelAttribute UpsertUserRequest user, BindingResult result, Model model,
                             @RequestParam String role) {
        if (userService.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(MessageFormat.format(
                    "User with email {0} already exists!", user.getEmail()));
        }
        if (userService.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException(MessageFormat.format(
                    "User with name {0} already exists!", user.getUsername()));
        }
        User newUser = userMapper.upsertRequestToUser(user);
        RoleType roleType = RoleType.valueOf(role);
        newUser.addRole(roleType);
        if (result.hasErrors()) {
            model.addAttribute("user", newUser);
            return "user/create_user";
        }
        User savedUser = userService.save(newUser);
        kafkaEventService.registrationEvent(savedUser.getId());
        return "redirect:/api/v1/user/success";
    }

    @GetMapping("/success")
    public String showSuccessMessage() {
        return "user/success";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            showForm.set("user/user_update");
        } else {
            showForm.set("redirect:/api/v1/user/create");
        }
        return showForm.get();
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "update")
    public String updateUser(@PathVariable UUID id, @RequestBody @ModelAttribute UpsertUserRequest user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/user_update";
        }
        User updatedUser = userService.update(id, userMapper.upsertRequestToUser(user));
        model.addAttribute("user", updatedUser);
        return "user/profile";
    }

    @GetMapping(value = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "delete")
    public String deleteById(@PathVariable UUID id) {
        userService.deleteById(id);

        return "redirect:/logout";
    }
}
