import React, { useState, useEffect } from "react";
import { 
  criarRegiao, getRegioes, 
  criarEscola, getEscolas, 
  criarGestor 
} from "../service/SecretariaService";

export default function PainelSecretaria() {
  const [regioes, setRegioes] = useState([]);
  const [escolas, setEscolas] = useState([]);

  const [formRegiao, setFormRegiao] = useState({ nome: "", mesorregiao: "" });

  const [formEscola, setFormEscola] = useState({ 
    nome: "", 
    codigoMec: "", 
    cidade: "", 
    idRegiao: "",
    tipoLocalizacao: "Urbana" 
  });
  
  const [formGestor, setFormGestor] = useState({ nome: "", idEscola: "" });

  const [codigoGerado, setCodigoGerado] = useState(null);

  useEffect(() => {
    carregarDados();
  }, []);

  async function carregarDados() {
    const listaRegioes = await getRegioes();
    setRegioes(listaRegioes);
    
    const listaEscolas = await getEscolas();
    setEscolas(listaEscolas);
  }

  async function handleSalvarRegiao(e) {
    e.preventDefault();
    try {
      await criarRegiao(formRegiao);
      alert("Região criada com sucesso!");
      setFormRegiao({ nome: "", mesorregiao: "" });
      carregarDados();
    } catch (error) {
      alert("Erro ao criar região.");
    }
  }

  async function handleSalvarEscola(e) {
    e.preventDefault();
    if (!formEscola.idRegiao) return alert("Selecione uma região!");

    try {
      const payload = {
        nome: formEscola.nome,
        codigo_mec: formEscola.codigoMec,
        cidade: formEscola.cidade,
        tipo_localizacao: formEscola.tipoLocalizacao,
        regiao: { id_regiao: formEscola.idRegiao }
      };

      await criarEscola(payload);
      alert("Escola criada com sucesso!");

      setFormEscola({ nome: "", codigoMec: "", cidade: "", idRegiao: "", tipoLocalizacao: "Urbana" });
      carregarDados();
    } catch (error) {
      console.error(error);
      alert("Erro ao criar escola.");
    }
  }

  async function handleSalvarGestor(e) {
    e.preventDefault();
    if (!formGestor.idEscola) return alert("Selecione uma escola!");

    try {
      const payload = {
        nome: formGestor.nome,
        escola: { id_escola: formGestor.idEscola }
      };

      const gestorCriado = await criarGestor(payload);

      setCodigoGerado(gestorCriado.codigo_acesso);
      
      alert(`Gestor criado! Código de acesso: ${gestorCriado.codigo_acesso}`);
      setFormGestor({ nome: "", idEscola: "" });
    } catch (error) {
      console.error(error);
      alert("Erro ao criar gestor.");
    }
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Painel Administrativo da Secretaria</h1>

      <div style={{ border: "1px solid #ccc", padding: "10px", marginBottom: "20px" }}>
        <h3>1. Cadastrar Região</h3>
        <form onSubmit={handleSalvarRegiao}>
          <input 
            placeholder="Nome da Região" 
            value={formRegiao.nome}
            onChange={e => setFormRegiao({...formRegiao, nome: e.target.value})}
            required
          />
          <input 
            placeholder="Mesorregião" 
            value={formRegiao.mesorregiao}
            onChange={e => setFormRegiao({...formRegiao, mesorregiao: e.target.value})}
            required
          />
          <button type="submit">Salvar Região</button>
        </form>
      </div>

      <div style={{ border: "1px solid #ccc", padding: "10px", marginBottom: "20px" }}>
        <h3>2. Cadastrar Escola</h3>
        <form onSubmit={handleSalvarEscola}>
          <input 
            placeholder="Nome da Escola" 
            value={formEscola.nome}
            onChange={e => setFormEscola({...formEscola, nome: e.target.value})}
            required
            style={{ width: "300px", marginRight: "10px" }}
          />
          <input 
            placeholder="Código MEC" 
            value={formEscola.codigoMec}
            onChange={e => setFormEscola({...formEscola, codigoMec: e.target.value})}
            style={{ marginRight: "10px" }}
          />
          <input 
            placeholder="Cidade" 
            value={formEscola.cidade}
            onChange={e => setFormEscola({...formEscola, cidade: e.target.value})}
            style={{ marginRight: "10px" }}
          />

          <label>Localização: </label>
          <select 
            value={formEscola.tipoLocalizacao} 
            onChange={e => setFormEscola({...formEscola, tipoLocalizacao: e.target.value})}
            style={{ marginRight: "10px", padding: "5px" }}
          >
            <option value="Urbana">Urbana</option>
            <option value="Rural">Rural</option>
          </select>
          
          <br /><br />

          <select 
            value={formEscola.idRegiao} 
            onChange={e => setFormEscola({...formEscola, idRegiao: e.target.value})}
            required
          >
            <option value="">Selecione a Região...</option>
            {regioes.map(r => (
              <option key={r.id_regiao} value={r.id_regiao}>
                {r.nome} - {r.mesorregiao}
              </option>
            ))}
          </select>

          <button type="submit" style={{ marginLeft: "10px" }}>Salvar Escola</button>
        </form>
      </div>

      <div style={{ border: "2px solid #007bff", padding: "15px", backgroundColor: "#f0f8ff" }}>
        <h3>3. Cadastrar Gestor</h3>

        {codigoGerado && (
          <div style={{ backgroundColor: "#d4edda", padding: "10px", margin: "10px 0", color: "#155724" }}>
            <strong>SUCESSO!</strong> O código do novo gestor é: <h2>{codigoGerado}</h2>
            <button onClick={() => setCodigoGerado(null)}>Fechar</button>
          </div>
        )}

        <form onSubmit={handleSalvarGestor}>
          <input 
            placeholder="Nome do Gestor" 
            value={formGestor.nome}
            onChange={e => setFormGestor({...formGestor, nome: e.target.value})}
            required
            style={{ marginRight: "10px" }}
          />
          
          <select 
            value={formGestor.idEscola} 
            onChange={e => setFormGestor({...formGestor, idEscola: e.target.value})}
            required
          >
            <option value="">Selecione a Escola...</option>
            {escolas.map(e => (
              <option key={e.id_escola} value={e.id_escola}>
                {e.nome} ({e.cidade})
              </option>
            ))}
          </select>

          <button type="submit" style={{ marginLeft: "10px" }}>Gerar Acesso para Gestor</button>
        </form>
      </div>

    </div>
  );
}