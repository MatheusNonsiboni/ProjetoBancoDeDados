package com.example.SADE.Model.DTO;

public class RelatorioDTO {
    private String nome;
    private String detalhe;
    private Double media;

    public RelatorioDTO(String nome, String detalhe, Double media) {
        this.nome = nome;
        this.detalhe = detalhe;
        this.media = media;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

}