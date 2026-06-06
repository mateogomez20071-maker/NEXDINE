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
            admin.setRol("ADMINISTRADOR");
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

    public Usuario createUser(String username, String password, String rol, String celular, String direccion) {
        Usuario newUser = new Cliente();

        if (!celular.matches("[0-9]+")) 
            {
                throw new IllegalArgumentException
                (
                    "El teléfono solo puede contener números"
                );
            }
            
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRol(rol);
        newUser.setCelular(celular);
        newUser.setDireccion(direccion);
        newUser.setActive(true);
        return userRepository.save(newUser);
    }

    public void updateUser(@NonNull Long id,
                       String username,
                       String role,
                       String celular,
                       String direccion) 
    {

        userRepository.findById(id).ifPresent(user -> {

        user.setUsername(username);
        user.setRol(role);
        user.setCelular(celular);
        user.setDireccion(direccion);

        userRepository.save(user);
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
