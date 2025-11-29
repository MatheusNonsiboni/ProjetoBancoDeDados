import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { 
  getDisciplinas, 
  criarDisciplina, 
  criarDesempenho 
} from "../service/GestorService";

export default function PainelGestor() {
  const navigate = useNavigate();

  const [gestor, setGestor] = useState(null);

  const [disciplinas, setDisciplinas] = useState([]);

  const [formDisciplina, setFormDisciplina] = useState({ nome: "", area: "" });

  const [formDesempenho, setFormDesempenho] = useState({
    ano: "",
    media: "",
    frequencia: "",
    idDisciplina: ""
  });

  useEffect(() => {
    const usuarioSalvo = localStorage.getItem("usuario_gestor");
    
    if (!usuarioSalvo) {
      alert("Você precisa fazer login primeiro!");
      navigate("/");
      return;
    }

    const gestorObjeto = JSON.parse(usuarioSalvo);
    setGestor(gestorObjeto);

    carregarDisciplinas(gestorObjeto.escola.id_escola);
  }, []);

  async function carregarDisciplinas(idEscola) {
    try {
      const id = idEscola || (gestor && gestor.escola.id_escola);
      
      if (id) {
        const lista = await getDisciplinas(id);
        setDisciplinas(lista);
      }
    } catch (error) {
      console.error("Erro ao buscar disciplinas", error);
    }
  }

  async function handleNovaDisciplina(e) {
    e.preventDefault();
    try {
      const novaDisciplina = {
        nome: formDisciplina.nome,
        area_conhecimento: formDisciplina.area, 
        escola: { id_escola: gestor.escola.id_escola }
      };

      await criarDisciplina(novaDisciplina);
      
      alert("Disciplina cadastrada com sucesso!");
      setFormDisciplina({ nome: "", area: "" });
      
      carregarDisciplinas(gestor.escola.id_escola);
      
    } catch (error) {
      console.error(error);
      alert("Erro ao criar disciplina. Verifique o console.");
    }
  }

  async function handleLancarDesempenho(e) {
    e.preventDefault();
    
    if (!formDesempenho.idDisciplina) return alert("Escolha uma disciplina!");

    try {
      const payload = {
        ano: parseInt(formDesempenho.ano),
        media: parseFloat(formDesempenho.media),
        frequencia: parseFloat(formDesempenho.frequencia),
        disciplina: { id_disciplina: formDesempenho.idDisciplina },
        escola: { id_escola: gestor.escola.id_escola }
      };

      await criarDesempenho(payload);
      alert("Desempenho lançado com sucesso!");

      setFormDesempenho({ ...formDesempenho, media: "", frequencia: "" });
    } catch (error) {
      console.error(error);
      alert("Erro ao lançar desempenho.");
    }
  }

  function sair() {
    localStorage.removeItem("usuario_gestor");
    navigate("/");
  }

  if (!gestor) return <div>Carregando...</div>;

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>

      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "30px" }}>
        <div>
          <h1>Painel do Gestor</h1>
          <h3>Escola: {gestor.escola.nome}</h3>
          <p>Olá, {gestor.nome}.</p>
        </div>
        <button onClick={sair} style={{ backgroundColor: "#dc3545", color: "white", border: "none", padding: "10px", cursor: "pointer" }}>
          Sair
        </button>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
        
        <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px" }}>
          <h3>1. Nova Disciplina</h3>
          <p style={{ fontSize: "12px", color: "#666" }}>Cadastre matérias exclusivas da sua escola.</p>
          
          <form onSubmit={handleNovaDisciplina}>
            <input 
              placeholder="Nome (ex: Matemática)" 
              value={formDisciplina.nome}
              onChange={e => setFormDisciplina({...formDisciplina, nome: e.target.value})}
              style={styles.input}
              required
            />
            <input 
              placeholder="Área (ex: Exatas) - Opcional" 
              value={formDisciplina.area}
              onChange={e => setFormDisciplina({...formDisciplina, area: e.target.value})}
              style={styles.input}
            />
            <button type="submit" style={styles.btnAzul}>Cadastrar Disciplina</button>
          </form>
        </div>

        <div style={{ border: "2px solid #28a745", padding: "20px", borderRadius: "8px", backgroundColor: "#f9fff9" }}>
          <h3>2. Lançar Notas e Frequência</h3>
          
          <form onSubmit={handleLancarDesempenho}>
            
            <label>Selecione a Disciplina:</label>
            <select 
              value={formDesempenho.idDisciplina}
              onChange={e => setFormDesempenho({...formDesempenho, idDisciplina: e.target.value})}
              style={styles.input}
              required
            >
              <option value="">-- Selecione --</option>
              {disciplinas.map(d => (
                <option key={d.id_disciplina} value={d.id_disciplina}>
                  {d.nome} {d.area_conhecimento ? `(${d.area_conhecimento})` : ""}
                </option>
              ))}
            </select>

            <div style={{ display: "flex", gap: "10px" }}>
              <input 
                type="number" placeholder="Ano (ex: 2023)" 
                value={formDesempenho.ano}
                onChange={e => setFormDesempenho({...formDesempenho, ano: e.target.value})}
                style={styles.input} required
              />
              <input 
                type="number" step="0.1" placeholder="Média Final" 
                value={formDesempenho.media}
                onChange={e => setFormDesempenho({...formDesempenho, media: e.target.value})}
                style={styles.input} required
              />
            </div>
            
            <input 
              type="number" step="0.1" placeholder="Frequência (%)" 
              value={formDesempenho.frequencia}
              onChange={e => setFormDesempenho({...formDesempenho, frequencia: e.target.value})}
              style={styles.input} required
            />

            <button type="submit" style={styles.btnVerde}>Salvar Dados</button>
          </form>
        </div>

      </div>
    </div>
  );
}

const styles = {
  input: { display: "block", width: "100%", padding: "8px", marginBottom: "10px", boxSizing: "border-box" },
  btnAzul: { width: "100%", padding: "10px", backgroundColor: "#007bff", color: "white", border: "none", cursor: "pointer" },
  btnVerde: { width: "100%", padding: "10px", backgroundColor: "#28a745", color: "white", border: "none", cursor: "pointer", fontWeight: "bold" }
};