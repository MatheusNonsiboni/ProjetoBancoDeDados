package com.example.SADE.Controller;

import com.example.SADE.Model.Disciplina;
import com.example.SADE.Repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@CrossOrigin("*")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repo;

    @GetMapping
    public List<Disciplina> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Disciplina buscar(@PathVariable int id) {
        return repo.findById(id);
    }

    @PostMapping
    public Disciplina cadastrar(@RequestBody Disciplina d) {
        return repo.save(d);
    }
}
