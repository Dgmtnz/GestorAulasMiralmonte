package com.joange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestorAulasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestorAulasApplication.class, args);
        System.out.println("Servidor de Gestor de Aulas escuchando en puerto 8080");
    }
} 