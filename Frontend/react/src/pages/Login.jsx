// src/pages/Login.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Toast from "../components/Toast";
import "./Login.css";

export default function Login() {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const [toast, setToast] = useState({ show: false, msg: "", type: "error" });

    const showToast = (msg, type = "error") => {
        setToast({ show: true, msg, type });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const res = await axios.post(
                "http://localhost:8080/api/v1/auth/login",
                { email, password }
            );

            localStorage.setItem("user", JSON.stringify(res.data));
            localStorage.setItem("token", res.data.token);

            showToast("Login efetuado com sucesso.", "success");

            setTimeout(() => {
                if (res.data.role === "DOCENTE") navigate("/docente");
                else navigate("/aluno");
            }, 400);

        } catch (err) {
            if (err.response?.status === 401) {
                showToast("Email ou palavra-passe incorretos.");
            } else {
                showToast("Erro ao ligar ao servidor.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Toast
                show={toast.show}
                message={toast.msg}
                type={toast.type}
                onClose={() => setToast(t => ({ ...t, show: false }))}
            />

            <div className="login-page">
                <div className="login-card">

                    <div className="login-header">
                        <h1>Portal Académico</h1>
                        <p>Sistema de Controlo de Exercícios por Checkpoints</p>
                    </div>

                    <form onSubmit={handleSubmit} className="login-form">

                        <div className="input-group">
                            <label>Email institucional</label>
                            <input
                                type="email"
                                placeholder="ex: aluno1@estg.pt"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                                required
                            />
                        </div>

                        <div className="input-group">
                            <label>Palavra-passe</label>

                            <div className="password-wrapper">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    placeholder="********"
                                    value={password}
                                    onChange={e => setPassword(e.target.value)}
                                    required
                                />
                                <button
                                    type="button"
                                    className="toggle-password"
                                    onClick={() => setShowPassword(v => !v)}
                                >
                                    {showPassword ? "Ocultar" : "Mostrar"}
                                </button>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="login-btn"
                            disabled={loading}
                        >
                            {loading ? "A entrar..." : "Entrar"}
                        </button>

                    </form>

                    <div className="login-footer">
                        <span>Engenharia de Software — ESTG</span>
                    </div>

                </div>
            </div>
        </>
    );
}
