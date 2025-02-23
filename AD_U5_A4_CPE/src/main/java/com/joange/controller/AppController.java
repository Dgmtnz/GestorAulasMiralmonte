package com.joange.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class AppController {
	
	@Value("${aplicacion.nombre}")
	private String titulo;
	
	// Remove the "/" mapping since it's handled by HomeController
	// Keep this class for other potential application-wide configurations
}
