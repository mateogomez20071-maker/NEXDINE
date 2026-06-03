package com.restaurant.app.controller;

import com.restaurant.app.model.Usuario;
import com.restaurant.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        List<Usuario> users = userService.getAllUsers();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", users);
        return "dashboard";
    }

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if ("CLIENTE".equals(currentUser.getRol())) return "redirect:/dashboard";

        List<Usuario> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "dashboard";
    }

    @GetMapping("/users/search")
    public String searchUser(@RequestParam @NonNull Long id,
                            HttpSession session,
                            Model model) {

        Usuario currentUser = (Usuario) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        userService.getUserById(id).ifPresent(user -> {
            model.addAttribute("searchedUser", user);
        });

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());

        return "dashboard";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String role,
                             HttpSession session) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if (!"ADMINISTRADOR".equals(currentUser.getRol())) return "redirect:/dashboard";
        if ("ADMINISTRADOR".equals(role)) return "redirect:/users";

        userService.createUser(username, password, role);
        return "redirect:/users";
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam @NonNull Long id,
                           @RequestParam String username,
                           HttpSession session) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if (!"ADMINISTRADOR".equals(currentUser.getRol())) return "redirect:/dashboard";

        userService.updateUsername(id, username);
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam @NonNull Long id, HttpSession session) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if (!"ADMINISTRADOR".equals(currentUser.getRol())) return "redirect:/dashboard";

        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/users/toggle")
    public String toggleStatus(@RequestParam @NonNull Long id, HttpSession session) {
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if (!"ADMINISTRADOR".equals(currentUser.getRol())) return "redirect:/dashboard";

        userService.toggleUserStatus(id);
        return "redirect:/users";
    }
}
