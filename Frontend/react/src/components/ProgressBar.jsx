import React from "react";

export default function ProgressBar({ etapas = [], etapasConcluidas = [], etapasSkip = [] }) {
    const concluidasSet = new Set(etapasConcluidas);
    const skipSet = new Set(etapasSkip);

    return (
        <div style={wrap}>
            <div style={rail} />

            {etapas.map((label, i) => {
                const isDone = concluidasSet.has(i);
                const isSkip = skipSet.has(i);

                const bg = isSkip ? "#ef4444" : isDone ? "#34a853" : "#e5e7eb";
                const fg = isSkip || isDone ? "white" : "#111";
                const border = isSkip || isDone ? "none" : "2px solid #d1d5db";

                return (
                    <div key={i} style={step}>
                        <div style={{ ...dot, background: bg, color: fg, border }} title={isSkip ? "Etapa saltada" : isDone ? "Etapa concluída" : "Por fazer"}>
                            {isSkip ? "✕" : isDone ? "✓" : i + 1}
                        </div>
                        <div style={labelStyle}>{label}</div>
                    </div>
                );
            })}
        </div>
    );
}

const wrap = {
    position: "relative",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-start",
    gap: 10,
    marginTop: 8,
    padding: "0 6px",
};

const rail = {
    position: "absolute",
    top: 22,            // metade da bolinha (44px)
    left: 28,           // dentro da primeira bolinha
    right: 28,          // dentro da última bolinha
    height: 4,
    borderRadius: 999,
    background: "#d9d9d9",
    zIndex: 0,
};

const step = {
    position: "relative",
    zIndex: 1,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    minWidth: 70,
    flex: 1,
};

const dot = {
    width: 44,
    height: 44,
    borderRadius: 999,
    display: "grid",
    placeItems: "center",
    fontWeight: 900,
    boxShadow: "0 10px 18px rgba(0,0,0,.08)",
};

const labelStyle = {
    marginTop: 8,
    fontSize: 12,
    fontWeight: 800,
    color: "#444",
    textAlign: "center",
    lineHeight: "14px",
};
