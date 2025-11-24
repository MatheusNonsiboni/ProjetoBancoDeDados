package com.example.SADE.Controller;

import com.example.SADE.Model.Escola;
import com.example.SADE.Model.Regiao;
import com.example.SADE.Repository.EscolaRepository;
import com.example.SADE.Repository.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escolas")
@CrossOrigin("*")
public class EscolaController {

    @Autowired
    private EscolaRepository repo;

    @Autowired
    private RegiaoRepository regiaoRepo;

    @GetMapping
    public List<Escola> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Escola buscar(@PathVariable int id) {
        return repo.findById(id);
    }

    @PostMapping
    public Escola cadastrar(@RequestBody Escola e) {

        if (e.getRegiao() == null || e.getRegiao().getId_regiao() == null)
            throw new RuntimeException("Região obrigatória.");

        Regiao r = regiaoRepo.findById(e.getRegiao().getId_regiao());
        if (r == null) throw new RuntimeException("Região não encontrada.");

        e.setRegiao(r);
        return repo.save(e);
    }
}