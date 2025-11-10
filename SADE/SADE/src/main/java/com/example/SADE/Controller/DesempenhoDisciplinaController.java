package com.example.SADE.Controller;

import com.example.SADE.Model.DesempenhoDisciplina;
import com.example.SADE.Repository.DesempenhoDisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desempenho-disciplina")
public class DesempenhoDisciplinaController {

    @Autowired
    private DesempenhoDisciplinaRepository desempenhoRepository;

    @GetMapping
    public List<DesempenhoDisciplina> listar() {
        return desempenhoRepository.findAll();
    }

    @PostMapping
    public DesempenhoDisciplina cadastrar(@RequestBody DesempenhoDisciplina desempenho) {
        return desempenhoRepository.save(desempenho);
    }
}

