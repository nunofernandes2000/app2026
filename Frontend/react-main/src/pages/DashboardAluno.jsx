import React, { useEffect, useMemo, useState } from 'react';
import ProgressBar from '../components/ProgressBar';
import api from '../api/http';

function DashboardAluno() {
    const user = useMemo(() => {
        try {
            return JSON.parse(localStorage.getItem('user') || 'null');
        } catch {
            return null;
        }
    }, []);

    const token = localStorage.getItem('token');
    const alunoId = user?.userId ?? user?.id ?? null;
    const nome = user?.nome ?? user?.email ?? 'Aluno';
    const temSessao = Boolean(token && alunoId);

    const [exercicios, setExercicios] = useState([]);
    const [resolucoes, setResolucoes] = useState([]);
    const [loading, setLoading] = useState(true);

    const carregar = async () => {
        if (!temSessao) {
            setLoading(false);
            return;
        }

        setLoading(true);
        try {
            const [ex, res] = await Promise.all([
                api.get(`/exercicios`),
                api.get(`/resolucoes/aluno/${alunoId}`),
            ]);

            setExercicios(Array.isArray(ex.data) ? ex.data : []);
            setResolucoes(Array.isArray(res.data) ? res.data : []);
        } catch (e) {
            console.error('Erro ao carregar dados do aluno:', e);
            setExercicios([]);
            setResolucoes([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        carregar();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const resolucaoPorExercicio = useMemo(() => {
        const map = new Map();
        for (const r of resolucoes) {
            const exId = r?.exercicio?.id ?? r?.exercicioId;
            if (exId != null) map.set(exId, r);
        }
        return map;
    }, [resolucoes]);

    return (
        <div style={{ padding: 32, maxWidth: 1200, margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
                <div>
                    <h1 style={{ margin: 0, color: '#2c3e50' }}>Área do Aluno 🎓</h1>
                    <p style={{ marginTop: 6, color: '#555' }}>
                        {temSessao ? <>Olá, <strong>{nome}</strong>. Seleciona um exercício para abrir.</> : 'Sem sessão ativa.'}
                    </p>
                </div>

                <button
                    onClick={() => {
                        localStorage.removeItem('user');
                        localStorage.removeItem('token');
                        window.location.href = '/login';
                    }}
                    style={{ border: '1px solid #ddd', background: 'white', borderRadius: 10, padding: '10px 12px', cursor: 'pointer' }}
                >
                    Logout
                </button>
            </div>

            {loading ? (
                <p>A carregar...</p>
            ) : !temSessao ? (
                <div style={{ color: '#666', marginTop: 14 }}>
                    Faz login para acederes aos exercícios.
                </div>
            ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: 14, marginTop: 16 }}>
                    {exercicios.map((ex) => {
                        const r = resolucaoPorExercicio.get(ex.id);
                        const total = ex.totalEtapas || 0;

                        // --- CORREÇÃO AQUI ---
                        const concluidas = r ? (r.ultimaEtapaConcluida ?? 0) : 0;
                        const skipCount = r ? (r.etapasSkip ?? 0) : 0; // Ler skips

                        // Calcular índices de skip (do fim para o início das concluídas)
                        const skippedIndices = Array.from({ length: skipCount }, (_, i) => concluidas - 1 - i).filter(
                            (idx) => idx >= 0
                        );
                        // ---------------------

                        const estado = r
                            ? (r.terminado ? '✅ Terminado' : r.solicitarAjuda ? '🚨 Pediste ajuda' : '⏳ Em curso')
                            : 'Ainda não iniciado';

                        return (
                            <div
                                key={ex.id}
                                style={{ ...card, cursor: 'pointer' }}
                                onClick={() => {
                                    window.location.href = `/aluno/exercicio/${ex.id}`;
                                }}
                                title="Clique para abrir"
                            >
                                <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
                                    <div style={{ fontWeight: 800 }}>{ex.enunciado}</div>
                                    <div style={{ fontSize: 12, color: '#666' }}>{total} etapas</div>
                                </div>

                                <div style={{ marginTop: 10 }}>
                                    <ProgressBar
                                        etapas={Array.from({ length: total }, (_, i) => `Etapa ${i + 1}`)}
                                        etapasConcluidas={Array.from({ length: concluidas }, (_, i) => i)}
                                        etapasSkip={skippedIndices} // Passar a prop aqui!
                                    />
                                </div>

                                <div style={{ marginTop: 12, fontWeight: 800, color: estado.includes('🚨') ? 'red' : '#555' }}>
                                    {estado}
                                </div>

                                {r?.nota != null && (
                                    <div style={{ marginTop: 6, fontWeight: 900 }}>
                                        Nota: {r.nota}
                                    </div>
                                )}

                                <div style={{ marginTop: 10, fontSize: 12, color: '#666' }}>
                                    Clique para abrir
                                </div>
                            </div>
                        );
                    })}

                    {exercicios.length === 0 && (
                        <div style={{ color: '#666' }}>Ainda não existem exercícios criados.</div>
                    )}
                </div>
            )}
        </div>
    );
}

const card = {
    background: 'white',
    borderRadius: 14,
    padding: 14,
    boxShadow: '0 8px 20px rgba(0,0,0,0.06)',
};

export default DashboardAluno;