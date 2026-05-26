import React from "react";
import socket from "../socket";
import Leaderboard from "./Leaderboard";

const Lobby = ({
    nickname,
    games,
    leaderboard,
    setGameId
}) => {

    const createGame = () => {

        socket.emit(
            "createGame",
            { nickname }
        );
    };

    const joinGame = (gameId) => {

        socket.emit(
            "joinGame",
            {
                gameId,
                nickname
            }
        );

        setGameId(gameId);
    };

    return (
        <div
            style={{
                textAlign: "center",
                marginTop: "50px"
            }}
        >

            <h1>Лобі</h1>

            <button
                onClick={createGame}
                style={{
                    padding: "10px 20px",
                    marginBottom: "30px"
                }}
            >
                Створити гру
            </button>

            <h2>Активні ігри</h2>

            {
                games.length === 0 && (
                    <p>
                        Немає активних ігор
                    </p>
                )
            }

            {
                games.map((game) => (

                    <div
                        key={game.id}
                        style={{
                            marginBottom: "15px"
                        }}
                    >

                        <span>
                            {game.name}
                            {" | "}
                            Гравців:
                            {" "}
                            {game.players}/2
                        </span>

                        {
                            game.players < 2 && (

                                <button
                                    onClick={() =>
                                        joinGame(game.id)
                                    }
                                    style={{
                                        marginLeft: "10px"
                                    }}
                                >
                                    Приєднатися
                                </button>
                            )
                        }

                    </div>
                ))
            }

            <Leaderboard
                leaderboard={leaderboard}
            />

        </div>
    );
};

export default Lobby;