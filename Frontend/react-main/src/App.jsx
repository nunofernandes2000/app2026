import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardAluno from './pages/DashboardAluno';
import DashboardDocente from './pages/DashboardDocente'; // <--- Importa o novo ficheiro
import Login from './pages/Login';
import AlunoExercicio from "./pages/AlunoExercicio";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Login */}
                <Route path="/" element={<Login />} />
                <Route path="/login" element={<Login />} />

                {/* Área do Aluno */}
                <Route path="/aluno" element={<DashboardAluno />} />

                {/* Área do Docente (NOVO) */}
                <Route path="/docente" element={<DashboardDocente />} />
                {/* Exercicio */}
                <Route path="/aluno/exercicio/:exercicioId" element={<AlunoExercicio />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;