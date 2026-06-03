package com.restaurant.app.controller;

import com.restaurant.app.services.UserService;
import com.restaurant.app.services.UserService.SuspendedMarker;
import com.restaurant.app.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/login"})
    public String loginPage(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        userService.initAdmin();

        Optional<Usuario> userOpt = userService.login(username, password);

        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (user instanceof SuspendedMarker) {
                model.addAttribute("error", "Usuario suspendido");
                return "login";
            }
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
