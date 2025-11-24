package com.example.SADE.Repository;

import com.example.SADE.Database.ConnectionFactory;
import com.example.SADE.Model.*;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DesempenhoDisciplinaRepository {

    public DesempenhoDisciplina save(DesempenhoDisciplina d) {
        String sql = "INSERT INTO Desempenho_Disciplina (id_escola, id_disciplina, ano_letivo, media_disciplina, frequencia_media) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, d.getEscola().getId_escola());
            if (d.getDisciplina() != null) ps.setInt(2, d.getDisciplina().getId_disciplina()); else ps.setNull(2, Types.INTEGER);
            ps.setInt(3, d.getAno_letivo());
            ps.setDouble(4, d.getMedia_disciplina());
            ps.setDouble(5, d.getFrequencia_media());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) d.setId_desempenho(keys.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("Erro DesempenhoDisciplinaRepository.save: " + e.getMessage());
        }
        return d;
    }

    public List<DesempenhoDisciplina> findAll() {
        List<DesempenhoDisciplina> lista = new ArrayList<>();
        String sql = "SELECT d.id_desempenho, d.ano_letivo, d.media_disciplina, d.frequencia_media, " +
                     "e.id_escola, e.nome as escola_nome, e.codigo_mec, e.cidade, e.tipo_localizacao, r.id_regiao, r.nome as reg_nome, r.mesorregiao, " +
                     "dis.id_disciplina, dis.nome as disciplina_nome " +
                     "FROM Desempenho_Disciplina d " +
                     "LEFT JOIN Escola e ON d.id_escola = e.id_escola " +
                     "LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao " +
                     "LEFT JOIN Disciplina dis ON d.id_disciplina = dis.id_disciplina " +
                     "ORDER BY d.ano_letivo DESC, dis.nome, e.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DesempenhoDisciplina d = new DesempenhoDisciplina();
                d.setId_desempenho(rs.getInt("id_desempenho"));
                d.setAno_letivo(rs.getInt("ano_letivo"));
                d.setMedia_disciplina(rs.getDouble("media_disciplina"));
                d.setFrequencia_media(rs.getDouble("frequencia_media"));

                // Escola + Regiao
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
                d.setEscola(e);

                // Disciplina (pode ser null)
                int idDisc = rs.getInt("id_disciplina");
                if (!rs.wasNull()) {
                    Disciplina dis = new Disciplina();
                    dis.setId_disciplina(idDisc);
                    dis.setNome(rs.getString("disciplina_nome"));
                    d.setDisciplina(dis);
                }

                lista.add(d);
            }

        } catch (Exception e) {
            System.out.println("Erro DesempenhoDisciplinaRepository.findAll: " + e.getMessage());
        }
        return lista;
    }

    public DesempenhoDisciplina findById(int id) {
        String sql = "SELECT d.id_desempenho, d.ano_letivo, d.media_disciplina, d.frequencia_media, " +
                     "e.id_escola, e.nome as escola_nome, e.codigo_mec, e.cidade, e.tipo_localizacao, r.id_regiao, r.nome as reg_nome, r.mesorregiao, " +
                     "dis.id_disciplina, dis.nome as disciplina_nome " +
                     "FROM Desempenho_Disciplina d " +
                     "LEFT JOIN Escola e ON d.id_escola = e.id_escola " +
                     "LEFT JOIN Regiao r ON e.id_regiao = r.id_regiao " +
                     "LEFT JOIN Disciplina dis ON d.id_disciplina = dis.id_disciplina " +
                     "WHERE d.id_desempenho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DesempenhoDisciplina d = new DesempenhoDisciplina();
                    d.setId_desempenho(rs.getInt("id_desempenho"));
                    d.setAno_letivo(rs.getInt("ano_letivo"));
                    d.setMedia_disciplina(rs.getDouble("media_disciplina"));
                    d.setFrequencia_media(rs.getDouble("frequencia_media"));

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
                    d.setEscola(e);

                    int idDisc = rs.getInt("id_disciplina");
                    if (!rs.wasNull()) {
                        Disciplina dis = new Disciplina();
                        dis.setId_disciplina(idDisc);
                        dis.setNome(rs.getString("disciplina_nome"));
                        d.setDisciplina(dis);
                    }

                    return d;
                }
            }

        } catch (Exception e) {
            System.out.println("Erro DesempenhoDisciplinaRepository.findById: " + e.getMessage());
        }
        return null;
    }
}
