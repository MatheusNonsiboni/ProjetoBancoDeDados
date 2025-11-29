package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.Disciplina;
import com.example.SADE.Model.Escola;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DisciplinaRepository {

    public Disciplina save(Disciplina d) {
        String sql = "INSERT INTO Disciplina (nome, area_conhecimento, id_escola) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getNome());
            ps.setString(2, d.getArea_conhecimento());
            ps.setInt(3, d.getEscola().getId_escola());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) d.setId_disciplina(keys.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("Erro DisciplinaRepository.save: " + e.getMessage());
        }
        return d;
    }

    public List<Disciplina> findByEscola(int idEscola) {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT id_disciplina, nome, area_conhecimento FROM Disciplina WHERE id_escola = ? ORDER BY nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEscola);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setId_disciplina(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome"));
                    d.setArea_conhecimento(rs.getString("area_conhecimento"));
                    
                    lista.add(d);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro DisciplinaRepository.findByEscola: " + e.getMessage());
        }
        return lista;
    }

    public List<Disciplina> findAll() {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT id_disciplina, nome, area_conhecimento, id_escola FROM Disciplina ORDER BY nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Disciplina d = new Disciplina();
                d.setId_disciplina(rs.getInt("id_disciplina"));
                d.setNome(rs.getString("nome"));
                d.setArea_conhecimento(rs.getString("area_conhecimento"));
                
                Escola e = new Escola();
                e.setId_escola(rs.getInt("id_escola"));
                d.setEscola(e);

                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro DisciplinaRepository.findAll: " + e.getMessage());
        }
        return lista;
    }
    
    public Disciplina findById(int id) {
        String sql = "SELECT id_disciplina, nome, area_conhecimento, id_escola FROM Disciplina WHERE id_disciplina = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setId_disciplina(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome"));
                    d.setArea_conhecimento(rs.getString("area_conhecimento"));
                    
                    Escola e = new Escola();
                    e.setId_escola(rs.getInt("id_escola"));
                    d.setEscola(e);
                    
                    return d;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro DisciplinaRepository.findById: " + e.getMessage());
        }
        return null;
    }
}