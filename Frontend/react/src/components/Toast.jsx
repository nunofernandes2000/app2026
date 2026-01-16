import React, { useEffect } from "react";

function Toast({ show, message, type = "error", onClose, duration = 3000 }) {
  useEffect(() => {
    if (!show) return;
    const t = setTimeout(() => onClose?.(), duration);
    return () => clearTimeout(t);
  }, [show, duration, onClose]);

  if (!show) return null;

  const leftColor = type === "success" ? "#0b7a2a" : "#b00020";
  const title = type === "success" ? "Sucesso" : "Erro";

  return (
    <div style={wrap}>
      <div style={{ ...toast, borderLeft: `6px solid ${leftColor}` }}>
        <div style={titleStyle}>{title}</div>
        <div style={msgStyle}>{message}</div>

        <button onClick={onClose} style={closeBtn} aria-label="Fechar">
          ✕
        </button>
      </div>
    </div>
  );
}

const wrap = {
  position: "fixed",
  top: 14,
  left: 0,
  right: 0,
  display: "grid",
  placeItems: "center",
  zIndex: 9999,
  pointerEvents: "none",
};

const toast = {
  width: "min(520px, calc(100% - 24px))",
  background: "white",
  borderRadius: 14,
  padding: "12px 14px",
  boxShadow: "0 10px 30px rgba(0,0,0,.12)",
  position: "relative",
  pointerEvents: "auto",
};

const titleStyle = { fontWeight: 900, marginBottom: 4 };
const msgStyle = { color: "#333" };

const closeBtn = {
  position: "absolute",
  top: 8,
  right: 10,
  border: "none",
  background: "transparent",
  cursor: "pointer",
  fontSize: 16,
  opacity: 0.7,
};

export default Toast;
