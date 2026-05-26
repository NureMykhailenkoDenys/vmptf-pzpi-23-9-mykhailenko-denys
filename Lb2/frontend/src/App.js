import React, { useEffect, useState } from "react";

import socket from "./socket";

import Login from "./components/Login";
import Lobby from "./components/Lobby";
import GameBoard from "./components/GameBoard";

function App() {

    const [nickname, setNickname] =
        useState("");

    const [games, setGames] =
        useState([]);

    const [leaderboard, setLeaderboard] =
        useState({});

    const [gameId, setGameId] =
        useState(null);

    const [game, setGame] =
        useState(null);

    useEffect(() => {

        socket.on(
            "gameCreated",
            ({ gameId }) => {

                setGameId(gameId);

                setGame({
                    waiting: true
                });
            }
        );

        socket.on(
            "gamesList",
            (gamesList) => {

                setGames(gamesList);
            }
        );

        socket.on(
            "startGame",
            ({ game }) => {

                setGame(game);
            }
        );

        socket.on(
            "updateGame",
            ({
                game,
                leaderboard
            }) => {

                setGame(game);

                setLeaderboard(
                    leaderboard
                );
            }
        );

        socket.on(
            "updateLeaderboard",
            (leaderboardData) => {

                setLeaderboard(
                    leaderboardData
                );
            }
        );

        return () => {

            socket.off("gameCreated");
            socket.off("gamesList");
            socket.off("startGame");
            socket.off("updateGame");
            socket.off("updateLeaderboard");
        };

    }, []);

    const leaveGame = () => {

        if (gameId) {

            socket.emit(
                "leaveGame",
                { gameId }
            );
        }

        setGame(null);

        setGameId(null);
    };

    if (!nickname) {

        return (
            <Login
                setNickname={
                    setNickname
                }
            />
        );
    }

    if (game) {

        return (
            <GameBoard
                game={game}
                gameId={gameId}
                leaveGame={leaveGame}
            />
        );
    }

    return (
        <Lobby
            nickname={nickname}
            games={games}
            leaderboard={leaderboard}
            setGameId={setGameId}
        />
    );
}

export default App;