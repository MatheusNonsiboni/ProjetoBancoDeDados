import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { 
  getRelatorioRanking, 
  getRelatorioRegiao, 
  getRelatorioEvolucao,
  getRelatorioIdeb,
  getRegioes 
} from "../service/SecretariaService";

import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line 
} from 'recharts';

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

  const [visualizacao, setVisualizacao] = useState("grafico");

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

  function renderConteudo() {
    if (dados.length === 0) {
      return <div style={{textAlign: "center", padding: "50px", color: "#666"}}>Nenhum dado encontrado para gerar gráficos.</div>;
    }

    if (visualizacao === "tabela") {
      return renderTabela();
    }

    if (tipoRelatorio === "evolucao") {
      return (
        <div style={{ height: 400, marginTop: 20 }}>
          <h3 style={{textAlign: "center", color: "#555"}}>Evolução Temporal (Anual)</h3>
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={dados} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="nome" label={{ value: 'Ano', position: 'insideBottomRight', offset: 0 }} />
              <YAxis domain={[4, 9]} />
              <Tooltip formatter={(value) => Number(value).toFixed(2)} />
              <Legend />
              <Line type="monotone" dataKey="media" name="Média Geral" stroke="#8884d8" strokeWidth={3} activeDot={{ r: 8 }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      );
    }

    if (tipoRelatorio === "regiao") {
      return (
        <div style={{ height: 400, marginTop: 20 }}>
          <h3 style={{textAlign: "center", color: "#555"}}>Comparativo por Região</h3>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={dados} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="nome" />
              <YAxis domain={[4, 9]} />
              <Tooltip formatter={(value) => Number(value).toFixed(2)} />
              <Legend />
              <Bar dataKey="media" name="Média da Região" fill="#82ca9d" barSize={60} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      );
    }

    if (tipoRelatorio === "ranking" || tipoRelatorio === "ideb") {
      const dadosTop10 = dados.slice(0, 10); 
      const corBarra = tipoRelatorio === "ideb" ? "#ffc107" : "#007bff";
      const titulo = tipoRelatorio === "ideb" ? "Top 10 Escolas (IDEB)" : "Top 10 Escolas (Média Notas)";

      return (
        <div style={{ height: 500, marginTop: 20 }}>
          <h3 style={{textAlign: "center", color: "#555"}}>{titulo}</h3>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart layout="vertical" data={dadosTop10} margin={{ top: 5, right: 30, left: 100, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis type="number" domain={[5, 9]} />
              <YAxis dataKey="nome" type="category" width={150} tick={{fontSize: 12}} />
              <Tooltip formatter={(value) => Number(value).toFixed(2)} />
              <Legend />
              <Bar 
                dataKey="media" 
                name="Nota" 
                fill={corBarra} 
                barSize={25} 
                label={{ 
                  position: 'right', 
                  fill: '#333', 
                  formatter: (value) => Number(value).toFixed(2)
                }} 
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      );
    }
  }

  function renderTabela() {
    return (
      <div style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.1)", borderRadius: "8px", overflow: "hidden" }}>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ backgroundColor: "#343a40", color: "white", textAlign: "left" }}>
              <th style={{ padding: "15px" }}>Nome</th>
              <th style={{ padding: "15px" }}>Detalhe</th>
              <th style={{ padding: "15px" }}>Nota</th>
            </tr>
          </thead>
          <tbody>
            {dados.map((item, index) => (
              <tr key={index} style={{ borderBottom: "1px solid #eee", backgroundColor: index % 2 === 0 ? "white" : "#f9f9f9" }}>
                <td style={{ padding: "12px" }}>{item.nome}</td>
                <td style={{ padding: "12px", color: "#555" }}>{item.detalhe}</td>
                <td style={{ padding: "12px", fontWeight: "bold" }}>{item.media ? item.media.toFixed(2) : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial", maxWidth: "1100px", margin: "0 auto" }}>

      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "30px" }}>
        <div>
          <h1 style={{ margin: 0 }}>Dashboard SADE</h1>
          <p style={{ color: "#666" }}>Análise visual de dados educacionais</p>
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
        
        <input type="number" placeholder="Ano Letivo" value={filtroAno} onChange={e => setFiltroAno(e.target.value)} style={styles.inputFiltro} />

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

        <div style={{ marginLeft: "auto", display: "flex", gap: "5px" }}>
          <button onClick={() => setVisualizacao("grafico")} style={visualizacao === "grafico" ? styles.btnViewActive : styles.btnView}>📊 Gráfico</button>
          <button onClick={() => setVisualizacao("tabela")} style={visualizacao === "tabela" ? styles.btnViewActive : styles.btnView}>📋 Tabela</button>
        </div>
      </div>

      <div style={{ border: "1px solid #eee", padding: "20px", borderRadius: "8px", backgroundColor: "white", boxShadow: "0 4px 12px rgba(0,0,0,0.05)" }}>
        {renderConteudo()}
      </div>

    </div>
  );
}

const styles = {
  btnVoltar: { padding: "10px 15px", border: "1px solid #ccc", background: "white", cursor: "pointer", borderRadius: "5px" },
  inputFiltro: { padding: "8px", borderRadius: "4px", border: "1px solid #ccc", width: "100px" },
  selectFiltro: { padding: "8px", borderRadius: "4px", border: "1px solid #ccc", minWidth: "140px" },
  btnLimpar: { padding: "8px 12px", border: "none", background: "#6c757d", color: "white", borderRadius: "4px", cursor: "pointer" },
  btnView: { padding: "8px 12px", border: "1px solid #ccc", background: "white", cursor: "pointer", borderRadius: "4px" },
  btnViewActive: { padding: "8px 12px", border: "1px solid #007bff", background: "#007bff", color: "white", cursor: "pointer", borderRadius: "4px" }
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