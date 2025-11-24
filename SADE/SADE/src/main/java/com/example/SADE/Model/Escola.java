package com.example.SADE.Model;

public class Escola {

    private Integer id_escola;
    private String nome;
    private String codigo_mec;
    private String cidade;
    private String tipo_localizacao;
    private Regiao regiao;

    public Integer getId_escola() {
        return id_escola;
    }

    public void setId_escola(Integer id_escola) {
        this.id_escola = id_escola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo_mec() {
        return codigo_mec;
    }

    public void setCodigo_mec(String codigo_mec) {
        this.codigo_mec = codigo_mec;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTipo_localizacao() {
        return tipo_localizacao;
    }

    public void setTipo_localizacao(String tipo_localizacao) {
        this.tipo_localizacao = tipo_localizacao;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }
}