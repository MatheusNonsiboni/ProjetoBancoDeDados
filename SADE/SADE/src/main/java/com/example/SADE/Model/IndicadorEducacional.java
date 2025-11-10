package com.example.SADE.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Indicador_Educacional")
public class IndicadorEducacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_indicador;

    @ManyToOne
    @JoinColumn(name = "id_escola", nullable = false)
    private Escola escola;

    @NotNull
    private Integer ano_letivo;

    @NotNull
    private Double ideb;

    @NotNull
    private Double taxa_evasao;

    public Integer getId_indicador() {
        return id_indicador;
    }

    public void setId_indicador(Integer id_indicador) {
        this.id_indicador = id_indicador;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Integer getAno_letivo() {
        return ano_letivo;
    }

    public void setAno_letivo(Integer ano_letivo) {
        this.ano_letivo = ano_letivo;
    }

    public Double getIdeb() {
        return ideb;
    }

    public void setIdeb(Double ideb) {
        this.ideb = ideb;
    }

    public Double getTaxa_evasao() {
        return taxa_evasao;
    }

    public void setTaxa_evasao(Double taxa_evasao) {
        this.taxa_evasao = taxa_evasao;
    }
}