package com.example.SADE.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secretaria")
@CrossOrigin(origins = "*")
public class SecretariaController {

    private static final String SECRETARIA_CODIGO = "SECRETA123"; 

    @PostMapping("/login")
    public boolean login(@RequestParam String codigo) {
        return SECRETARIA_CODIGO.equals(codigo);
    }
}

