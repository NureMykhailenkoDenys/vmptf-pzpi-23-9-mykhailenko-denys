import React from "react";

const Leaderboard = ({ leaderboard }) => {

    return (
        <div
            style={{
                marginTop: "40px"
            }}
        >

            <h2>Таблиця лідерів</h2>

            {
                Object.entries(leaderboard).length === 0 && (
                    <p>
                        Немає результатів
                    </p>
                )
            }

            {
                Object.entries(leaderboard)

                    .sort(
                        (a, b) => b[1] - a[1]
                    )

                    .map(
                        ([nick, wins]) => (

                            <p key={nick}>
                                {nick} — {wins}
                            </p>
                        )
                    )
            }

        </div>
    );
};

export default Leaderboard;