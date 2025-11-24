package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.Regiao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegiaoRepository {

    public Regiao save(Regiao r) {
        String sql = "INSERT INTO Regiao (nome, mesorregiao) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getNome());
            ps.setString(2, r.getMesorregiao());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId_regiao(keys.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("Erro RegiaoRepository.save: " + e.getMessage());
        }
        return r;
    }

    public List<Regiao> findAll() {
        List<Regiao> lista = new ArrayList<>();
        String sql = "SELECT id_regiao, nome, mesorregiao FROM Regiao ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Regiao r = new Regiao();
                r.setId_regiao(rs.getInt("id_regiao"));
                r.setNome(rs.getString("nome"));
                r.setMesorregiao(rs.getString("mesorregiao"));
                lista.add(r);
            }

        } catch (Exception e) {
            System.out.println("Erro RegiaoRepository.findAll: " + e.getMessage());
        }
        return lista;
    }

    public Regiao findById(int id) {
        String sql = "SELECT id_regiao, nome, mesorregiao FROM Regiao WHERE id_regiao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Regiao r = new Regiao();
                    r.setId_regiao(rs.getInt("id_regiao"));
                    r.setNome(rs.getString("nome"));
                    r.setMesorregiao(rs.getString("mesorregiao"));
                    return r;
                }
            }

        } catch (Exception e) {
            System.out.println("Erro RegiaoRepository.findById: " + e.getMessage());
        }
        return null;
    }
}
