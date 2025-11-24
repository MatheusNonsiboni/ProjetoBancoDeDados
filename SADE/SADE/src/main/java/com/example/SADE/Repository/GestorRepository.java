package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.Gestor;
import com.example.SADE.Model.Escola;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GestorRepository {

    public Gestor save(Gestor g) {
        String sql = "INSERT INTO Gestor (nome, email, codigo_acesso, id_escola) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, g.getNome());
            ps.setString(2, g.getEmail());
            ps.setString(3, g.getCodigo_acesso());
            ps.setInt(4, g.getEscola().getId_escola());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) g.setId_gestor(keys.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("Erro GestorRepository.save: " + e.getMessage());
        }
        return g;
    }

    public Gestor findByCodigoAcesso(String codigo) {
        String sql = "SELECT g.id_gestor, g.nome, g.email, g.codigo_acesso, g.id_escola, " +
                     "e.nome as escola_nome, e.codigo_mec, e.cidade, e.tipo_localizacao, r.id_regiao, r.nome as reg_nome, r.mesorregiao " +
                     "FROM Gestor g " +
                     "LEFT JOIN Escola e ON g.id_escola = e.id_escola " +
                     "LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao " +
                     "WHERE g.codigo_acesso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Gestor g = new Gestor();
                    g.setId_gestor(rs.getInt("id_gestor"));
                    g.setNome(rs.getString("nome"));
                    g.setEmail(rs.getString("email"));
                    g.setCodigo_acesso(rs.getString("codigo_acesso"));

                    Escola e = new Escola();
                    e.setId_escola(rs.getInt("id_escola"));
                    e.setNome(rs.getString("escola_nome"));
                    e.setCodigo_mec(rs.getString("codigo_mec"));
                    e.setCidade(rs.getString("cidade"));
                    e.setTipo_localizacao(rs.getString("tipo_localizacao"));

                    int idReg = rs.getInt("id_regiao");
                    if (!rs.wasNull()) {
                        com.example.SADE.Model.Regiao r = new com.example.SADE.Model.Regiao();
                        r.setId_regiao(idReg);
                        r.setNome(rs.getString("reg_nome"));
                        r.setMesorregiao(rs.getString("mesorregiao"));
                        e.setRegiao(r);
                    }
                    g.setEscola(e);
                    return g;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro GestorRepository.findByCodigoAcesso: " + e.getMessage());
        }
        return null;
    }

    public Gestor findById(int id) {
        String sql = "SELECT id_gestor, nome, email, codigo_acesso, id_escola FROM Gestor WHERE id_gestor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Gestor g = new Gestor();
                    g.setId_gestor(rs.getInt("id_gestor"));
                    g.setNome(rs.getString("nome"));
                    g.setEmail(rs.getString("email"));
                    g.setCodigo_acesso(rs.getString("codigo_acesso"));
                    return g;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro GestorRepository.findById: " + e.getMessage());
        }
        return null;
    }

    public List<Gestor> findAll() {
        List<Gestor> lista = new ArrayList<>();
        String sql = "SELECT id_gestor, nome, email, codigo_acesso, id_escola FROM Gestor ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Gestor g = new Gestor();
                g.setId_gestor(rs.getInt("id_gestor"));
                g.setNome(rs.getString("nome"));
                g.setEmail(rs.getString("email"));
                g.setCodigo_acesso(rs.getString("codigo_acesso"));
                lista.add(g);
            }
        } catch (Exception e) {
            System.out.println("Erro GestorRepository.findAll: " + e.getMessage());
        }
        return lista;
    }
}