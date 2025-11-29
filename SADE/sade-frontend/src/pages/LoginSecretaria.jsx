import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginSecretaria } from "../service/SecretariaService";

export default function LoginSecretaria() {
  const navigate = useNavigate();
  const [codigo, setCodigo] = useState("");
  const [erro, setErro] = useState("");

  async function handleLogin(e) {
    e.preventDefault();
    setErro("");

    try {
      const logado = await loginSecretaria(codigo);
      
      if (logado) {
        navigate("/secretaria");
      } else {
        setErro("Código de acesso incorreto.");
      }
    } catch (error) {
      console.error(error);
      setErro("Erro de conexão com o servidor.");
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.formBox}>
        <h2>Login Secretaria</h2>
        <p>Informe o código administrativo</p>
        
        <form onSubmit={handleLogin}>
          <input
            type="password"
            placeholder="Código de Acesso"
            value={codigo}
            onChange={(e) => setCodigo(e.target.value)}
            style={styles.input}
            required
          />
          <button type="submit" style={styles.button}>Acessar Painel</button>
        </form>

        {erro && <p style={{ color: "red", marginTop: "10px" }}>{erro}</p>}
        
        <button onClick={() => navigate("/")} style={styles.backButton}>Voltar</button>
      </div>
    </div>
  );
}

const styles = {
  container: { display: "flex", justifyContent: "center", marginTop: "100px", fontFamily: "Arial" },
  formBox: { border: "1px solid #ccc", padding: "30px", borderRadius: "10px", textAlign: "center", width: "300px" },
  input: { width: "90%", padding: "10px", marginBottom: "10px", fontSize: "16px" },
  button: { width: "100%", padding: "10px", backgroundColor: "#007bff", color: "white", border: "none", borderRadius: "5px", cursor: "pointer" },
  backButton: { marginTop: "15px", background: "none", border: "none", color: "#666", cursor: "pointer", textDecoration: "underline" }
};