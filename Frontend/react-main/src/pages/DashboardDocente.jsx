import React, { useEffect, useState } from 'react';
import api from '../api/http';
import ProgressBar from '../components/ProgressBar';

// baseURL definido em src/api/http.js

function DashboardDocente() {
    const [exercicios, setExercicios] = useState([]);
    const [unidades, setUnidades] = useState([]);
    const [exercicioId, setExercicioId] = useState('');
    const [resolucoes, setResolucoes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [notasTemp, setNotasTemp] = useState({});

    useEffect(() => {
        (async () => {
            try {
                const [ex, ucs] = await Promise.all([
                    api.get(`/exercicios`),
                    api.get(`/unidades`)
                ]);
                setExercicios(ex.data);
                setUnidades(ucs.data);
            } catch (e) {
                console.error('Erro ao carregar exercícios:', e);
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    const [novo, setNovo] = useState({ unidadeCurricularId: '', enunciado: '', totalEtapas: 4 });

    const criarExercicio = async (e) => {
        e.preventDefault();
        if (!novo.unidadeCurricularId || !novo.enunciado || !novo.totalEtapas) {
            alert('Preenche a UC, enunciado e total de etapas.');
            return;
        }
        try {
            await api.post(`/exercicios`, {
                unidadeCurricularId: Number(novo.unidadeCurricularId),
                enunciado: novo.enunciado,
                totalEtapas: Number(novo.totalEtapas),
            });
            const ex = await api.get(`/exercicios`);
            setExercicios(ex.data);
            setNovo({ unidadeCurricularId: '', enunciado: '', totalEtapas: 4 });
            alert('Exercício criado ✅');
        } catch (err) {
            console.error(err);
            alert('Erro ao criar exercício');
        }
    };

    const carregarResolucoes = async (id) => {
        setLoading(true);
        try {
            if (!id) {
                const all = await api.get(`/resolucoes`);
                setResolucoes(all.data);
            } else {
                const res = await api.get(`/exercicios/${id}/resolucoes`);
                setResolucoes(res.data);
            }
        } catch (error) {
            console.error('Erro ao carregar resoluções:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        carregarResolucoes(exercicioId);
    }, [exercicioId]);

    const lancarNota = async (resolucaoId) => {
        const notaValor = notasTemp[resolucaoId];
        if (notaValor === undefined || notaValor === '' || isNaN(notaValor)) {
            alert('Por favor insere uma nota válida.');
            return;
        }

        try {
            await api.post(`/resolucoes/${resolucaoId}/avaliar?nota=${notaValor}`);
            alert('Nota lançada com sucesso! 🎓');
            await carregarResolucoes(exercicioId);
        } catch (error) {
            console.error('Erro ao avaliar:', error);
            alert('Erro ao lançar nota.');
        }
    };

    const handleNotaChange = (id, valor) => {
        setNotasTemp((prev) => ({ ...prev, [id]: valor }));
    };

    return (
        <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 12, flexWrap: 'wrap' }}>
                <div>
                    <h1 style={{ color: '#2c3e50', marginBottom: 6 }}>Painel de Controlo do Docente 👨‍🏫</h1>
                    <p style={{ marginTop: 0, color: '#555' }}>Progresso por exercício e pedidos de ajuda.</p>
                </div>

                <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
                    <label style={{ fontWeight: 'bold' }}>Exercício:</label>
                    <select
                        value={exercicioId}
                        onChange={(e) => setExercicioId(e.target.value)}
                        style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid #ddd', minWidth: 260 }}
                    >
                        <option value="">(Todos)</option>
                        {exercicios.map((ex) => (
                            <option key={ex.id} value={ex.id}>
                                {ex.enunciado}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            <div style={{ marginTop: 18, background: 'white', padding: 14, borderRadius: 10, boxShadow: '0 2px 10px rgba(0,0,0,0.06)' }}>
                <h3 style={{ marginTop: 0 }}>Criar novo exercício</h3>
                <form onSubmit={criarExercicio} style={{ display: 'grid', gridTemplateColumns: '1fr 2fr 1fr auto', gap: 10, alignItems: 'end' }}>
                    <div>
                        <label style={{ display: 'block', fontWeight: 700, marginBottom: 6 }}>Unidade Curricular</label>
                        <select
                            value={novo.unidadeCurricularId}
                            onChange={(e) => setNovo((p) => ({ ...p, unidadeCurricularId: e.target.value }))}
                            style={{ width: '100%', padding: '8px 10px', borderRadius: 8, border: '1px solid #ddd' }}
                        >
                            <option value="">-- escolher --</option>
                            {unidades.map((u) => (
                                <option key={u.id} value={u.id}>{u.nome}</option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label style={{ display: 'block', fontWeight: 700, marginBottom: 6 }}>Enunciado</label>
                        <input
                            value={novo.enunciado}
                            onChange={(e) => setNovo((p) => ({ ...p, enunciado: e.target.value }))}
                            placeholder="Ex: Implementar API REST..."
                            style={{ width: '100%', padding: '8px 10px', borderRadius: 8, border: '1px solid #ddd' }}
                        />
                    </div>

                    <div>
                        <label style={{ display: 'block', fontWeight: 700, marginBottom: 6 }}>Total de etapas</label>
                        <input
                            type="number"
                            min="1"
                            value={novo.totalEtapas}
                            onChange={(e) => setNovo((p) => ({ ...p, totalEtapas: e.target.value }))}
                            style={{ width: '100%', padding: '8px 10px', borderRadius: 8, border: '1px solid #ddd' }}
                        />
                    </div>

                    <button
                        type="submit"
                        style={{ background: '#2d6cdf', color: 'white', border: 'none', padding: '9px 14px', borderRadius: 8, cursor: 'pointer', fontWeight: 800 }}
                    >
                        Criar
                    </button>
                </form>
            </div>

            {loading ? (
                <p>A carregar dados...</p>
            ) : (
                <table
                    style={{
                        width: '100%',
                        borderCollapse: 'collapse',
                        marginTop: '20px',
                        boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
                        background: 'white',
                        borderRadius: 10,
                        overflow: 'hidden'
                    }}
                >
                    <thead>
                    <tr style={{ backgroundColor: '#f4f6f8', textAlign: 'left' }}>
                        <th style={styles.th}>Aluno</th>
                        <th style={styles.th}>Exercício</th>
                        <th style={styles.th}>Progresso</th>
                        <th style={styles.th}>Estado</th>
                        <th style={styles.th}>Avaliação</th>
                    </tr>
                    </thead>
                    <tbody>
                    {resolucoes.map((res) => {
                        // --- CÁLCULO DAS ETAPAS SALTADAS ---
                        const total = res.exercicio?.totalEtapas || 0;
                        const concluidas = res.ultimaEtapaConcluida || 0;
                        const skipCount = res.etapasSkip || 0;

                        const skippedIndices = Array.from({ length: skipCount }, (_, i) => concluidas - 1 - i).filter(
                            (idx) => idx >= 0
                        );
                        // -----------------------------------

                        return (
                            <tr key={res.id} style={{ borderBottom: '1px solid #eee' }}>
                                <td style={styles.td}>
                                    <strong>{res.aluno ? res.aluno.nome : 'Desconhecido'}</strong>
                                    <br />
                                    <span style={{ fontSize: '0.8em', color: '#666' }}>{res.aluno?.email}</span>
                                </td>

                                <td style={styles.td}>{res.exercicio?.enunciado}</td>

                                <td style={styles.td}>
                                    <div style={{ transform: 'scale(0.8)', transformOrigin: 'left' }}>
                                        <ProgressBar
                                            etapas={Array.from({ length: total }, (_, i) => `Etapa ${i + 1}`)}
                                            etapasConcluidas={Array.from({ length: concluidas }, (_, i) => i)}
                                            etapasSkip={skippedIndices} // <--- Passar aqui
                                        />
                                    </div>
                                </td>

                                <td style={styles.td}>
                                    {res.solicitarAjuda ? (
                                        <span style={{ color: 'red', fontWeight: 'bold' }}>🚨 Pediu Ajuda</span>
                                    ) : res.terminado ? (
                                        <span style={{ color: 'green' }}>✅ Terminado</span>
                                    ) : (
                                        <span style={{ color: '#888' }}>⏳ Em curso</span>
                                    )}
                                </td>

                                <td style={styles.td}>
                                    {res.nota !== null && res.nota !== undefined ? (
                                        <span style={{ fontWeight: 'bold', fontSize: '1.1em' }}>{res.nota} val.</span>
                                    ) : (
                                        <div style={{ display: 'flex', gap: '5px' }}>
                                            <input
                                                type="number"
                                                placeholder="0-20"
                                                style={{ width: '70px', padding: '6px', borderRadius: 6, border: '1px solid #ddd' }}
                                                value={notasTemp[res.id] || ''}
                                                onChange={(e) => handleNotaChange(res.id, e.target.value)}
                                            />
                                            <button onClick={() => lancarNota(res.id)} style={styles.btnAvaliar}>
                                                Lançar
                                            </button>
                                        </div>
                                    )}
                                </td>
                            </tr>
                        );
                    })}
                    {resolucoes.length === 0 && (
                        <tr>
                            <td colSpan={5} style={{ padding: 16, color: '#666' }}>
                                Ainda não há resoluções.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            )}
        </div>
    );
}

const styles = {
    th: { padding: '15px', color: '#555' },
    td: { padding: '15px', verticalAlign: 'middle' },
    btnAvaliar: {
        backgroundColor: '#2ecc71',
        color: 'white',
        border: 'none',
        padding: '6px 10px',
        borderRadius: '6px',
        cursor: 'pointer',
        fontWeight: 'bold'
    }
};

export default DashboardDocente;