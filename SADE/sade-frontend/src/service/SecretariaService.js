import axios from "axios";

const API_URL = "http://localhost:8080/api/secretaria";
const API_BASE = "http://localhost:8080/api";

export async function loginSecretaria(codigo) {
  const response = await axios.post(`${API_URL}/login?codigo=${codigo}`);
  return response.data;
}

export async function getRegioes() {
  const response = await axios.get(`${API_BASE}/regioes`);
  return response.data;
}

export async function criarRegiao(regiao) {
  const response = await axios.post(`${API_BASE}/regioes`, regiao);
  return response.data;
}

export async function getEscolas() {
  const response = await axios.get(`${API_BASE}/escolas`);
  return response.data;
}

export async function criarEscola(escola) {
  const response = await axios.post(`${API_BASE}/escolas`, escola);
  return response.data;
}

export async function criarGestor(gestor) {
  const response = await axios.post(`${API_BASE}/gestores`, gestor);
  return response.data;
}

export async function getRelatorioRanking(ano, tipo, regiao, disciplina) {
  const params = new URLSearchParams();
  if (ano) params.append("ano", ano);
  if (tipo) params.append("tipo", tipo);
  if (regiao) params.append("regiao", regiao);
  if (disciplina) params.append("disciplina", disciplina);

  const response = await axios.get(`${API_BASE}/relatorios/ranking`, { params });
  return response.data;
}

export async function getRelatorioRegiao(ano, disciplina) {
  const params = new URLSearchParams();
  if (ano) params.append("ano", ano);
  if (disciplina) params.append("disciplina", disciplina);
  const response = await axios.get(`${API_BASE}/relatorios/regiao`, { params });
  return response.data;
}

export async function getRelatorioEvolucao(ano, disciplina) {
  const params = new URLSearchParams();
  if (ano) params.append("ano", ano);
  if (disciplina) params.append("disciplina", disciplina);
  const response = await axios.get(`${API_BASE}/relatorios/evolucao`, { params });
  return response.data;
}

export async function getRelatorioIdeb(ano) {
  const params = new URLSearchParams();
  if (ano) params.append("ano", ano);
  const response = await axios.get(`${API_BASE}/relatorios/ideb`, { params });
  return response.data;
}