package com.example.SADE.Controller;

import com.example.SADE.Model.DesempenhoDisciplina;
import com.example.SADE.Model.Disciplina;
import com.example.SADE.Model.Escola;
import com.example.SADE.Model.Regiao;
import com.example.SADE.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/desempenhos")
@CrossOrigin("*")
public class DesempenhoDisciplinaController {

    @Autowired
    private DesempenhoDisciplinaRepository repo;

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private DisciplinaRepository discRepo;

    @Autowired
    private RegiaoRepository regiaoRepo;

    @GetMapping
    public List<DesempenhoDisciplina> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public DesempenhoDisciplina buscar(@PathVariable int id) {
        return repo.findById(id);
    }

    @PostMapping
    public DesempenhoDisciplina cadastrar(@RequestBody DesempenhoDisciplina d) {

        Escola e = escolaRepo.findById(d.getEscola().getId_escola());
        if (e == null) throw new RuntimeException("Escola não encontrada.");

        Disciplina disc = discRepo.findById(d.getDisciplina().getId_disciplina());
        if (disc == null) throw new RuntimeException("Disciplina não encontrada.");

        d.setEscola(e);
        d.setDisciplina(disc);

        return repo.save(d);
    }

    @GetMapping("/ranking")
    public List<DesempenhoDisciplina> rankingPorDisciplina(
            @RequestParam String disciplina,
            @RequestParam String regiao
    ) {
        List<DesempenhoDisciplina> todos = repo.findAll();

        return todos.stream()
                .filter(d -> d.getDisciplina() != null &&
                             d.getDisciplina().getNome().equalsIgnoreCase(disciplina))
                .filter(d -> d.getEscola().getRegiao() != null &&
                             d.getEscola().getRegiao().getNome().equalsIgnoreCase(regiao))
                .sorted(Comparator.comparing(DesempenhoDisciplina::getMedia_disciplina).reversed())
                .collect(Collectors.toList());
    }
}