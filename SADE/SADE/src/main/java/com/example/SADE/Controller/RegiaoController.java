package com.example.SADE.Controller;

import com.example.SADE.Model.Regiao;
import com.example.SADE.Repository.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/regioes")
public class RegiaoController {

    @Autowired
    private RegiaoRepository regiaoRepository;

    @GetMapping
    public List<Regiao> listar() {
        return regiaoRepository.findAll();
    }

    @PostMapping
    public Regiao cadastrar(@RequestBody Regiao regiao) {
    return Optional.ofNullable(regiao)
            .map(regiaoRepository::save)
            .orElseThrow(() -> new IllegalArgumentException("Região não pode ser nula."));
}

}
