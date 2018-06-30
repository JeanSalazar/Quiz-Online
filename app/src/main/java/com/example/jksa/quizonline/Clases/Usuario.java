package com.example.jksa.quizonline.Clases;

public class Usuario {

    private String nombre, correo, apellidos, fecha_nac, rol, genero, estado;

    public Usuario() {

    }


    public Usuario(String nombre, String correo, String apellidos, String fecha_nac, String rol, String genero, String estado) {
        this.nombre = nombre;
        this.correo = correo;
        this.apellidos = apellidos;
        this.fecha_nac = fecha_nac;
        this.rol = rol;
        this.genero = genero;
        this.estado = estado;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}