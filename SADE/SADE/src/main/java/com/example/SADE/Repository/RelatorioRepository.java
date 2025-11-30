package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.DTO.RelatorioDTO;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RelatorioRepository {

    public List<RelatorioDTO> ranking(Integer ano, String tipo, Integer idRegiao, String disciplina) {
        List<RelatorioDTO> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT e.nome, e.cidade, AVG(d.media_disciplina) as media " +
            "FROM Desempenho_Disciplina d " +
            "JOIN Escola e ON d.id_escola = e.id_escola " +
            "JOIN Disciplina dis ON d.id_disciplina = dis.id_disciplina " + 
            "WHERE 1=1 "
        );

        if (ano != null) sql.append(" AND d.ano_letivo = ? ");
        if (tipo != null && !tipo.isEmpty()) sql.append(" AND e.tipo_localizacao = ? ");
        if (idRegiao != null) sql.append(" AND e.id_regiao = ? ");
        if (disciplina != null && !disciplina.isEmpty()) sql.append(" AND dis.nome = ? ");

        sql.append(" GROUP BY e.id_escola, e.nome, e.cidade ORDER BY media DESC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (ano != null) ps.setInt(index++, ano);
            if (tipo != null && !tipo.isEmpty()) ps.setString(index++, tipo);
            if (idRegiao != null) ps.setInt(index++, idRegiao);
            if (disciplina != null && !disciplina.isEmpty()) ps.setString(index++, disciplina);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new RelatorioDTO(
                        rs.getString("nome"), 
                        rs.getString("cidade"), 
                        rs.getDouble("media")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public List<RelatorioDTO> porRegiao(Integer ano, String nomeDisciplina) {
        List<RelatorioDTO> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT r.nome, AVG(d.media_disciplina) as media " +
            "FROM Desempenho_Disciplina d " +
            "JOIN Escola e ON d.id_escola = e.id_escola " +
            "JOIN Regiao r ON e.id_regiao = r.id_regiao " +
            "JOIN Disciplina dis ON d.id_disciplina = dis.id_disciplina " +
            "WHERE 1=1 "
        );

        if (ano != null) sql.append(" AND d.ano_letivo = ? ");
        if (nomeDisciplina != null && !nomeDisciplina.isEmpty()) sql.append(" AND dis.nome = ? ");

        sql.append(" GROUP BY r.id_regiao, r.nome ORDER BY media DESC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (ano != null) ps.setInt(index++, ano);
            if (nomeDisciplina != null && !nomeDisciplina.isEmpty()) ps.setString(index++, nomeDisciplina);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new RelatorioDTO(rs.getString("nome"), "Média Regional", rs.getDouble("media")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public List<RelatorioDTO> evolucao(Integer ano, String nomeDisciplina) {
        List<RelatorioDTO> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT d.ano_letivo, AVG(d.media_disciplina) as media " +
            "FROM Desempenho_Disciplina d " +
            "JOIN Disciplina dis ON d.id_disciplina = dis.id_disciplina " +
            "WHERE 1=1 "
        );

        if (ano != null) sql.append(" AND d.ano_letivo = ? ");
        if (nomeDisciplina != null && !nomeDisciplina.isEmpty()) sql.append(" AND dis.nome = ? ");

        sql.append(" GROUP BY d.ano_letivo ORDER BY d.ano_letivo ASC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (ano != null) ps.setInt(index++, ano);
            if (nomeDisciplina != null && !nomeDisciplina.isEmpty()) ps.setString(index++, nomeDisciplina);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new RelatorioDTO(String.valueOf(rs.getInt("ano_letivo")), "Média Estadual", rs.getDouble("media")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public List<RelatorioDTO> rankingIdeb(Integer ano) {
        List<RelatorioDTO> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT e.nome, e.cidade, i.ideb " +
            "FROM Indicador_Educacional i " +
            "JOIN Escola e ON i.id_escola = e.id_escola " +
            "WHERE 1=1 "
        );

        if (ano != null) sql.append(" AND i.ano_letivo = ? ");

        sql.append(" ORDER BY i.ideb DESC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (ano != null) ps.setInt(1, ano);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new RelatorioDTO(rs.getString("nome"), rs.getString("cidade"), rs.getDouble("ideb")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
}