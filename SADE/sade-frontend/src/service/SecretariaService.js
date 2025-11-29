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

export async function getRankingIdeb() {
    const response = await axios.get(`${API_BASE}/indicadores/ranking/ideb`);
    return response.data;
}