package com.example.SADE.Controller;

import com.example.SADE.Model.Regiao;
import com.example.SADE.Repository.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regioes")
@CrossOrigin("*")
public class RegiaoController {

    @Autowired
    private RegiaoRepository repo;

    @GetMapping
    public List<Regiao> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Regiao buscar(@PathVariable int id) {
        return repo.findById(id);
    }

    @PostMapping
    public Regiao cadastrar(@RequestBody Regiao r) {
        return repo.save(r);
    }
}