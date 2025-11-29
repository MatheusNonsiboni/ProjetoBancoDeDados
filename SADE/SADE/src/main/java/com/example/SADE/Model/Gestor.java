package com.example.SADE.Model;

public class Gestor {

    private Integer id_gestor;
    private String nome;
    private String codigo_acesso;
    private Escola escola;

    public Integer getId_gestor() {
        return id_gestor;
    }

    public void setId_gestor(Integer id_gestor) {
        this.id_gestor = id_gestor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo_acesso() {
        return codigo_acesso;
    }

    public void setCodigo_acesso(String codigo_acesso) {
        this.codigo_acesso = codigo_acesso;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }
}

