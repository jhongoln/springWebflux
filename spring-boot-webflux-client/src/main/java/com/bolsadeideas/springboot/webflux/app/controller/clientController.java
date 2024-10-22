package com.bolsadeideas.springboot.webflux.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class clientController {
	
	
	public ResponseEntity<String> clientPrueba(){
		return ResponseEntity.ok("prueba de cliente con eureka");
	}

}
