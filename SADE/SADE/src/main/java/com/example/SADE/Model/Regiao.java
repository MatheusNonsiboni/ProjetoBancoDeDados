package com.example.SADE.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Regiao")
public class Regiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_regiao;

    private String nome;
    private String mesorregiao;

    public Integer getId_regiao() { 
        return id_regiao; 
    }

    public void setId_regiao(Integer id_regiao) { 
        this.id_regiao = id_regiao; 
    }

    public String getNome() { 
        return nome; 
    }

    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public String getMesorregiao() { 
        return mesorregiao; 
    }

    public void setMesorregiao(String mesorregiao) { 
        this.mesorregiao = mesorregiao; 
    }
}
