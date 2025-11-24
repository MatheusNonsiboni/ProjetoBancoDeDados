package com.example.SADE.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final String CODIGO_SECRETARIA = "1234";

    @PostMapping("/secretaria")
    public boolean loginSecretaria(@RequestBody String codigo) {
        return CODIGO_SECRETARIA.equals(codigo);
    }
}