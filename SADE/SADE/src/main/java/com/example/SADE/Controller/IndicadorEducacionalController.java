package com.example.SADE.Controller;

import com.example.SADE.Model.*;
import com.example.SADE.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/indicadores")
@CrossOrigin("*")
public class IndicadorEducacionalController {

    @Autowired
    private IndicadorEducacionalRepository repo;

    @Autowired
    private EscolaRepository escolaRepo;

    @GetMapping
    public List<IndicadorEducacional> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public IndicadorEducacional buscar(@PathVariable int id) {
        return repo.findById(id);
    }

    @PostMapping
    public IndicadorEducacional cadastrar(@RequestBody IndicadorEducacional i) {

        Escola e = escolaRepo.findById(i.getEscola().getId_escola());
        if (e == null) throw new RuntimeException("Escola não encontrada.");

        i.setEscola(e);
        return repo.save(i);
    }

    @GetMapping("/ranking/ideb")
    public List<IndicadorEducacional> rankingIdeb() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing(IndicadorEducacional::getIdeb).reversed())
                .collect(Collectors.toList());
    }
}
