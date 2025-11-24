package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.Escola;
import com.example.SADE.Model.Regiao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EscolaRepository {

    public Escola save(Escola e) {
        String sql = "INSERT INTO Escola (nome, codigo_mec, cidade, tipo_localizacao, id_regiao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getNome());
            ps.setString(2, e.getCodigo_mec());
            ps.setString(3, e.getCidade());
            ps.setString(4, e.getTipo_localizacao());
            ps.setInt(5, e.getRegiao().getId_regiao());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId_escola(keys.getInt(1));
            }

        } catch (Exception ex) {
            System.out.println("Erro EscolaRepository.save: " + ex.getMessage());
        }
        return e;
    }

    public List<Escola> findAll() {
        List<Escola> lista = new ArrayList<>();
        String sql = "SELECT e.id_escola, e.nome, e.codigo_mec, e.cidade, e.tipo_localizacao, e.id_regiao, r.nome as reg_nome, r.mesorregiao " +
                     "FROM Escola e LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao ORDER BY e.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Escola e = new Escola();
                e.setId_escola(rs.getInt("id_escola"));
                e.setNome(rs.getString("nome"));
                e.setCodigo_mec(rs.getString("codigo_mec"));
                e.setCidade(rs.getString("cidade"));
                e.setTipo_localizacao(rs.getString("tipo_localizacao"));

                int idReg = rs.getInt("id_regiao");
                if (!rs.wasNull()) {
                    Regiao r = new Regiao();
                    r.setId_regiao(idReg);
                    r.setNome(rs.getString("reg_nome"));
                    r.setMesorregiao(rs.getString("mesorregiao"));
                    e.setRegiao(r);
                }
                lista.add(e);
            }

        } catch (Exception ex) {
            System.out.println("Erro EscolaRepository.findAll: " + ex.getMessage());
        }
        return lista;
    }

    public Escola findById(int id) {
        String sql = "SELECT e.id_escola, e.nome, e.codigo_mec, e.cidade, e.tipo_localizacao, e.id_regiao, r.nome as reg_nome, r.mesorregiao " +
                     "FROM Escola e LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao WHERE e.id_escola = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Escola e = new Escola();
                    e.setId_escola(rs.getInt("id_escola"));
                    e.setNome(rs.getString("nome"));
                    e.setCodigo_mec(rs.getString("codigo_mec"));
                    e.setCidade(rs.getString("cidade"));
                    e.setTipo_localizacao(rs.getString("tipo_localizacao"));

                    int idReg = rs.getInt("id_regiao");
                    if (!rs.wasNull()) {
                        Regiao r = new Regiao();
                        r.setId_regiao(idReg);
                        r.setNome(rs.getString("reg_nome"));
                        r.setMesorregiao(rs.getString("mesorregiao"));
                        e.setRegiao(r);
                    }
                    return e;
                }
            }

        } catch (Exception ex) {
            System.out.println("Erro EscolaRepository.findById: " + ex.getMessage());
        }
        return null;
    }
}