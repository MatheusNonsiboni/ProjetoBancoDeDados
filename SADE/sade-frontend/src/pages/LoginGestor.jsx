import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginGestor } from "../service/GestorService";

export default function LoginGestor() {
  const navigate = useNavigate();
  const [codigo, setCodigo] = useState("");
  const [erro, setErro] = useState("");

  async function handleLogin(e) {
    e.preventDefault();
    setErro("");

    try {
      const gestorEncontrado = await loginGestor(codigo);

      if (gestorEncontrado) {
        localStorage.setItem("usuario_gestor", JSON.stringify(gestorEncontrado));
        
        navigate("/gestor");
      } else {
        setErro("Código não encontrado.");
      }
    } catch (error) {
      setErro("Código inválido ou erro no sistema.");
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.formBox}>
        <h2>Área do Gestor</h2>
        <p>Informe seu código único</p>

        <form onSubmit={handleLogin}>
          <input
            type="password"
            placeholder="Código do Gestor"
            value={codigo}
            onChange={(e) => setCodigo(e.target.value)}
            style={styles.input}
            required
          />
          <button type="submit" style={styles.button}>Entrar</button>
        </form>

        {erro && <p style={{ color: "red", marginTop: "10px" }}>{erro}</p>}
        
        <button onClick={() => navigate("/")} style={styles.backButton}>Voltar</button>
      </div>
    </div>
  );
}

const styles = {
  container: { display: "flex", justifyContent: "center", marginTop: "100px", fontFamily: "Arial" },
  formBox: { border: "1px solid #ccc", padding: "30px", borderRadius: "10px", textAlign: "center", width: "300px", borderColor: "#28a745" },
  input: { width: "90%", padding: "10px", marginBottom: "10px", fontSize: "16px" },
  button: { width: "100%", padding: "10px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: "5px", cursor: "pointer" },
  backButton: { marginTop: "15px", background: "none", border: "none", color: "#666", cursor: "pointer", textDecoration: "underline" }
};