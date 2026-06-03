package com.restaurant.app.services;

import com.restaurant.app.model.*;
import com.restaurant.app.repository.UserRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initAdmin() {
        if (!userRepository.findByUsername("jesus").isPresent()) {
            Administrador admin = new Administrador();
            admin.setUsername("jesus");
            admin.setPassword("1234");
            admin.setActive(true);
            admin.setNombre("Administrador");
            userRepository.save(admin);
        }
    }

    public Optional<Usuario> login(String username, String password) {
        Optional<Usuario> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Usuario u = userOpt.get();
            if (u.getPassword().equals(password)) {
                if (!u.isActive()) {
                    return Optional.of(new SuspendedMarker());
                }
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    public Usuario createUser(String username, String password, String rol) {
        Usuario newUser;
        switch (rol) {
            case "CLIENTE":    newUser = new Cliente();    break;
            case "COCINERO":   newUser = new Cocinero();   break;
            case "MESERO":     newUser = new Mesero();     break;
            default:           throw new IllegalArgumentException("Rol no válido");
        }
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setActive(true);
        return userRepository.save(newUser);
    }

    public void updateUsername(@NonNull Long id, String newUsername) {
        userRepository.findById(id).ifPresent(u -> {
            u.setUsername(newUsername);
            userRepository.save(u);
        });
    }

    public void deleteUser(@NonNull Long id) {
        userRepository.deleteById(id);
    }

    public void toggleUserStatus(@NonNull Long id) {
        userRepository.findById(id).ifPresent(u -> {
            u.setActive(!u.isActive());
            userRepository.save(u);
        });
    }

    // Marcador para usuario suspendido
    public static class SuspendedMarker extends Cliente {
        public SuspendedMarker() { super(); }
    }

    public Optional<Usuario> getUserById(@NonNull Long id) 
    {
        return userRepository.findById(id);
    }
}
