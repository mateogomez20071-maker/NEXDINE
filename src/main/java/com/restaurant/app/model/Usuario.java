package com.restaurant.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String celular;
    private String correo;
    private String direccion;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean active = true;

    // Determina el rol según el tipo concreto
    public String getRol() {
        if (this instanceof Administrador) return "ADMINISTRADOR";
        if (this instanceof Cocinero)      return "COCINERO";
        if (this instanceof Mesero)        return "MESERO";
        if (this instanceof Empleado)      return "EMPLEADO";
        if (this instanceof Cliente)       return "CLIENTE";
        return "DESCONOCIDO";
    }

    public void iniciarSesion() {}
    public void cerrarSesion() {}
    public void editarPerfil() {}
    public void recibirNotificacion() {}
}
