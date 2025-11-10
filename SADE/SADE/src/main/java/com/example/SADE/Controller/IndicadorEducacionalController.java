package com.example.SADE.Controller;

import com.example.SADE.Model.IndicadorEducacional;
import com.example.SADE.Repository.IndicadorEducacionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/indicadores")
@Validated
public class IndicadorEducacionalController {

    @Autowired
    private IndicadorEducacionalRepository indicadorRepository;

    @GetMapping
    public List<IndicadorEducacional> listar() {
        return indicadorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody IndicadorEducacional indicador) {
        if (indicador.getAno_letivo() == null || 
            indicador.getIdeb() == null || 
            indicador.getTaxa_evasao() == null || 
            indicador.getEscola() == null) {
            return ResponseEntity.badRequest().body("Erro: Todos os campos (ano_letivo, ideb, taxa_evasao e escola) são obrigatórios.");
        }

        IndicadorEducacional salvo = indicadorRepository.save(indicador);
        return ResponseEntity.ok(salvo);
    }
}