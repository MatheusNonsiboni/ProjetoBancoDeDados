import React from "react";
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div style={styles.container}>
      <h1 style={styles.title}>SADE</h1>
      <p style={styles.subtitle}>Sistema de Acompanhamento do Desempenho Escolar</p>
      
      <div style={styles.cardContainer}>
        <div style={styles.card}>
          <h3>Área Administrativa</h3>
          <p>Acesso restrito à Secretaria de Educação</p>
          <Link to="/login-secretaria">
            <button style={styles.buttonSec}>Entrar como Secretaria</button>
          </Link>
        </div>

        <div style={styles.card}>
          <h3>Área Escolar</h3>
          <p>Acesso para Gestores e Diretores</p>
          <Link to="/login-gestor">
            <button style={styles.buttonGest}>Entrar como Gestor</button>
          </Link>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: { textAlign: "center", marginTop: "50px", fontFamily: "Arial, sans-serif" },
  title: { fontSize: "3rem", color: "#333", marginBottom: "10px" },
  subtitle: { color: "#666", marginBottom: "40px" },
  cardContainer: { display: "flex", justifyContent: "center", gap: "20px" },
  card: { border: "1px solid #ddd", padding: "20px", borderRadius: "8px", width: "250px", boxShadow: "0 2px 5px rgba(0,0,0,0.1)" },
  buttonSec: { padding: "10px 20px", marginTop: "10px", cursor: "pointer", backgroundColor: "#007bff", color: "white", border: "none", borderRadius: "4px" },
  buttonGest: { padding: "10px 20px", marginTop: "10px", cursor: "pointer", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: "4px" }
};