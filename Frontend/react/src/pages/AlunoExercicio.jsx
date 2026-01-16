import React, { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api/http";
import ProgressBar from "../components/ProgressBar";

export default function AlunoExercicio() {
    const { exercicioId } = useParams();
    const nav = useNavigate();

    const user = useMemo(() => {
        try {
            return JSON.parse(localStorage.getItem("user") || "null");
        } catch {
            return null;
        }
    }, []);

    const alunoId = user?.userId ?? user?.id ?? null;
    const token = localStorage.getItem("token");
    const temSessao = Boolean(token && alunoId);

    const [ex, setEx] = useState(null);
    const [r, setR] = useState(null);
    const [loading, setLoading] = useState(true);

    const carregar = async () => {
        if (!temSessao) {
            setLoading(false);
            return;
        }

        setLoading(true);
        try {
            const [exResp, resResp] = await Promise.all([
                api.get(`/exercicios`), // lista
                api.get(`/resolucoes/aluno/${alunoId}`),
            ]);

            const exercicios = Array.isArray(exResp.data) ? exResp.data : [];
            const resolucoes = Array.isArray(resResp.data) ? resResp.data : [];

            const exIdNum = Number(exercicioId);
            const exercicio = exercicios.find((e) => e.id === exIdNum) || null;

            const resolucao =
                resolucoes.find((x) => (x?.exercicio?.id ?? x?.exercicioId) === exIdNum) || null;

            setEx(exercicio);
            setR(resolucao);
        } catch (e) {
            console.error("Erro ao carregar exercício:", e);
            setEx(null);
            setR(null);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        carregar();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [exercicioId]);

    const iniciar = async () => {
        try {
            await api.post(`/resolucoes/iniciar?alunoId=${alunoId}&exercicioId=${exercicioId}`);
            await carregar();
        } catch (e) {
            console.error("Erro ao iniciar:", e);
            alert("Não foi possível iniciar o exercício.");
        }
    };

    const avancar = async () => {
        try {
            if (!r?.id) return;
            await api.post(`/resolucoes/${r.id}/avancar`);
            await carregar();
        } catch (e) {
            console.error("Erro ao avançar:", e);
            alert("Não foi possível avançar.");
        }
    };

    const skip = async () => {
        try {
            if (!r?.id) return;
            await api.put(`/resolucoes/${r.id}/skip`);
            await carregar();
        } catch (e) {
            console.error("Erro ao fazer skip:", e);
            alert("Não foi possível fazer skip.");
        }
    };

    const pedirAjuda = async () => {
        try {
            if (!r?.id) return;
            await api.post(`/resolucoes/${r.id}/ajuda`);
            await carregar();
        } catch (e) {
            console.error("Erro ao pedir ajuda:", e);
            alert("Não foi possível pedir ajuda.");
        }
    };

    if (!temSessao) {
        return (
            <div style={wrap}>
                <div style={card}>
                    <h2 style={{ marginTop: 0 }}>Sem sessão ativa</h2>
                    <button style={btnPrimary} onClick={() => nav("/login")}>
                        Ir para login
                    </button>
                </div>
            </div>
        );
    }

    if (loading) {
        return (
            <div style={wrap}>
                <div style={card}>A carregar…</div>
            </div>
        );
    }

    if (!ex) {
        return (
            <div style={wrap}>
                <div style={card}>
                    <div style={{ fontWeight: 900, marginBottom: 10 }}>Exercício não encontrado.</div>
                    <button style={btnGhost} onClick={() => nav("/aluno")}>
                        Voltar
                    </button>
                </div>
            </div>
        );
    }

    const total = ex.totalEtapas || 0;
    const concluidas = r ? (r.ultimaEtapaConcluida ?? 0) : 0;
    const skipCount = r ? (r.etapasSkip ?? 0) : 0;

    // ✅ marca as últimas skipCount etapas (aproximação sem guardar lista na BD)
    const skippedIndices = Array.from({ length: skipCount }, (_, i) => concluidas - 1 - i).filter(
        (idx) => idx >= 0
    );

    const percent = total > 0 ? Math.min(100, Math.round((concluidas / total) * 100)) : 0;

    const terminado = Boolean(r?.terminado);

    return (
        <div style={wrap}>
            <div style={cardWide}>
                <div style={topRow}>
                    <button style={btnGhost} onClick={() => nav("/aluno")}>
                        ← Voltar
                    </button>

                    <button
                        style={btnGhost}
                        onClick={() => {
                            localStorage.removeItem("user");
                            localStorage.removeItem("token");
                            nav("/login");
                        }}
                    >
                        Logout
                    </button>
                </div>

                <h1 style={{ margin: "8px 0 6px" }}>{ex.enunciado}</h1>

                <div style={{ color: "#666", fontWeight: 700 }}>
                    {!r ? "Ainda não iniciado" : terminado ? "✅ Terminado" : "⏳ Em curso"}
                    {r?.solicitarAjuda ? <span style={{ color: "red" }}> • 🚨 Pediste ajuda</span> : null}
                </div>

                {skipCount > 0 && (
                    <div style={{ marginTop: 6, color: "#b45309", fontWeight: 900 }}>
                        Etapas saltadas: {skipCount}
                    </div>
                )}

                {/* Barra de progresso */}
                <div style={{ marginTop: 16 }}>
                    <div style={{ display: "flex", justifyContent: "space-between", fontWeight: 800 }}>
                        <span>Progresso</span>
                        <span>{percent}%</span>
                    </div>
                    <div style={progressOuter}>
                        <div style={{ ...progressInner, width: `${percent}%` }} />
                    </div>
                </div>

                {/* Bolinhas (com X para skip) */}
                <div style={{ marginTop: 16 }}>
                    <ProgressBar
                        etapas={Array.from({ length: total }, (_, i) => `Etapa ${i + 1}`)}
                        etapasConcluidas={Array.from({ length: concluidas }, (_, i) => i)}
                        etapasSkip={skippedIndices}
                    />
                </div>

                {/* Nota */}
                {terminado && (
                    <div style={notaBox}>
                        <div style={{ fontWeight: 900 }}>Nota final</div>
                        <div style={{ fontSize: 28, fontWeight: 1000 }}>{r?.nota ?? "-"}</div>
                    </div>
                )}

                {/* Botões */}
                <div style={{ marginTop: 18 }}>
                    {!r ? (
                        <button style={btnPrimary} onClick={iniciar}>
                            Iniciar exercício
                        </button>
                    ) : (
                        <div style={btnRow}>
                            <button
                                style={{ ...btnPrimary, opacity: terminado ? 0.55 : 1 }}
                                disabled={terminado}
                                onClick={avancar}
                            >
                                Marcar etapa
                            </button>

                            <button
                                style={{ ...btnWarn, opacity: terminado ? 0.55 : 1 }}
                                disabled={terminado}
                                onClick={skip}
                            >
                                Skip etapa
                            </button>

                            <button
                                style={{ ...btnSecondary, opacity: terminado ? 0.55 : 1 }}
                                disabled={terminado}
                                onClick={pedirAjuda}
                            >
                                Chamar docente
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

const wrap = {
    minHeight: "100vh",
    display: "grid",
    placeItems: "center",
    padding: 18,
    background: "#f5f7fb",
    fontFamily: "Arial, sans-serif",
};

const card = {
    background: "white",
    padding: 16,
    borderRadius: 14,
    width: "min(520px, 100%)",
    boxShadow: "0 10px 30px rgba(0,0,0,.08)",
};

const cardWide = { ...card, width: "min(1100px, 100%)" };

const topRow = { display: "flex", justifyContent: "space-between", gap: 10 };

const btnRow = { display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 10, marginTop: 10 };

const btnPrimary = {
    width: "100%",
    border: "none",
    background: "#2d6cdf",
    color: "white",
    fontWeight: 900,
    padding: "14px 12px",
    borderRadius: 14,
    cursor: "pointer",
};

const btnSecondary = {
    width: "100%",
    border: "1px solid #ddd",
    background: "white",
    color: "#111",
    fontWeight: 900,
    padding: "14px 12px",
    borderRadius: 14,
    cursor: "pointer",
};

const btnWarn = {
    width: "100%",
    border: "none",
    background: "#f59e0b",
    color: "#111",
    fontWeight: 900,
    padding: "14px 12px",
    borderRadius: 14,
    cursor: "pointer",
};

const btnGhost = {
    border: "1px solid #ddd",
    background: "white",
    borderRadius: 14,
    padding: "10px 12px",
    cursor: "pointer",
    fontWeight: 900,
};

const progressOuter = {
    marginTop: 8,
    height: 12,
    borderRadius: 999,
    background: "rgba(45,108,223,.12)",
    overflow: "hidden",
};

const progressInner = {
    height: "100%",
    borderRadius: 999,
    background: "#2d6cdf",
};

const notaBox = {
    marginTop: 16,
    padding: 14,
    borderRadius: 14,
    background: "rgba(11,122,42,.10)",
    border: "1px solid rgba(11,122,42,.25)",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
};
