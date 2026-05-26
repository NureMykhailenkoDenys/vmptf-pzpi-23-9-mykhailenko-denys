import React, { useState } from "react";

const Login = ({ setNickname }) => {

    const [name, setName] = useState("");

    const handleSubmit = () => {

        if (!name.trim()) return;

        setNickname(name);
    };

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                marginTop: "200px"
            }}
        >

            <h1>Хрестики-нулики онлайн</h1>

            <input
                type="text"
                placeholder="Введіть нікнейм"
                value={name}
                onChange={(e) =>
                    setName(e.target.value)
                }
                style={{
                    padding: "10px",
                    fontSize: "18px"
                }}
            />

            <button
                onClick={handleSubmit}
                style={{
                    marginTop: "20px",
                    padding: "10px 20px"
                }}
            >
                Увійти
            </button>

        </div>
    );
};

export default Login;