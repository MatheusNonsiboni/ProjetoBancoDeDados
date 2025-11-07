package com.example.SADE.Controller;

import com.example.SADE.Model.Escola;
import com.example.SADE.Model.Regiao;
import com.example.SADE.Repository.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/escolas")
public class EscolaController {

    @Autowired
    private EscolaRepository escolaRepository;

    @GetMapping
    public List<Escola> listar() {
        return escolaRepository.findAll();
    }

    @PostMapping
    public Escola cadastrar(@RequestBody Escola escola) {
    return Optional.ofNullable(escola)
            .map(escolaRepository::save)
            .orElseThrow(() -> new IllegalArgumentException("Região não pode ser nula."));
}
}
