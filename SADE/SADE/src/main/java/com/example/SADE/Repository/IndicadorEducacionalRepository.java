package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IndicadorEducacionalRepository {

    public IndicadorEducacional save(IndicadorEducacional i) {
        String sql = "INSERT INTO Indicador_Educacional (id_escola, ano_letivo, ideb, taxa_evasao) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, i.getEscola().getId_escola());
            ps.setInt(2, i.getAno_letivo());
            ps.setDouble(3, i.getIdeb());
            ps.setDouble(4, i.getTaxa_evasao());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) i.setId_indicador(keys.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("Erro IndicadorEducacionalRepository.save: " + e.getMessage());
        }
        return i;
    }

    public List<IndicadorEducacional> findAll() {
        List<IndicadorEducacional> lista = new ArrayList<>();
        String sql = "SELECT ind.id_indicador, ind.ano_letivo, ind.ideb, ind.taxa_evasao, " +
                     "e.id_escola, e.nome as escola_nome, e.codigo_mec, e.cidade, e.tipo_localizacao, r.id_regiao, r.nome as reg_nome, r.mesorregiao " +
                     "FROM Indicador_Educacional ind " +
                     "LEFT JOIN Escola e ON ind.id_escola = e.id_escola " +
                     "LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao " +
                     "ORDER BY ind.ano_letivo DESC, e.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                IndicadorEducacional ind = new IndicadorEducacional();
                ind.setId_indicador(rs.getInt("id_indicador"));
                ind.setAno_letivo(rs.getInt("ano_letivo"));
                ind.setIdeb(rs.getDouble("ideb"));
                ind.setTaxa_evasao(rs.getDouble("taxa_evasao"));

                Escola e = new Escola();
                e.setId_escola(rs.getInt("id_escola"));
                e.setNome(rs.getString("escola_nome"));
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
                ind.setEscola(e);

                lista.add(ind);
            }

        } catch (Exception e) {
            System.out.println("Erro IndicadorEducacionalRepository.findAll: " + e.getMessage());
        }
        return lista;
    }

    public IndicadorEducacional findById(int id) {
        String sql = "SELECT ind.id_indicador, ind.ano_letivo, ind.ideb, ind.taxa_evasao, " +
                     "e.id_escola, e.nome as escola_nome, e.codigo_mec, e.cidade, e.tipo_localizacao, r.id_regiao, r.nome as reg_nome, r.mesorregiao " +
                     "FROM Indicador_Educacional ind " +
                     "LEFT JOIN Escola e ON ind.id_escola = e.id_escola " +
                     "LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao " +
                     "WHERE ind.id_indicador = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    IndicadorEducacional ind = new IndicadorEducacional();
                    ind.setId_indicador(rs.getInt("id_indicador"));
                    ind.setAno_letivo(rs.getInt("ano_letivo"));
                    ind.setIdeb(rs.getDouble("ideb"));
                    ind.setTaxa_evasao(rs.getDouble("taxa_evasao"));

                    Escola e = new Escola();
                    e.setId_escola(rs.getInt("id_escola"));
                    e.setNome(rs.getString("escola_nome"));
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
                    ind.setEscola(e);

                    return ind;
                }
            }

        } catch (Exception e) {
            System.out.println("Erro IndicadorEducacionalRepository.findById: " + e.getMessage());
        }
        return null;
    }
}
