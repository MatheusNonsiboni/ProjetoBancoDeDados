package com.example.SADE.Controller;

import com.example.SADE.Model.Disciplina;
import com.example.SADE.Model.Escola;
import com.example.SADE.Repository.DisciplinaRepository;
import com.example.SADE.Repository.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@CrossOrigin("*")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repo;

    @Autowired
    private EscolaRepository escolaRepo;

    @GetMapping
    public List<Disciplina> listar(@RequestParam(required = false) Integer escola) {
        if (escola != null) {
            return repo.findByEscola(escola);
        }
        return repo.findAll();
    }

    @PostMapping
    public Disciplina cadastrar(@RequestBody Disciplina d) {
        if (d.getEscola() == null || d.getEscola().getId_escola() == null) {
            throw new RuntimeException("É necessário vincular a disciplina a uma escola.");
        }

        Escola e = escolaRepo.findById(d.getEscola().getId_escola());
        if (e == null) throw new RuntimeException("Escola não encontrada.");

        d.setEscola(e);
        
        return repo.save(d);
    }
}