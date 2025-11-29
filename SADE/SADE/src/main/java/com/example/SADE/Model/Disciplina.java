package com.example.SADE.Model;

public class Disciplina {

    private Integer id_disciplina;
    private String nome;
    private String area_conhecimento; 
    private Escola escola;

    public Integer getId_disciplina() {
        return id_disciplina;
    }

    public void setId_disciplina(Integer id_disciplina) {
        this.id_disciplina = id_disciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea_conhecimento() {
        return area_conhecimento;
    }

    public void setArea_conhecimento(String area_conhecimento) {
        this.area_conhecimento = area_conhecimento;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }
}
