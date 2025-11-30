import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import LoginSecretaria from "./pages/LoginSecretaria";
import LoginGestor from "./pages/LoginGestor";
import PainelSecretaria from "./pages/PainelSecretaria";
import PainelGestor from "./pages/PainelGestor";
import Relatorios from "./pages/Relatorios";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login-secretaria" element={<LoginSecretaria />} />
        <Route path="/login-gestor" element={<LoginGestor />} />
        <Route path="/secretaria" element={<PainelSecretaria />} />
        <Route path="/gestor" element={<PainelGestor />} />
        <Route path="/relatorios" element={<Relatorios />} />

      </Routes>
    </BrowserRouter>
  );
}