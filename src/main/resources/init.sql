CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    birth_day DATE
);

CREATE TABLE IF NOT EXISTS messages(
    id SERIAL PRIMARY KEY,
    sender_id INTEGER REFERENCES users(id),
    receiver_id INTEGER REFERENCES users(id),
    text VARCHAR(999) NOT NULL,
    time TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS friends_relations(
    id SERIAL PRIMARY KEY,
    inviter_id INTEGER REFERENCES users(id),
    receiver_id INTEGER REFERENCES users(id),
    UNIQUE (inviter_id, receiver_id)
);

CREATE TABLE IF NOT EXISTS posts(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    text VARCHAR(999) NOT NULL,
    time TIMESTAMP
)