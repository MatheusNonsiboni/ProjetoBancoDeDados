import axios from "axios";

const API_URL = "http://localhost:8080/api/gestores";
const API_BASE = "http://localhost:8080/api";

export async function loginGestor(codigo) {
  const response = await axios.get(`${API_URL}/login?codigo=${codigo}`);
  return response.data; 
}

export async function getDisciplinas(idEscola) {
  const url = idEscola 
    ? `${API_BASE}/disciplinas?escola=${idEscola}` 
    : `${API_BASE}/disciplinas`;
    
  const response = await axios.get(url);
  return response.data;
}

export async function criarDisciplina(disciplina) {
  const response = await axios.post(`${API_BASE}/disciplinas`, disciplina);
  return response.data;
}

export async function criarDesempenho(desempenho) {
  const response = await axios.post(`${API_BASE}/desempenhos`, desempenho);
  return response.data;
}

export async function getRanking(disciplinaNome, regiaoNome) {
    const response = await axios.get(`${API_BASE}/desempenhos/ranking`, {
        params: { disciplina: disciplinaNome, regiao: regiaoNome }
    });
    return response.data;
}