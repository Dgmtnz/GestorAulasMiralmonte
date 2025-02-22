package com.joange.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	
	@Value("${aplicacion.nombre}")
	private String titulo;
	
	@GetMapping("/")
	public String inicio() {
		return "redirect:/login";
	}
}
