package com.example.SADE.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Desempenho_Disciplina")
public class DesempenhoDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_desempenho;

    @ManyToOne
    @JoinColumn(name = "id_escola", nullable = false)
    private Escola escola;

    @ManyToOne
    @JoinColumn(name = "id_disciplina", nullable = false)
    private Disciplina disciplina;

    private Integer ano_letivo;
    private Double media_disciplina;
    private Double frequencia_media;

    public Integer getId_desempenho() {
        return id_desempenho;
    }

    public void setId_desempenho(Integer id_desempenho) {
        this.id_desempenho = id_desempenho;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getAno_letivo() {
        return ano_letivo;
    }

    public void setAno_letivo(Integer ano_letivo) {
        this.ano_letivo = ano_letivo;
    }

    public Double getMedia_disciplina() {
        return media_disciplina;
    }

    public void setMedia_disciplina(Double media_disciplina) {
        this.media_disciplina = media_disciplina;
    }

    public Double getFrequencia_media() {
        return frequencia_media;
    }

    public void setFrequencia_media(Double frequencia_media) {
        this.frequencia_media = frequencia_media;
    }
}