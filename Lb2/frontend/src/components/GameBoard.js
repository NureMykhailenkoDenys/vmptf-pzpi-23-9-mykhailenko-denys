import React from "react";
import socket from "../socket";

const GameBoard = ({
    game,
    gameId,
    leaveGame
}) => {

    if (game.waiting) {

        return (

            <div
                style={{
                    textAlign: "center",
                    marginTop: "100px"
                }}
            >

                <h1>
                    Очікування другого гравця...
                </h1>

            </div>
        );
    }

    const handleClick = (index) => {

        const currentPlayer =
            game.players[
                game.currentPlayer
            ];

        if (
            currentPlayer.id !== socket.id
        ) {
            return;
        }

        socket.emit(
            "makeMove",
            {
                gameId,
                index
            }
        );
    };

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                marginTop: "50px"
            }}
        >

            <h1>Ігрова кімната</h1>

            <h2>
                Хід гравця:
                {" "}
                {
                    game.players[
                        game.currentPlayer
                    ]?.nickname
                }
            </h2>

            {
                game.winner && (
                    <h2>
                        Переможець:
                        {" "}
                        {
                            game.players.find(
                                (player) =>
                                    player.symbol === game.winner
                            )?.nickname
                        }
                    </h2>
                )
            }

            <div>

                {
                    [0, 1, 2].map((row) => (

                        <div
                            key={row}
                            style={{
                                display: "flex"
                            }}
                        >

                            {
                                [0, 1, 2].map((col) => {

                                    const index =
                                        row * 3 + col;

                                    return (

                                        <button
                                            key={index}
                                            onClick={() =>
                                                handleClick(index)
                                            }
                                            style={{
                                                width: "100px",
                                                height: "100px",
                                                fontSize: "40px",
                                                margin: "5px",
                                                cursor: "pointer"
                                            }}
                                        >

                                            {
                                                game.board[index]
                                            }

                                        </button>
                                    );
                                })
                            }

                        </div>
                    ))
                }

            </div>

            {
                game.winner && (

                    <button
                        onClick={leaveGame}
                        style={{
                            marginTop: "20px",
                            padding: "10px 20px"
                        }}
                    >
                        Повернутися до лобі
                    </button>
                )
            }

        </div>
    );
};

export default GameBoard;