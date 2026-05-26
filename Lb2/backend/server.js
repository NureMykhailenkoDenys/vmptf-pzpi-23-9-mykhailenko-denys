const express = require("express");
const cors = require("cors");
const http = require("http");
const { Server } = require("socket.io");
const fs = require("fs");

const app = express();

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: "*"
    }
});

app.use(cors());
app.use(express.json());

let games = {};

let leaderboard = {};

if (
    fs.existsSync("leaderboard.json")
) {

    const data =
        fs.readFileSync(
            "leaderboard.json",
            "utf8"
        );

    leaderboard =
        JSON.parse(data || "{}");
}

const winningCombinations = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],

    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],

    [0, 4, 8],
    [2, 4, 6]
];

function checkWinner(board) {

    for (let combination of winningCombinations) {

        const [a, b, c] = combination;

        if (
            board[a] &&
            board[a] === board[b] &&
            board[a] === board[c]
        ) {

            return board[a];
        }
    }

    return null;
}

function sendGamesList() {

    const gamesList =
        Object.entries(games)

            .filter(
                ([_, game]) =>
                    game.players.length < 2
            )

            .map(
                ([id, game]) => ({

                    id,

                    name:
                        `Гра: ${game.players[0]?.nickname}`,

                    players:
                        game.players.length
                })
            );

    io.emit(
        "gamesList",
        gamesList
    );
}

io.on("connection", (socket) => {

    console.log(
        "Користувач підключився:",
        socket.id
    );

    socket.emit(
        "updateLeaderboard",
        leaderboard
    );

    sendGamesList();
    
    socket.on(
        "createGame",
        ({ nickname }) => {

            const gameId =
                Math.random()
                    .toString(36)
                    .substring(2, 7);

            games[gameId] = {

                board: [
                    "", "", "",
                    "", "", "",
                    "", "", ""
                ],

                players: [
                    {
                        id: socket.id,
                        nickname,
                        symbol: "X"
                    }
                ],

                currentPlayer: 0,

                winner: null
            };

            socket.join(gameId);

            socket.emit(
                "gameCreated",
                { gameId }
            );

            sendGamesList();

            console.log(
                `Гру ${gameId} створено`
            );
        }
    );

    socket.on(
        "joinGame",
        ({
            gameId,
            nickname
        }) => {

            const game =
                games[gameId];

            if (!game) {

                return socket.emit(
                    "errorMessage",
                    "Гру не знайдено"
                );
            }

            if (
                game.players.length >= 2
            ) {

                return socket.emit(
                    "errorMessage",
                    "Гра заповнена"
                );
            }

            game.players.push({
                id: socket.id,
                nickname,
                symbol: "O"
            });

            socket.join(gameId);

            game.id = gameId;

            io.to(gameId).emit(
                "startGame",
                {
                    game,
                    gameId
                }
            );

            io.to(gameId).emit(
                "updateGame",
                {
                    game,
                    leaderboard
                }
            );

            sendGamesList();

            console.log(
                `${nickname} приєднався до ${gameId}`
            );
        }
    );

    socket.on(
        "makeMove",
        ({
            gameId,
            index
        }) => {

            const game =
                games[gameId];

            if (!game) return;

            if (game.winner) return;

            const currentPlayer =
                game.players[
                    game.currentPlayer
                ];

            if (
                currentPlayer.id !== socket.id
            ) {
                return;
            }

            if (
                game.board[index] !== ""
            ) {
                return;
            }

            game.board[index] =
                currentPlayer.symbol;

            const winner =
                checkWinner(game.board);

            if (winner) {

                game.winner = winner;

                const winnerPlayer =
                    game.players.find(
                        (player) =>
                            player.symbol === winner
                    );

                if (winnerPlayer) {

                    const nickname =
                        winnerPlayer.nickname;

                    leaderboard[nickname] =
                        (leaderboard[nickname] || 0) + 1;
                        fs.writeFileSync(
                        "leaderboard.json",
                        JSON.stringify(
                            leaderboard,
                            null,
                            2
                        )
                    );
                }

            } else {

                game.currentPlayer =
                    game.currentPlayer === 0
                        ? 1
                        : 0;
            }

            io.to(gameId).emit(
                "updateGame",
                {
                    game,
                    leaderboard
                }
            );

            io.emit(
                "updateLeaderboard",
                leaderboard
            );
        }
    );

    socket.on(
        "resetGame",
        ({ gameId }) => {

            const game =
                games[gameId];

            if (!game) return;

            game.board = [
                "", "", "",
                "", "", "",
                "", "", ""
            ];

            game.currentPlayer = 0;

            game.winner = null;

            io.to(gameId).emit(
                "updateGame",
                {
                    game,
                    leaderboard
                }
            );
        }
    );

  socket.on(
    "leaveGame",
    ({ gameId }) => {

        if (games[gameId]) {

            delete games[gameId];
        }

        sendGamesList();
    }
);

    socket.on(
        "disconnect",
        () => {

            console.log(
                "Користувач відключився:",
                socket.id
            );

            for (let gameId in games) {

                const game =
                    games[gameId];

                game.players =
                    game.players.filter(
                        (player) =>
                            player.id !== socket.id
                    );

                if (
                    game.players.length === 0
                ) {

                    delete games[gameId];
                }
            }

            sendGamesList();
        }
    );
});

server.listen(
    5000,
    "0.0.0.0",
         () => {

        console.log(
            "Сервер запущено на порту 5000"
        );
    }
);