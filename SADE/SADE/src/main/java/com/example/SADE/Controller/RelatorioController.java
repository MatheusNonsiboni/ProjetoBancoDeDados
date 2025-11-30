package com.example.SADE.Controller;

import com.example.SADE.Model.DTO.RelatorioDTO;
import com.example.SADE.Repository.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin("*")
public class RelatorioController {

    @Autowired
    private RelatorioRepository repo;

    @GetMapping("/ranking")
    public List<RelatorioDTO> getRanking(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer regiao,
            @RequestParam(required = false) String disciplina
    ) {
        return repo.ranking(ano, tipo, regiao, disciplina);
    }

    @GetMapping("/regiao")
    public List<RelatorioDTO> getPorRegiao(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String disciplina
    ) {
        return repo.porRegiao(ano, disciplina);
    }

    @GetMapping("/evolucao")
    public List<RelatorioDTO> getEvolucao(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String disciplina
    ) {
        return repo.evolucao(ano, disciplina);
    }

    @GetMapping("/ideb")
    public List<RelatorioDTO> getRankingIdeb(
            @RequestParam(required = false) Integer ano
    ) {
        return repo.rankingIdeb(ano);
    }
}