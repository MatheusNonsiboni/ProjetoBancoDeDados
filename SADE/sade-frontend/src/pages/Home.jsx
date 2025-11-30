import React from "react";
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div style={styles.container}>
      <h1 style={styles.title}>SADE</h1>
      <p style={styles.subtitle}>Sistema de Avaliação do Desempenho Escolar</p>
      
      <div style={styles.cardContainer}>
        
        <div style={styles.card}>
          <h3 style={{color: "#007bff"}}>Área Administrativa</h3>
          <p>Cadastro de Escolas, Regiões e Gestores.</p>
          <Link to="/login-secretaria">
            <button style={{...styles.button, backgroundColor: "#007bff"}}>Login Secretaria</button>
          </Link>
        </div>

        <div style={styles.card}>
          <h3 style={{color: "#28a745"}}>Área Escolar</h3>
          <p>Lançamento de Notas, Frequência e Indicadores.</p>
          <Link to="/login-gestor">
            <button style={{...styles.button, backgroundColor: "#28a745"}}>Login Gestor</button>
          </Link>
        </div>

        <div style={{...styles.card, border: "2px solid #ffc107"}}>
          <h3 style={{color: "#d39e00"}}>Transparência</h3>
          <p>Consulta pública de rankings e evolução educacional.</p>
          <Link to="/relatorios">
            <button style={{...styles.button, backgroundColor: "#ffc107", color: "black", fontWeight: "bold"}}>
              Ver Relatórios
            </button>
          </Link>
        </div>

      </div>
    </div>
  );
}

const styles = {
  container: { textAlign: "center", marginTop: "50px", fontFamily: "Arial, sans-serif", padding: "20px" },
  title: { fontSize: "3rem", color: "#333", marginBottom: "10px" },
  subtitle: { color: "#666", marginBottom: "50px" },
  cardContainer: { display: "flex", justifyContent: "center", gap: "20px", flexWrap: "wrap" },
  card: { border: "1px solid #ddd", padding: "20px", borderRadius: "8px", width: "250px", boxShadow: "0 4px 8px rgba(0,0,0,0.1)", backgroundColor: "white" },
  button: { padding: "10px 20px", marginTop: "15px", cursor: "pointer", color: "white", border: "none", borderRadius: "4px", width: "100%" }
};