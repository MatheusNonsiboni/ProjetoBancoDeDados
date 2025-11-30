import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { 
  getRelatorioRanking, 
  getRelatorioRegiao, 
  getRelatorioEvolucao,
  getRelatorioIdeb,
  getRegioes 
} from "../service/SecretariaService";

const LISTA_DISCIPLINAS = ["Matemática", "Português", "História", "Geografia", "Biologia"];

export default function Relatorios() {
  const navigate = useNavigate();

  const [dados, setDados] = useState([]);
  const [listaRegioes, setListaRegioes] = useState([]);

  const [tipoRelatorio, setTipoRelatorio] = useState("ranking");

  const [filtroAno, setFiltroAno] = useState("");
  const [filtroTipo, setFiltroTipo] = useState("");
  const [filtroRegiao, setFiltroRegiao] = useState("");
  const [filtroDisciplina, setFiltroDisciplina] = useState("");

  useEffect(() => {
    async function carregarRegioes() {
      const r = await getRegioes();
      setListaRegioes(r);
    }
    carregarRegioes();
  }, []);

  useEffect(() => {
    carregarRelatorio();
  }, [tipoRelatorio, filtroAno, filtroTipo, filtroRegiao, filtroDisciplina]);

  async function carregarRelatorio() {
    let resultado = [];
    try {
      if (tipoRelatorio === "ranking") {
        resultado = await getRelatorioRanking(filtroAno, filtroTipo, filtroRegiao, filtroDisciplina);
      } else if (tipoRelatorio === "regiao") {
        resultado = await getRelatorioRegiao(filtroAno, filtroDisciplina);
      } else if (tipoRelatorio === "evolucao") {
        resultado = await getRelatorioEvolucao(filtroAno, filtroDisciplina);
      } else if (tipoRelatorio === "ideb") {
        resultado = await getRelatorioIdeb(filtroAno);
      }
      setDados(resultado);
    } catch (error) {
      console.error("Erro ao carregar relatório:", error);
    }
  }

  function limparFiltros() {
    setFiltroAno(""); setFiltroTipo(""); setFiltroRegiao(""); setFiltroDisciplina("");
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial", maxWidth: "1000px", margin: "0 auto" }}>

      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "30px" }}>
        <div>
          <h1 style={{ margin: 0 }}>Transparência SADE</h1>
          <p style={{ color: "#666" }}>Dados públicos da educação estadual</p>
        </div>
        <button onClick={() => navigate("/")} style={styles.btnVoltar}>⬅ Voltar ao Início</button>
      </div>

      <div style={{ display: "flex", gap: "10px", marginBottom: "20px", borderBottom: "1px solid #ccc", paddingBottom: "10px", overflowX: "auto" }}>
        <button onClick={() => setTipoRelatorio("ranking")} style={getEstiloAba(tipoRelatorio === "ranking")}>🏆 Ranking Notas</button>
        <button onClick={() => setTipoRelatorio("ideb")} style={getEstiloAba(tipoRelatorio === "ideb")}>⭐ Ranking IDEB</button>
        <button onClick={() => setTipoRelatorio("regiao")} style={getEstiloAba(tipoRelatorio === "regiao")}>🗺️ Por Região</button>
        <button onClick={() => setTipoRelatorio("evolucao")} style={getEstiloAba(tipoRelatorio === "evolucao")}>📈 Evolução Anual</button>
      </div>

      <div style={{ backgroundColor: "#f8f9fa", padding: "15px", borderRadius: "8px", marginBottom: "20px", display: "flex", gap: "15px", alignItems: "center", flexWrap: "wrap" }}>
        <strong>Filtros:</strong>

        <input 
          type="number" placeholder="Ano Letivo" 
          value={filtroAno} onChange={e => setFiltroAno(e.target.value)} 
          style={styles.inputFiltro} 
        />

        {tipoRelatorio === "ranking" && (
          <>
            <select value={filtroTipo} onChange={e => setFiltroTipo(e.target.value)} style={styles.selectFiltro}>
              <option value="">Todas Localizações</option>
              <option value="Urbana">Urbana</option>
              <option value="Rural">Rural</option>
            </select>
            <select value={filtroRegiao} onChange={e => setFiltroRegiao(e.target.value)} style={styles.selectFiltro}>
              <option value="">Todas as Regiões</option>
              {listaRegioes.map(r => <option key={r.id_regiao} value={r.id_regiao}>{r.nome}</option>)}
            </select>
          </>
        )}

        {tipoRelatorio !== "ideb" && (
          <select value={filtroDisciplina} onChange={e => setFiltroDisciplina(e.target.value)} style={styles.selectFiltro}>
            <option value="">Todas Disciplinas</option>
            {LISTA_DISCIPLINAS.map(d => <option key={d} value={d}>{d}</option>)}
          </select>
        )}

        <button onClick={limparFiltros} style={styles.btnLimpar}>Limpar</button>
      </div>

      <div style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.1)", borderRadius: "8px", overflow: "hidden" }}>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ backgroundColor: "#007bff", color: "white", textAlign: "left" }}>
              <th style={{ padding: "15px" }}>
                {tipoRelatorio === "evolucao" ? "Ano Letivo" : tipoRelatorio === "regiao" ? "Região" : "Escola"}
              </th>
              <th style={{ padding: "15px" }}>Detalhe</th>
              <th style={{ padding: "15px" }}>
                {tipoRelatorio === "ideb" ? "Nota IDEB" : "Média Geral"}
              </th>
            </tr>
          </thead>
          <tbody>
            {dados.length === 0 ? (
              <tr><td colSpan="3" style={{ padding: "30px", textAlign: "center", color: "#666" }}>Nenhum dado encontrado.</td></tr>
            ) : (
              dados.map((item, index) => (
                <tr key={index} style={{ borderBottom: "1px solid #eee", backgroundColor: index % 2 === 0 ? "white" : "#f9f9f9" }}>
                  <td style={{ padding: "12px", fontWeight: "bold" }}>{item.nome}</td>
                  <td style={{ padding: "12px", color: "#555" }}>{item.detalhe}</td>
                  <td style={{ padding: "12px" }}>
                    <span style={{ 
                      backgroundColor: 
                        (tipoRelatorio === "ideb" ? (item.media >= 6.0 ? "#6ee301ff" : "#ff8000ff") :
                        (item.media >= 6.0 ? "#28a745" : "#dc3545")),
                      color: tipoRelatorio === "ideb" ? "black" : "white", 
                      padding: "5px 12px", borderRadius: "20px", fontWeight: "bold", fontSize: "14px"
                    }}>
                      {item.media ? item.media.toFixed(2) : "-"}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

    </div>
  );
}

const styles = {
  btnVoltar: { padding: "10px 15px", border: "1px solid #ccc", background: "white", cursor: "pointer", borderRadius: "5px" },
  inputFiltro: { padding: "8px", borderRadius: "4px", border: "1px solid #ccc", width: "120px" },
  selectFiltro: { padding: "8px", borderRadius: "4px", border: "1px solid #ccc", minWidth: "150px" },
  btnLimpar: { padding: "8px 12px", border: "none", background: "#6c757d", color: "white", borderRadius: "4px", cursor: "pointer", marginLeft: "auto" }
};

function getEstiloAba(ativo) {
  return {
    padding: "10px 15px",
    cursor: "pointer",
    border: "none",
    borderBottom: ativo ? "3px solid #007bff" : "3px solid transparent",
    background: "none",
    fontWeight: ativo ? "bold" : "normal",
    color: ativo ? "#007bff" : "#555",
    fontSize: "15px",
    whiteSpace: "nowrap"
  };
}