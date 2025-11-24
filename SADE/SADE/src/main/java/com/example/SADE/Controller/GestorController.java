package com.example.SADE.Controller;

import com.example.SADE.Model.Gestor;
import com.example.SADE.Model.Escola;
import com.example.SADE.Repository.EscolaRepository;
import com.example.SADE.Repository.GestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gestores")
@CrossOrigin("*")
public class GestorController {

    @Autowired
    private GestorRepository repo;

    @Autowired
    private EscolaRepository escolaRepo;

    @PostMapping
    public Gestor cadastrar(@RequestBody Gestor g) {
        if (g.getEscola() == null || g.getEscola().getId_escola() == null) {
            throw new RuntimeException("Escola obrigatória para criar gestor.");
        }

        Escola e = escolaRepo.findById(g.getEscola().getId_escola());
        if (e == null) throw new RuntimeException("Escola não encontrada.");

        String codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        g.setCodigo_acesso(codigo);
        g.setEscola(e);

        return repo.save(g);
    }

    @GetMapping
    public List<Gestor> listar() {
        return repo.findAll();
    }

    @GetMapping("/login")
    public Gestor loginPorCodigo(@RequestParam String codigo) {
        return repo.findByCodigoAcesso(codigo);
    }

    @GetMapping("/{id}")
    public Gestor buscar(@PathVariable int id) {
        return repo.findById(id);
    }
}
